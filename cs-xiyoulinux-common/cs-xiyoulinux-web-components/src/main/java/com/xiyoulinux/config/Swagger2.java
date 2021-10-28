package com.xiyoulinux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CoderZk
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    // http://localhost:8088/swagger-ui.html        原路径
    // http://localhost:8088/doc.html               引入的依赖皮肤路径


    @Bean
    public Docket createRestApi() {
//        List<Parameter> pars = new ArrayList<>();
//        ParameterBuilder ticketPar = new ParameterBuilder();
//        ticketPar.name("cs-user").description("user ticket")
//
//                .modelRef(new ModelRef("string")).parameterType("header")
//
//                .required(true).build(); //header中的ticket参数非必填，传空也可以

//        pars.add(ticketPar.build()); //根据每个方法名也知道当前方法在设置什么参数

        return new Docket(DocumentationType.SWAGGER_2)  // 指定api类型为swagger2
                .apiInfo(apiInfo())                     // 用于定义api文档汇总信息
                .select().apis(RequestHandlerSelectors.basePackage("com.xiyoulinux")) // 指定controller包
                .paths(PathSelectors.any())             // 包下的所有controller
                .build();
//                .globalOperationParameters(pars);
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CS平台接口api")           // 文档页标题
                .contact(new Contact("CoderZk",     // 联系人信息
                        "https://blog.csdn.net/weixin_45626515?spm=1011.2124.3001.5343",
                        "1178053078@qq.com"))
                .description("专为CS平台提供的api文档")    // 详细信息
                .version("1.0.1")                        // 文档版本号
                .termsOfServiceUrl("http://cs.xiyoulinux.com") // 网站地址
                .build();
    }
}
