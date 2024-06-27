package team9502.sinchulgwinong.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import team9502.sinchulgwinong.global.jwt.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public WebSecurityConfig(JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/home",
                                "/aws",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/auth/signup",
                                "/auth/login",
                                "/auth/cp-signup",
                                "/auth/cp-login",
                                "/email/**",
                                "/social-login/**",
                                "/cpUsers/{cpUserId}/profile",
                                "/business/status",
                                "/business/verify",
                                "/faqs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/cpUsers",
                                "/boards",
                                "/boards/{boardId}",
                                "/boards/find-boards",
                                "/job-boards",
                                "/job-boards/region-name",
                                "/job-boards/sub-region-name",
                                "/job-boards/locality-name",
                                "/job-boards/major-category-name",
                                "/job-boards/minor-category-name",
                                "/job-boards/cp-user/{cpUserId}/open-api",
                                "/job-boards/{jobBoardId}",
                                "/job-boards/cp-user/{cpUserId}/my-job-boards").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/job-boards/locality-category",
                                "/job-boards/job-category"
                                ).permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractAuthenticationFilterConfigurer::disable)
                .logout(LogoutConfigurer::permitAll);

        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
