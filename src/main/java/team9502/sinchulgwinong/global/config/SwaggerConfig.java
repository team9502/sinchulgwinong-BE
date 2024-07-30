package team9502.sinchulgwinong.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // 로컬 서버 설정
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        // 프로덕션 HTTPS 서버 설정
        Server prodHttpsServer = new Server();
        prodHttpsServer.setUrl("https://www.sinchulgwinong.site/");
        prodHttpsServer.setDescription("Production HTTPS Server");

        // 개발 서버 설정
        Server devServer = new Server();
        devServer.setUrl("https://cat.sinchulgwinong.site/");
        devServer.setDescription("Development Server");

        return new OpenAPI()
                .servers(Arrays.asList(localServer, prodHttpsServer, devServer))
                .info(new Info()
                        .title("신출귀농 API 문서")
                        .description("""
                                ### 신출귀농 애플리케이션의 API 문서입니다.
                                                   \s
                                #### [백엔드 개발자]
                                1. 김은채
                                  ke808762@gmail.com
                                2. 창다은
                                  cdaeun95@gmail.com""")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("9502")
                                .email("9502team@gmail.com")));
    }
}
