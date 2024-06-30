package team9502.sinchulgwinong.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.auth.dto.request.CompanyUserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.CpUserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.CompanyUserLoginResponseDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.UserLoginResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.companyUser.service.EncryptionService;
import team9502.sinchulgwinong.domain.email.service.EmailVerificationService;
import team9502.sinchulgwinong.domain.oauth.enums.SocialType;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyUserRepository companyUserRepository;
    private final EncryptionService encryptionService;
    private final AuthenticationManager authenticationManager;
    private final PointService pointService;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public void signup(UserSignupRequestDTO requestDTO) {
        validateSignupRequest(requestDTO.getEmail(), requestDTO.getPassword(), requestDTO.getConfirmPassword(), requestDTO.isAgreeToTerms());
        validateEmailVerification(requestDTO.getEmail());

        try {
            User user = createUser(requestDTO);
            userRepository.save(user);
            pointService.earnPoints(user, SpType.SIGNUP);
        } catch (Exception e) {
            log.error("회원가입 중 발생한 에러: ", e);
            throw e;
        }
    }

    @Transactional
    public void cpSignup(CpUserSignupRequestDTO requestDTO) {
        validateSignupRequest(requestDTO.getCpEmail(), requestDTO.getCpPassword(), requestDTO.getCpConfirmPassword(), requestDTO.isAgreeToTerms());
        validateEmailVerification(requestDTO.getCpEmail());

        try {
            CompanyUser companyUser = createCompanyUser(requestDTO);
            companyUserRepository.save(companyUser);
            pointService.earnPoints(companyUser, SpType.SIGNUP);
        } catch (Exception e) {
            log.error("기업 회원가입 중 발생한 에러: ", e);
            throw e;
        }
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO loginRequest) {
        Authentication authentication = authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        return createUserLoginResponseDTO(loginRequest.getEmail());
    }

    public CompanyUserLoginResponseDTO cpLogin(CompanyUserLoginRequestDTO loginRequest) {
        Authentication authentication = authenticateUser(loginRequest.getCpEmail(), loginRequest.getCpPassword());
        return createCompanyUserLoginResponseDTO(loginRequest.getCpEmail());
    }

    private void validateSignupRequest(String email, String password, String confirmPassword, boolean agreeToTerms) {
        if (!agreeToTerms) {
            throw new ApiException(ErrorCode.TERMS_NOT_ACCEPTED);
        }
        if (userRepository.findByEmail(email).isPresent() || companyUserRepository.findByCpEmail(email).isPresent()) {
            throw new ApiException(ErrorCode.EMAIL_DUPLICATION);
        }
        if (password == null || password.isEmpty()) {
            throw new ApiException(ErrorCode.PASSWORD_CANNOT_BE_NULL);
        }
        if (!password.equals(confirmPassword)) {
            throw new ApiException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }
    }

    private void validateEmailVerification(String email) {
        if (!emailVerificationService.isEmailVerified(email)) {
            throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED);
        }
    }

    private User createUser(UserSignupRequestDTO requestDTO) {
        return User.builder()
                .username(requestDTO.getUsername())
                .nickname(requestDTO.getNickname())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .phoneNumber(requestDTO.getPhoneNumber())
                .loginType(SocialType.NORMAL)
                .build();
    }

    private CompanyUser createCompanyUser(CpUserSignupRequestDTO requestDTO) {
        CompanyUser companyUser = CompanyUser.builder()
                .hiringStatus(requestDTO.getHiringStatus())
                .employeeCount(requestDTO.getEmployeeCount())
                .foundationDate(requestDTO.getFoundationDate())
                .description(requestDTO.getDescription())
                .cpNum(encryptionService.encryptCpNum(requestDTO.getCpNum()))
                .cpName(requestDTO.getCpName())
                .cpUsername(requestDTO.getCpUsername())
                .cpEmail(requestDTO.getCpEmail())
                .cpPhoneNumber(requestDTO.getCpPhoneNumber())
                .cpPassword(passwordEncoder.encode(requestDTO.getCpPassword()))
                .viewCount(0)
                .build();
        return companyUser;
    }

    private Authentication authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private UserLoginResponseDTO createUserLoginResponseDTO(String email) {
        User user = userRepository.findByEmail(email)
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

    private CompanyUserLoginResponseDTO createCompanyUserLoginResponseDTO(String email) {
        CompanyUser companyUser = companyUserRepository.findByCpEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.COMPANY_USER_NOT_FOUND));
        return new CompanyUserLoginResponseDTO(
                companyUser.getCpUserId(),
                companyUser.getCpUsername(),
                companyUser.getCpName(),
                companyUser.getCpEmail(),
                companyUser.getCpPhoneNumber(),
                companyUser.getHiringStatus(),
                companyUser.getEmployeeCount()
        );
    }

    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", "")
                .path("/")
                .domain(".sinchulgwinong.site")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }
}
