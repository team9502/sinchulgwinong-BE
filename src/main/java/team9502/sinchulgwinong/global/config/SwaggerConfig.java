package team9502.sinchulgwinong.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("신출귀농 API 문서")
                        .description("신출귀농 애플리케이션의 API 문서입니다.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("9502")
                                // TODO(은채): 이메일 주소 수정
                                .email("ke808762@gmail.com")));
    }
}
