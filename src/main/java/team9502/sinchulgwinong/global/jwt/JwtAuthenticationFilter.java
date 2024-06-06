package team9502.sinchulgwinong.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import team9502.sinchulgwinong.domain.auth.dto.request.UserLoginRequestDTO;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.response.SuccessCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /*
        Http 요청에서 Jwt 추출, 토큰 검증 및 사용자 인증 정보 설정
        Spring Security의 보안 컨텍스트에 인증 객체 설정
     */

    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        super.setAuthenticationManager(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.objectMapper = new ObjectMapper();
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequestDTO loginRequest = objectMapper.readValue(request.getInputStream(), UserLoginRequestDTO.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = tokenProvider.generateToken(authResult);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        GlobalApiResponse<String> globalApiResponse = GlobalApiResponse.of(SuccessCode.OK.getMessage(), null);
        response.getWriter().write(objectMapper.writeValueAsString(globalApiResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        GlobalApiResponse<String> globalApiResponse = GlobalApiResponse.of(
                ErrorCode.LOGIN_FAILURE.getMessage(),
                null);
        response.getWriter().write(objectMapper.writeValueAsString(globalApiResponse));
    }
}
