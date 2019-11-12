package com.cp.swagger_demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2 //开启Swagger2
public class SwaggerConfig {

    //配置了Swagger的Docket的bean实例
    @Bean
    public Docket docket(Environment environment){
        //设置显示的swagger环境
        Profiles profiles = Profiles.of("test","dev");
        //通过environment.acceptsProfiles判断是否处在自己设定的环境中
        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //enable是否启用Swagger，如果为false，则不能在浏览器中访问
                .enable(flag)
                .select()
                //RequestHandlerSelectors,配置扫描接口的方式
                //basePackage()指定要扫描的包
                //any()扫描全部
                //none()都不扫描
                //withClassAnnotation()扫描类上的注解
                //withMethodAnnotation()扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("com.cp.swagger_demo.controller"))
                //paths 过滤，只扫描
                .paths(PathSelectors.ant("/**"))
                .build();
    }

    //配置Swagger信息=apiInfo
    private ApiInfo apiInfo(){
        return new ApiInfo("CP的SwaggerAPI文档",
                "嘻嘻嘻",
                "1.0",
                "47.96.128.98",
                //作者信息
                new Contact("cp","47.96.128.98","770047395@qq.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());

    }
}
