package team9502.sinchulgwinong.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team9502.sinchulgwinong.domain.auth.dto.request.CpUserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.UserLoginResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.companyUser.service.EncryptionService;
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

    public void signup(UserSignupRequestDTO signupRequest) {

        validateSignupRequest(signupRequest.getEmail(), signupRequest.getPassword(),
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
        } catch (Exception e) {
            log.error("회원가입 중 발생한 에러: ", e);
            throw e;
        }
    }

    public void cpSignup(CpUserSignupRequestDTO requestDTO) {

        validateSignupRequest(requestDTO.getCpEmail(), requestDTO.getCpPassword(),
                requestDTO.getCpConfirmPassword(), requestDTO.isAgreeToTerms());

        try {

            String encryptedCpNum = encryptionService.encryptCpNum(requestDTO.getCpNum());

            CompanyUser companyUser = CompanyUser.builder()
                    .hiringStatus(requestDTO.getHiringStatus())
                    .employeeCount(requestDTO.getEmployeeCount())
                    .foundationDate(requestDTO.getFoundationDate())
                    .description(requestDTO.getDescription())
                    .cpNum(encryptedCpNum)
                    .cpName(requestDTO.getCpName())
                    .cpUsername(requestDTO.getCpUsername())
                    .cpEmail(requestDTO.getCpEmail())
                    .cpPhoneNumber(requestDTO.getCpPhoneNumber())
                    .cpPassword(passwordEncoder.encode(requestDTO.getCpPassword()))
                    .build();

            companyUserRepository.save(companyUser);

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

    /*
        중복된 예외처리의 메서드 추출
     */

    private void validateSignupRequest(String email, String password, String confirmPassword, boolean agreeToTerms) {
        if (!agreeToTerms) {
            throw new ApiException(ErrorCode.TERMS_NOT_ACCEPTED);
        }

        if (userRepository.findByEmail(email).isPresent() || companyUserRepository.findByCpEmail(email).isPresent()) {
            throw new ApiException(ErrorCode.EMAIL_DUPLICATION);
        }

        if (!password.equals(confirmPassword)) {
            throw new ApiException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
