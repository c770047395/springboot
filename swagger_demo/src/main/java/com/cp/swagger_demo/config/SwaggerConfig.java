package com.cp.swagger_demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
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
