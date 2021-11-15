package com.xiyoulinux.filter;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.auth.service.IAuthService;
import com.xiyoulinux.common.*;
import com.xiyoulinux.constant.AuthCommonConstant;
import com.xiyoulinux.constant.GlobalConstant;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.pojo.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 一个全局过滤器，拦截登陆，注册，以及用户权限的验证
 *
 * @author qkm
 */
@Slf4j
@Service
public class GlobalRequestFilter implements GlobalFilter, Ordered {

    @DubboReference
    private IAuthService iauthService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        UsernameAndPassword usernameAndPassword = JSON.parseObject(
                parseBodyFromRequest(request), UsernameAndPassword.class
        );
        // 1. 如果是登录
        if (request.getURI().getPath().contains(GlobalConstant.USER_LOGIN_PATH)) {
            // 去授权中心拿 token

            JSONResult jsonResult = null;
            try {
                JwtToken token = iauthService.login(usernameAndPassword);
                jsonResult = JSONResult.ok(token);
            } catch (Exception e) {
                log.error("user [{}] login get token error [{}]", usernameAndPassword.getUsername(),e.getMessage(),e);
                jsonResult = JSONResult.errorMsg(ReturnCode.ERROR.code, "user login get token error");
            }
            byte[] bytes = JSON.toJSONBytes(jsonResult);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        }

        // 2. 如果是注册
        if (request.getURI().getPath().contains(GlobalConstant.USER_REGISTER_PATH)) {
            // 去授权中心拿 token: 先创建用户, 再返回 Token
            JSONResult jsonResult = null;
            try {
                JwtToken token = iauthService.register(usernameAndPassword);
                jsonResult = JSONResult.ok(token);
            } catch (Exception e) {
                log.error("user [{}] register get token error [{}]", usernameAndPassword.getUsername(),e.getMessage(),e);
                jsonResult = JSONResult.errorMsg(ReturnCode.ERROR.code, "user register get token error");
            }
            byte[] bytes = JSON.toJSONBytes(jsonResult);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        }

        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AuthCommonConstant.JWT_USER_INFO_KEY);
        LoginUserInfo loginUserInfo = null;

        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("parse user info from token error: [{}]", ex.getMessage(), ex);
        }

        // 获取不到登录用户信息, 返回 401
        if (null == loginUserInfo) {
            byte[] result = JSON.toJSONBytes(JSONResult.errorMsg(ReturnCode.UNAUTHORIZED.code,
                    "login expired or not has auth !!"));
            DataBuffer buffer = response.bufferFactory().wrap(result);
            return response.writeWith(Flux.just(buffer));
        }

        //3. 如果是刷新token
        if (request.getURI().getPath().contains(GlobalConstant.USER_REFRESH_PATH)) {
            JSONResult jsonResult = null;
            try {
                JwtToken newToken = iauthService.refresh(loginUserInfo);
                jsonResult = JSONResult.ok(newToken);
            } catch (Exception e) {
                log.error("user [{}], refresh token error,[{}]",loginUserInfo.getUsername(),e.getMessage(),e);
                jsonResult = JSONResult.errorMsg(ReturnCode.ERROR.code, "user refresh token error");
            }
            byte[] bytes = JSON.toJSONBytes(jsonResult);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        }

        // 4.访问其他的服务,解析通过, 则放行
        return chain.filter(exchange);
    }


    /**
     * 从 Post 请求中获取到请求数据
     */
    private String parseBodyFromRequest(ServerHttpRequest request) {

        // 获取请求体
        Flux<DataBuffer> body = request.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();

        // 订阅缓冲区去消费请求体中的数据
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            // 一定要使用 DataBufferUtils.release 释放掉, 否则, 会出现内存泄露
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });

        // 获取 request body
        return bodyRef.get();
    }

    public void get() {

    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
