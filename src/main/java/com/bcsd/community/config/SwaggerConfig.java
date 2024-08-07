package com.bcsd.community.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Community api")
                .version("v1")
                .description("스프링으로 만들어진 간단한 게시판 커뮤니티 백엔드 api입니다.")
                .contact(new Contact()
                        .name("김학인")
                        .email("abc6271416@gmail.com"));
        return new OpenAPI().info(info);
    }
}