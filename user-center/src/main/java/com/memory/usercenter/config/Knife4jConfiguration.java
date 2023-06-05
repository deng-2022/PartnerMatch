package com.memory.usercenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author 邓哈哈
 * 2023/4/2 23:39
 * Function: 快速生成接口文档
 * Version 1.0
 */
@Configuration
@EnableSwagger2WebMvc
@Profile({"dev", "test"})
public class Knife4jConfiguration {
    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .title("Memory用户中心")
                        .description("# Memory用户中心接口文档")
                        .termsOfServiceUrl("https://gitee.com/deng-2022/client-center")
                        .contact("3348407547@qq.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("用户服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.memory.usercenter.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
