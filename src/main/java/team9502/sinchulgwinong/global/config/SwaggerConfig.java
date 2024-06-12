package team9502.sinchulgwinong.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("신출귀농 API 문서")
                        .description("""
                                ### 신출귀농 애플리케이션의 API 문서입니다.

                                #### [백엔드 개발자]
                                                               \s
                                1. 김은채
                                  ke808762@gmail.com
                                                               \s
                                2. 창다은
                                  cdaeun95@gmail.com""")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("9502")
                                .email("9502team@gmail.com")));
    }
}
