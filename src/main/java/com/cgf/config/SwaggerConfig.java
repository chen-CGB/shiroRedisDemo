package com.cgf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableOpenApi
public class SwaggerConfig {


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("高校就业推荐后台系统")
                .description("高校就业推荐后台系统，提供一套API说明")
                .version("1.0.0")
                .contact(new Contact("Luke", "http://www.xxx.com", "xxxx@mail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .build();
    }

    @Bean()
    public Docket api() {
        //添加请求头
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("Authorization")
                .description("认证token")
                .in(ParameterType.HEADER)
                .required(false)
                .build());

        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .globalRequestParameters(parameters)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("cgf")
                .enable(true) //正式部署时，改为false：关闭swagger
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.cgf.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}