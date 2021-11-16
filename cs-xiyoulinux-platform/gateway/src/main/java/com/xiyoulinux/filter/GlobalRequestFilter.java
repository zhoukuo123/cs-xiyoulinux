package com.xiyoulinux.filter;

import com.alibaba.fastjson.JSON;
import com.xiyoulinux.common.*;
import com.xiyoulinux.constant.AuthCommonConstant;
import com.xiyoulinux.constant.GlobalConstant;
import com.xiyoulinux.enums.ReturnCode;
import com.xiyoulinux.pojo.JSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 一个全局过滤器，拦截登陆，注册，以及用户权限的验证
 *
 * @author qkm
 */
@Slf4j
@Service
public class GlobalRequestFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 1. 如果是登录、注册、刷新token
        if (request.getURI().getPath().contains(GlobalConstant.USER_LOGIN_PATH) ||
                request.getURI().getPath().contains(GlobalConstant.USER_REGISTER_PATH) ||
                request.getURI().getPath().contains(GlobalConstant.USER_REFRESH_PATH)
        ) {
            return chain.filter(exchange);
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

        // 2.访问其他的服务,解析通过, 则放行
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
