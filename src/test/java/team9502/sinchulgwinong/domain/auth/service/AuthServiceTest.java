package team9502.sinchulgwinong.domain.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import team9502.sinchulgwinong.domain.auth.dto.request.CpUserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserSignupRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.companyUser.service.EncryptionService;
import team9502.sinchulgwinong.domain.email.enums.UserType;
import team9502.sinchulgwinong.domain.email.service.EmailVerificationService;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PointService pointService;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private CompanyUserRepository companyUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Mock
    private EncryptionService encryptionService;


    @Nested
    @DisplayName("구직자 회원가입 테스트")
    class signup {

        @Test
        @DisplayName("회원가입 성공")
        void signupShouldCreateNewUserAccountWithValidInput() {
            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("test@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setUsername("TestUser");
            requestDTO.setNickname("TestNick");
            requestDTO.setPhoneNumber("1234567890");

            when(emailVerificationService.isEmailVerified("test@example.com")).thenReturn(true);
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

            // when
            authService.signup(requestDTO);

            // then
            verify(userRepository).save(any(User.class));
            verify(pointService).earnPoints(any(User.class), eq(SpType.SIGNUP));
        }

        @Test
        @DisplayName("회원가입 실패: 이미 존재하는 이메일")
        void signupShouldThrowExceptionWhenEmailAlreadyExists() {
            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("existing@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setUsername("ExistingUser");
            requestDTO.setNickname("ExistingNick");
            requestDTO.setPhoneNumber("1234567890");

            when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new User()));

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.signup(requestDTO);
            });

            // then
            verify(userRepository).findByEmail("existing@example.com");
            verify(userRepository, never()).save(any(User.class));
            verify(emailVerificationService, never()).isEmailVerified(any());
        }

        @Test
        @DisplayName("회원가입 실패: 이메일 인증이 확인 되지 않음")
        void signupShouldThrowExceptionWhenEmailIsNotVerified() {
            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("test@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setUsername("TestUser");
            requestDTO.setNickname("TestNick");
            requestDTO.setPhoneNumber("1234567890");

            when(emailVerificationService.isEmailVerified("test@example.com")).thenReturn(false);

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.signup(requestDTO);
            });

            // then
            verify(emailVerificationService).isEmailVerified("test@example.com");
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("회원가입 실패: 비밀번호와 비밀번호 확인이 일치하지 않음")
        void signupShouldThrowExceptionWhenPasswordsAreNotMatching() {
            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("test@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("differentPassword");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setUsername("TestUser");
            requestDTO.setNickname("TestNick");
            requestDTO.setPhoneNumber("1234567890");

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.signup(requestDTO);
            });

            // then
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("회원가입 실패: 약관 미동의")
        void signupShouldThrowExceptionWhenTermsAreNotAgreed() {
            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("test@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("password123");
            requestDTO.setAgreeToTerms(false);
            requestDTO.setUsername("TestUser");
            requestDTO.setNickname("TestNick");
            requestDTO.setPhoneNumber("1234567890");

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.signup(requestDTO);
            });

            // then
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("회원가입 실패: 이메일, 닉네임, 전화번호 중 하나 이상이 null")
        void signupShouldThrowExceptionWhenSomeFieldIsMissing() {
            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("test@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.signup(requestDTO);
            });

            // then
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("구인자 회원가입 테스트")
    class cpUserSignup {

        @Test
        @DisplayName("회원가입 성공")
        void cpSignupShouldCreateNewUserAccountWithValidInput() {
            // given
            CpUserSignupRequestDTO requestDTO = new CpUserSignupRequestDTO();
            requestDTO.setCpEmail("test@example.com");
            requestDTO.setCpPassword("password123");
            requestDTO.setCpConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setCpUsername("TestUser");
            requestDTO.setCpNum("1234567890");
            requestDTO.setDescription("Test Company");
            requestDTO.setEmployeeCount(10);
            requestDTO.setFoundationDate(LocalDate.of(1999, 10, 6));
            requestDTO.setHiringStatus(true);

            // when
            when(emailVerificationService.isEmailVerified("test@example.com")).thenReturn(true);
            when(companyUserRepository.findByCpEmail("test@example.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
            when(encryptionService.encryptCpNum(any(String.class))).thenReturn("encryptedCpNum");
            when(companyUserRepository.save(any(CompanyUser.class))).thenReturn(new CompanyUser());

            // then
            authService.cpSignup(requestDTO);

            verify(companyUserRepository).save(any(CompanyUser.class));
            verify(pointService).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService).encryptCpNum(eq("1234567890"));
        }

        @Test
        @DisplayName("회원가입 실패: 이미 존재하는 이메일")
        void cpSignupShouldThrowExceptionWhenCpEmailAlreadyExists() {
            // given
            CpUserSignupRequestDTO requestDTO = new CpUserSignupRequestDTO();
            requestDTO.setCpEmail("existing@example.com");
            requestDTO.setCpPassword("password123");
            requestDTO.setCpConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setCpUsername("ExistingUser");
            requestDTO.setCpNum("1234567890");
            requestDTO.setDescription("Test Company");
            requestDTO.setEmployeeCount(10);
            requestDTO.setFoundationDate(LocalDate.of(1999, 10, 6));
            requestDTO.setHiringStatus(true);

            // when
            when(companyUserRepository.findByCpEmail("existing@example.com")).thenReturn(Optional.of(new CompanyUser()));

            // then
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.cpSignup(requestDTO);
            });

            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
        }

        @Test
        @DisplayName("회원가입 실패: 이메일 인증이 확인 되지 않음")
        void cpSignupShouldThrowExceptionWhenCpEmailIsNotVerified() {
            // given
            CpUserSignupRequestDTO requestDTO = new CpUserSignupRequestDTO();
            requestDTO.setCpEmail("existing@example.com");
            requestDTO.setCpPassword("password123");
            requestDTO.setCpConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setCpUsername("ExistingUser");
            requestDTO.setCpNum("1234567890");
            requestDTO.setDescription("Test Company");
            requestDTO.setEmployeeCount(10);
            requestDTO.setFoundationDate(LocalDate.of(1999, 10, 6));
            requestDTO.setHiringStatus(true);

            // when
            when(emailVerificationService.isEmailVerified("existing@example.com")).thenReturn(false);

            // then
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.cpSignup(requestDTO);
            });

            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
        }

        @Test
        @DisplayName("회원가입 실패: 약관 미동의")
        void cpSignupShouldThrowExceptionWhenTermsAreNotAgreed() {
            // given
            CpUserSignupRequestDTO requestDTO = new CpUserSignupRequestDTO();
            requestDTO.setCpEmail("existing@example.com");
            requestDTO.setCpPassword("password123");
            requestDTO.setCpConfirmPassword("password123");
            requestDTO.setAgreeToTerms(false);
            requestDTO.setCpUsername("ExistingUser");
            requestDTO.setCpNum("1234567890");
            requestDTO.setDescription("Test Company");
            requestDTO.setEmployeeCount(10);
            requestDTO.setFoundationDate(LocalDate.of(1999, 10, 6));
            requestDTO.setHiringStatus(true);

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.cpSignup(requestDTO);
            });

            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
            verify(emailVerificationService, never()).createVerification(anyString(), any(UserType.class));
        }

        @Test
        @DisplayName("회원가입 실패: 이메일, 닉네임, 전화번호 중 하나 이상이 null")
        void cpSignupShouldThrowExceptionWhenSomeFieldIsMissing() {
            // given
            CpUserSignupRequestDTO requestDTO = new CpUserSignupRequestDTO();
            requestDTO.setCpEmail("existing@example.com");
            requestDTO.setCpPassword("password123");
            requestDTO.setCpConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setCpUsername(null);
            requestDTO.setCpNum("1234567890");
            requestDTO.setDescription("Test Company");
            requestDTO.setEmployeeCount(10);
            requestDTO.setFoundationDate(LocalDate.of(1999, 10, 6));
            requestDTO.setHiringStatus(true);

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.cpSignup(requestDTO);
            });

            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
            verify(emailVerificationService, never()).createVerification(anyString(), any(UserType.class));
        }

        @Test
        @DisplayName("회원가입 실패: 비밀번호, 비밀번호 확인이 일치하지 않음")
        void cpSignupShouldThrowExceptionWhenPasswordAndConfirmPasswordNotMatch() {
            // given
            CpUserSignupRequestDTO requestDTO = new CpUserSignupRequestDTO();
            requestDTO.setCpEmail("existing@example.com");
            requestDTO.setCpPassword("password123");
            requestDTO.setCpConfirmPassword("wrongPassword");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setCpUsername("ExistingUser");
            requestDTO.setCpNum("1234567890");
            requestDTO.setDescription("Test Company");
            requestDTO.setEmployeeCount(10);
            requestDTO.setFoundationDate(LocalDate.of(1999, 10, 6));
            requestDTO.setHiringStatus(true);

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.cpSignup(requestDTO);
            });

            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
            verify(emailVerificationService, never()).createVerification(anyString(), any(UserType.class));
            verify(passwordEncoder, never()).encode(any(CharSequence.class));
        }
    }
}