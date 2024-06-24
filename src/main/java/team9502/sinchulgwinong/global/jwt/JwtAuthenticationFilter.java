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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import team9502.sinchulgwinong.domain.auth.dto.request.CompanyUserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.CompanyUserLoginResponseDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.UserLoginResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;
import team9502.sinchulgwinong.global.response.GlobalApiResponse;
import team9502.sinchulgwinong.global.response.SuccessCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;

    @Autowired
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
                                   UserRepository userRepository, CompanyUserRepository companyUserRepository) {
        super.setAuthenticationManager(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.objectMapper = new ObjectMapper();
        this.userRepository = userRepository;
        this.companyUserRepository = companyUserRepository;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // 요청 URI 검사를 통해 필터 적용 결정
        String uri = request.getRequestURI();
        return uri.equals("/auth/login") || uri.equals("/auth/cp-login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            if (request.getRequestURI().contains("/cp-login")) {
                CompanyUserLoginRequestDTO loginRequest = objectMapper.readValue(request.getInputStream(), CompanyUserLoginRequestDTO.class);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                        loginRequest.getCpEmail(), loginRequest.getCpPassword());
                return getAuthenticationManager().authenticate(authRequest);
            } else {
                UserLoginRequestDTO loginRequest = objectMapper.readValue(request.getInputStream(), UserLoginRequestDTO.class);
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword());
                return getAuthenticationManager().authenticate(authRequest);
            }
        } catch (IOException e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = tokenProvider.generateToken(authResult);

        // 쿠키 문자열 수동 설정
        String cookieValue = "AUTH_TOKEN=" + accessToken + "; Path=/; Max-Age=" + (60 * 60) + "; HttpOnly; Secure; SameSite=None";  // 1시간 동안 유효

        response.addHeader("Set-Cookie", cookieValue);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        Object userResponseDto = getUserResponseDTO(request.getRequestURI(), userDetails.getUsername());

        GlobalApiResponse<Object> globalApiResponse = GlobalApiResponse.of(SuccessCode.OK.getMessage(), userResponseDto);
        response.getWriter().write(objectMapper.writeValueAsString(globalApiResponse));
    }


    private Object getUserResponseDTO(String requestUri, String username) throws ApiException {
        if (requestUri.contains("/cp-login")) {
            CompanyUser cpUser = companyUserRepository.findByCpEmail(username)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
            return new CompanyUserLoginResponseDTO(
                    cpUser.getCpUserId(),
                    cpUser.getCpUsername(),
                    cpUser.getCpName(),
                    cpUser.getCpEmail(),
                    cpUser.getCpPhoneNumber(),
                    cpUser.getHiringStatus(),
                    cpUser.getEmployeeCount()
            );
        } else {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
            return new UserLoginResponseDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getNickname(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getLoginType()
            );
        }
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse
            response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        GlobalApiResponse<String> globalApiResponse = GlobalApiResponse.of(
                "인증 실패: " + failed.getMessage(),
                null);
        response.getWriter().write(objectMapper.writeValueAsString(globalApiResponse));
    }
}
