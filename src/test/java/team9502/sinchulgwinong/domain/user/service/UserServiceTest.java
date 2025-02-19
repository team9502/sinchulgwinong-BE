package team9502.sinchulgwinong.domain.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import team9502.sinchulgwinong.domain.board.repository.BoardRepository;
import team9502.sinchulgwinong.domain.email.service.EmailVerificationService;
import team9502.sinchulgwinong.domain.oauth.enums.SocialType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.domain.scrap.service.ScrapService;
import team9502.sinchulgwinong.domain.user.dto.request.UserDeleteRequestDTO;
import team9502.sinchulgwinong.domain.user.dto.request.UserPasswordUpdateRequestDTO;
import team9502.sinchulgwinong.domain.user.dto.request.UserProfileUpdateRequestDTO;
import team9502.sinchulgwinong.domain.user.dto.response.UserProfileResponseDTO;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private PointService pointService;

    @Mock
    private ScrapService scrapService;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        // 테스트에서 자주 쓸 User 객체를 생성
        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUsername("테스트유저");
        mockUser.setNickname("테스트닉네임");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPwd");
        mockUser.setPhoneNumber("010-1234-5678");
        mockUser.setLoginType(SocialType.NORMAL);
    }

    @Nested
    @DisplayName("프로필 조회 테스트")
    class GetUserProfileTest {

        @Test
        @DisplayName("프로필 조회 성공")
        void 프로필조회_성공() {
            logger.info("===== 프로필조회_성공 테스트 시작 =====");

            // given
            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

            // when
            UserProfileResponseDTO response = userService.getUserProfile(1L);

            // then
            assertNotNull(response);
            assertEquals("테스트유저", response.getUsername());
            assertEquals("테스트닉네임", response.getNickname());
            verify(userRepository).findById(1L);
        }

        @Test
        @DisplayName("프로필 조회 실패: 존재하지 않는 사용자")
        void 프로필조회_실패_사용자없음() {
            logger.warn("===== 프로필조회_실패_사용자없음 테스트 시작 =====");

            // given
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.getUserProfile(999L);
            });
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("프로필 수정 테스트")
    class UpdateUserProfileTest {

        @Test
        @DisplayName("프로필 수정 성공")
        void 프로필수정_성공() {
            logger.info("===== 프로필수정_성공 테스트 시작 =====");

            // given
            UserProfileUpdateRequestDTO requestDTO = new UserProfileUpdateRequestDTO();
            requestDTO.setUsername("새유저명");
            requestDTO.setNickname("새닉네임");
            requestDTO.setEmail("new@example.com");
            requestDTO.setPhoneNumber("010-9999-8888");

            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
            when(emailVerificationService.isEmailVerified("new@example.com")).thenReturn(true);

            // when
            UserProfileResponseDTO response = userService.updateUserProfile(1L, requestDTO);

            // then
            assertEquals("새유저명", response.getUsername());
            assertEquals("새닉네임", response.getNickname());
            assertEquals("new@example.com", response.getEmail());
            assertEquals("010-9999-8888", response.getPhoneNumber());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("프로필 수정 실패: requestDTO가 null")
        void 프로필수정_실패_null입력() {
            logger.warn("===== 프로필수정_실패_null입력 테스트 시작 =====");

            // given
            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.updateUserProfile(1L, null);
            });
            assertEquals(ErrorCode.INVALID_INPUT, exception.getErrorCode());
        }

        @Test
        @DisplayName("프로필 수정 실패: 사용자 없음")
        void 프로필수정_실패_사용자없음() {
            logger.warn("===== 프로필수정_실패_사용자없음 테스트 시작 =====");

            // given
            when(userRepository.findById(999L)).thenReturn(Optional.empty());
            UserProfileUpdateRequestDTO requestDTO = new UserProfileUpdateRequestDTO();
            requestDTO.setEmail("update@example.com");

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.updateUserProfile(999L, requestDTO);
            });
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("프로필 수정 실패: 이메일 미인증")
        void 프로필수정_실패_이메일_미인증() {
            logger.warn("===== 프로필수정_실패_이메일_미인증 테스트 시작 =====");

            // given
            UserProfileUpdateRequestDTO requestDTO = new UserProfileUpdateRequestDTO();
            requestDTO.setEmail("unverified@example.com");

            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
            when(emailVerificationService.isEmailVerified("unverified@example.com")).thenReturn(false);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.updateUserProfile(1L, requestDTO);
            });
            assertEquals(ErrorCode.EMAIL_NOT_VERIFIED, exception.getErrorCode());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("비밀번호 수정 테스트")
    class UpdateUserPasswordTest {

        @Test
        @DisplayName("비밀번호 수정 성공")
        void 비밀번호수정_성공() {
            logger.info("===== 비밀번호수정_성공 테스트 시작 =====");

            // given
            UserPasswordUpdateRequestDTO requestDTO = new UserPasswordUpdateRequestDTO();
            requestDTO.setCurrentPassword("currentPwd");
            requestDTO.setNewPassword("newPwd");
            requestDTO.setNewPasswordConfirm("newPwd");

            UserDetailsImpl userDetails = mock(UserDetailsImpl.class); // 실제론 로그인된 유저 정보
            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches("currentPwd", mockUser.getPassword())).thenReturn(true);
            when(passwordEncoder.encode("newPwd")).thenReturn("encodedNewPwd");

            // when
            userService.updateUserPassword(1L, requestDTO, userDetails);

            // then
            verify(userRepository).save(any(User.class));
            assertEquals("encodedNewPwd", mockUser.getPassword());
        }

        @Test
        @DisplayName("비밀번호 수정 실패: 사용자 없음")
        void 비밀번호수정_실패_사용자없음() {
            logger.warn("===== 비밀번호수정_실패_사용자없음 테스트 시작 =====");

            // given
            UserPasswordUpdateRequestDTO requestDTO = new UserPasswordUpdateRequestDTO();
            requestDTO.setCurrentPassword("currentPwd");
            requestDTO.setNewPassword("newPwd");
            requestDTO.setNewPasswordConfirm("newPwd");

            UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.updateUserPassword(999L, requestDTO, userDetails);
            });
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("비밀번호 수정 실패: 현재 비밀번호 불일치")
        void 비밀번호수정_실패_현재비밀번호_불일치() {
            logger.warn("===== 비밀번호수정_실패_현재비밀번호_불일치 테스트 시작 =====");

            // given
            UserPasswordUpdateRequestDTO requestDTO = new UserPasswordUpdateRequestDTO();
            requestDTO.setCurrentPassword("wrongPwd");
            requestDTO.setNewPassword("newPwd");
            requestDTO.setNewPasswordConfirm("newPwd");

            UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches("wrongPwd", "encodedPwd")).thenReturn(false);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.updateUserPassword(1L, requestDTO, userDetails);
            });
            assertEquals(ErrorCode.PASSWORD_MISMATCH, exception.getErrorCode());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("비밀번호 수정 실패: 새 비밀번호 불일치")
        void 비밀번호수정_실패_새비밀번호_불일치() {
            logger.warn("===== 비밀번호수정_실패_새비밀번호_불일치 테스트 시작 =====");

            // given
            UserPasswordUpdateRequestDTO requestDTO = new UserPasswordUpdateRequestDTO();
            requestDTO.setCurrentPassword("currentPwd");
            requestDTO.setNewPassword("newPwd");
            requestDTO.setNewPasswordConfirm("mismatchPwd");

            UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches("currentPwd", "encodedPwd")).thenReturn(true);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.updateUserPassword(1L, requestDTO, userDetails);
            });
            assertEquals(ErrorCode.PASSWORD_CONFIRM_MISMATCH, exception.getErrorCode());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("회원 탈퇴(삭제) 테스트")
    class DeleteUserTest {

        @Test
        @DisplayName("회원탈퇴 성공")
        void 회원탈퇴_성공() {
            logger.info("===== 회원탈퇴_성공 테스트 시작 =====");

            // given
            UserDeleteRequestDTO requestDTO = new UserDeleteRequestDTO();
            requestDTO.setPassword("deletePassword");

            mockUser.setPoint(null);

            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches("deletePassword", "encodedPwd")).thenReturn(true);

            // when
            userService.deleteUser(1L, requestDTO);

            // then
            verify(boardRepository).deleteAll(mockUser.getBoards());
            verify(pointService).deletePointData(mockUser.getPoint());
            verify(scrapService).deleteAllScrapsForUser(mockUser);
            verify(userRepository).delete(mockUser);
        }

        @Test
        @DisplayName("회원탈퇴 실패: 사용자 없음")
        void 회원탈퇴_실패_사용자없음() {
            logger.warn("===== 회원탈퇴_실패_사용자없음 테스트 시작 =====");

            // given
            when(userRepository.findById(999L)).thenReturn(Optional.empty());
            UserDeleteRequestDTO requestDTO = new UserDeleteRequestDTO();
            requestDTO.setPassword("anyPassword");

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.deleteUser(999L, requestDTO);
            });
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(userRepository, never()).delete(any(User.class));
        }

        @Test
        @DisplayName("회원탈퇴 실패: 비밀번호 불일치")
        void 회원탈퇴_실패_비밀번호_불일치() {
            logger.warn("===== 회원탈퇴_실패_비밀번호_불일치 테스트 시작 =====");

            // given
            UserDeleteRequestDTO requestDTO = new UserDeleteRequestDTO();
            requestDTO.setPassword("wrongPwd");

            when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches("wrongPwd", "encodedPwd")).thenReturn(false);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                userService.deleteUser(1L, requestDTO);
            });
            assertEquals(ErrorCode.PASSWORD_MISMATCH, exception.getErrorCode());
            verify(userRepository, never()).delete(any(User.class));
        }
    }

}