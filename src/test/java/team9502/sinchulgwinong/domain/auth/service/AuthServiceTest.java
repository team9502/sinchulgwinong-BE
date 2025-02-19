package team9502.sinchulgwinong.domain.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import team9502.sinchulgwinong.domain.auth.dto.request.CompanyUserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.CpUserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserLoginRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.request.UserSignupRequestDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.CompanyUserLoginResponseDTO;
import team9502.sinchulgwinong.domain.auth.dto.response.UserLoginResponseDTO;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceTest.class);

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

    @Mock
    private AuthenticationManager authenticationManager;

    @Nested
    @DisplayName("구직자 회원가입 테스트")
    class signup {

        @Test
        @DisplayName("회원가입 성공")
        void 회원가입_성공() {
            logger.info("===== 회원가입_성공 테스트 시작 =====");

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
        void 이미_존재하는_이메일로_회원가입_실패() {
            logger.info("===== 이미_존재하는_이메일로_회원가입_실패 테스트 시작 =====");

            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("existing@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setUsername("ExistingUser");
            requestDTO.setNickname("ExistingNick");
            requestDTO.setPhoneNumber("1234567890");

            when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

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
        void 이메일_미인증_회원가입_실패() {
            logger.info("===== 이메일_미인증_회원가입_실패 테스트 시작 =====");

            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("test@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            requestDTO.setUsername("TestUser");
            requestDTO.setNickname("TestNick");
            requestDTO.setPhoneNumber("1234567890");

            when(emailVerificationService.isEmailVerified("test@example.com"))
                .thenReturn(false);

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
        void 비밀번호_불일치_회원가입_실패() {
            logger.info("===== 비밀번호_불일치_회원가입_실패 테스트 시작 =====");

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
        void 약관_미동의_회원가입_실패() {
            logger.info("===== 약관_미동의_회원가입_실패 테스트 시작 =====");

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
        void 필수_입력값_누락_회원가입_실패() {
            logger.info("===== 필수_입력값_누락_회원가입_실패 테스트 시작 =====");

            // given
            UserSignupRequestDTO requestDTO = new UserSignupRequestDTO();
            requestDTO.setEmail("test@example.com");
            requestDTO.setPassword("password123");
            requestDTO.setConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            // 닉네임, 전화번호 등이 세팅되지 않음

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
        void 구인자_회원가입_성공() {
            logger.info("===== 구인자_회원가입_성공 테스트 시작 =====");

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

            when(emailVerificationService.isEmailVerified("test@example.com")).thenReturn(true);
            when(companyUserRepository.findByCpEmail("test@example.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
            when(encryptionService.encryptCpNum(any(String.class))).thenReturn("encryptedCpNum");
            when(companyUserRepository.save(any(CompanyUser.class))).thenReturn(new CompanyUser());

            // when
            authService.cpSignup(requestDTO);

            // then
            verify(companyUserRepository).save(any(CompanyUser.class));
            verify(pointService).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService).encryptCpNum(eq("1234567890"));
        }

        @Test
        @DisplayName("회원가입 실패: 이미 존재하는 이메일")
        void 이미_존재하는_이메일_구인자_회원가입_실패() {
            logger.info("===== 이미_존재하는_이메일_구인자_회원가입_실패 테스트 시작 =====");

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

            when(companyUserRepository.findByCpEmail("existing@example.com"))
                .thenReturn(Optional.of(new CompanyUser()));

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.cpSignup(requestDTO);
            });

            // then
            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
        }

        @Test
        @DisplayName("회원가입 실패: 이메일 인증이 확인 되지 않음")
        void 이메일_미인증_구인자_회원가입_실패() {
            logger.info("===== 이메일_미인증_구인자_회원가입_실패 테스트 시작 =====");

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

            when(emailVerificationService.isEmailVerified("existing@example.com"))
                .thenReturn(false);

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.cpSignup(requestDTO);
            });

            // then
            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
        }

        @Test
        @DisplayName("회원가입 실패: 약관 미동의")
        void 약관_미동의_구인자_회원가입_실패() {
            logger.info("===== 약관_미동의_구인자_회원가입_실패 테스트 시작 =====");

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

            // then
            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
            verify(emailVerificationService, never()).createVerification(anyString(), any(UserType.class));
        }

        @Test
        @DisplayName("회원가입 실패: 이메일, 닉네임, 전화번호 중 하나 이상이 null")
        void 필수_입력값_누락_구인자_회원가입_실패() {
            logger.info("===== 필수_입력값_누락_구인자_회원가입_실패 테스트 시작 =====");

            // given
            CpUserSignupRequestDTO requestDTO = new CpUserSignupRequestDTO();
            requestDTO.setCpEmail("existing@example.com");
            requestDTO.setCpPassword("password123");
            requestDTO.setCpConfirmPassword("password123");
            requestDTO.setAgreeToTerms(true);
            // cpUsername 등 일부 필드가 null로 남아있음

            // when
            ApiException exception = assertThrows(ApiException.class, () -> {
                authService.cpSignup(requestDTO);
            });

            // then
            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
            verify(emailVerificationService, never()).createVerification(anyString(), any(UserType.class));
        }

        @Test
        @DisplayName("회원가입 실패: 비밀번호와 비밀번호 확인이 일치하지 않음")
        void 비밀번호_불일치_구인자_회원가입_실패() {
            logger.info("===== 비밀번호_불일치_구인자_회원가입_실패 테스트 시작 =====");

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

            // then
            verify(companyUserRepository, never()).save(any(CompanyUser.class));
            verify(pointService, never()).earnPoints(any(CompanyUser.class), eq(SpType.SIGNUP));
            verify(encryptionService, never()).encryptCpNum(any(String.class));
            verify(emailVerificationService, never()).createVerification(anyString(), any(UserType.class));
            verify(passwordEncoder, never()).encode(any(CharSequence.class));
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class login {

        @Test
        @DisplayName("구직자 로그인 성공")
        void 구직자_로그인_성공() {
            logger.info("===== 구직자_로그인_성공 테스트 시작 =====");

            // given
            UserLoginRequestDTO requestDTO = new UserLoginRequestDTO();
            requestDTO.setEmail("test@example.com");
            requestDTO.setPassword("password123");

            User user = new User();
            user.setEmail("test@example.com");
            user.setPassword("encodedPassword");

            when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
            when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null));

            // when
            UserLoginResponseDTO response = authService.login(requestDTO);

            // then
            assertNotNull(response);
            verify(userRepository).findByEmail("test@example.com");
            verify(authenticationManager).authenticate(any(Authentication.class));
        }

        @Test
        @DisplayName("구인자 로그인 성공")
        void 구인자_로그인_성공() {
            logger.info("===== 구인자_로그인_성공 테스트 시작 =====");

            // given
            CompanyUserLoginRequestDTO requestDTO = new CompanyUserLoginRequestDTO();
            requestDTO.setCpEmail("test@example.com");
            requestDTO.setCpPassword("password");

            CompanyUser cpUser = new CompanyUser();
            cpUser.setCpEmail("test@example.com");
            cpUser.setCpPassword("encodedPassword");

            when(companyUserRepository.findByCpEmail("test@example.com"))
                .thenReturn(Optional.of(cpUser));
            when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(cpUser, null));

            // when
            CompanyUserLoginResponseDTO response = authService.cpLogin(requestDTO);

            // then
            assertNotNull(response);
            verify(companyUserRepository).findByCpEmail("test@example.com");
            verify(authenticationManager).authenticate(any(Authentication.class));
        }
    }
}
