package team9502.sinchulgwinong.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team9502.sinchulgwinong.domain.auth.dto.request.UserSignupRequestDTO;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.enums.Role;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserSignupRequestDTO signupRequest) {
        try {
            Role userRole = determineUserRole(signupRequest.getCompanyNum());

            User user = User.builder()
                    .username(signupRequest.getUsername())
                    .nickname(signupRequest.getNickname())
                    .email(signupRequest.getEmail())
                    .password(passwordEncoder.encode(signupRequest.getPassword()))
                    .role(userRole)
                    .loginType(signupRequest.getLoginType())
                    .companyNum(signupRequest.getCompanyNum() == null || signupRequest.getCompanyNum().isEmpty() ? null : signupRequest.getCompanyNum())
                    .build();

            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error during signup: ", e);
            throw e;
        }
    }

    private Role determineUserRole(String companyNum) {
        return companyNum != null && !companyNum.isEmpty() ? Role.RECRUITER : Role.JOBSEEKER;
    }
}
