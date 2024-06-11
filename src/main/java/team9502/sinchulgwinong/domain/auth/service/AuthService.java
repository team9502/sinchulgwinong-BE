package team9502.sinchulgwinong.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;
import team9502.sinchulgwinong.global.jwt.JwtTokenProvider;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyUserRepository companyUserRepository;
    private final EncryptionService encryptionService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PointService pointService;

    @Transactional
    public void signup(UserSignupRequestDTO signupRequest) {
        validateUserSignupRequest(signupRequest.getEmail(), signupRequest.getPassword(),
                signupRequest.getConfirmPassword(), signupRequest.isAgreeToTerms());

        try {
            User user = User.builder()
                    .username(signupRequest.getUsername())
                    .nickname(signupRequest.getNickname())
                    .email(signupRequest.getEmail())
                    .password(passwordEncoder.encode(signupRequest.getPassword()))
                    .loginType(signupRequest.getLoginType())
                    .build();

            userRepository.save(user);
            pointService.earnPoints(user, SpType.SIGNUP);

        } catch (Exception e) {
            log.error("회원가입 중 발생한 에러: ", e);
            throw e;
        }
    }

    @Transactional
    public void cpSignup(CpUserSignupRequestDTO requestDTO) {
        validateCpSignupRequest(requestDTO.getCpEmail(), requestDTO.getCpPassword(),
                requestDTO.getCpConfirmPassword(), requestDTO.isAgreeToTerms());

        try {
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
                    .build();

            companyUserRepository.save(companyUser);
            pointService.earnPoints(companyUser, SpType.SIGNUP);

        } catch (Exception e) {
            log.error("기업 회원가입 중 발생한 에러: ", e);
            throw e;
        }
    }


    public UserLoginResponseDTO login(UserLoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }

        User user = userOptional.get();

        return new UserLoginResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getLoginType()
        );
    }

    public CompanyUserLoginResponseDTO cpLogin(CompanyUserLoginRequestDTO loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getCpEmail(), loginRequest.getCpPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        Optional<CompanyUser> companyUserOptional = companyUserRepository.findByCpEmail(loginRequest.getCpEmail());
        if (companyUserOptional.isEmpty()) {
            throw new ApiException(ErrorCode.USER_NOT_FOUND);
        }

        CompanyUser companyUser = companyUserOptional.get();

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

    /*
        중복된 예외처리의 메서드 추출
     */

    private void validateUserSignupRequest(String email, String password, String confirmPassword, boolean agreeToTerms) {
        if (!agreeToTerms) {
            throw new ApiException(ErrorCode.TERMS_NOT_ACCEPTED);
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ApiException(ErrorCode.EMAIL_DUPLICATION);
        }

        if (!password.equals(confirmPassword)) {
            throw new ApiException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

    private void validateCpSignupRequest(String cpEmail, String cpPassword, String cpConfirmPassword, boolean agreeToTerms) {
        if (!agreeToTerms) {
            throw new ApiException(ErrorCode.TERMS_NOT_ACCEPTED);
        }

        if (companyUserRepository.findByCpEmail(cpEmail).isPresent()) {
            throw new ApiException(ErrorCode.EMAIL_DUPLICATION);
        }

        if (!cpPassword.equals(cpConfirmPassword)) {
            throw new ApiException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
