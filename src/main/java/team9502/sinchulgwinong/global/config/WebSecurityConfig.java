package team9502.sinchulgwinong.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home").permitAll()  // 메인페이지와 홈페이지는 모든 사용자가 접근 가능
                        .anyRequest().authenticated())  // 그 외 요청은 인증 필요
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())  // 로그인 페이지는 모든 사용자가 접근 가능
                .logout(LogoutConfigurer::permitAll);  // 로그아웃 요청에 대해서도 모든 사용자가 접근 가능


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();  // 비밀번호 암호화를 위한 Bcrypt 암호화 방식 사용
    }
}
