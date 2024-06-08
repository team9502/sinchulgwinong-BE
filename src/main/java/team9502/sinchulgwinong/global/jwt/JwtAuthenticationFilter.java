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
        setFilterProcessesUrl("/auth/cp-login");
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

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        if (request.getRequestURI().contains("/cp-login")) {
            CompanyUser cpUser = companyUserRepository.findByCpEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

            CompanyUserLoginResponseDTO companyUserLoginResponseDTO = new CompanyUserLoginResponseDTO(
                    cpUser.getCpUserId(),
                    cpUser.getCpUsername(),
                    cpUser.getCpName(),
                    cpUser.getCpEmail(),
                    cpUser.getCpPhoneNumber(),
                    cpUser.getHiringStatus(),
                    cpUser.getEmployeeCount()
            );

            GlobalApiResponse<CompanyUserLoginResponseDTO> globalApiResponse = GlobalApiResponse.of(SuccessCode.OK.getMessage(), companyUserLoginResponseDTO);
            response.getWriter().write(objectMapper.writeValueAsString(globalApiResponse));

        } else {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

            UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getNickname(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getLoginType()
            );

            GlobalApiResponse<UserLoginResponseDTO> globalApiResponse = GlobalApiResponse.of(SuccessCode.OK.getMessage(), userLoginResponseDTO);
            response.getWriter().write(objectMapper.writeValueAsString(globalApiResponse));
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
