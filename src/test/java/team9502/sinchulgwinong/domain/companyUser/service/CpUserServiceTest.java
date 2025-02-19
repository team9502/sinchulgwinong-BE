package team9502.sinchulgwinong.domain.companyUser.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserDeleteRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserPasswordUpdateRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.request.CpUserProfileUpdateRequestDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserPageResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.dto.response.CpUserProfileResponseDTO;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.email.service.EmailVerificationService;
import team9502.sinchulgwinong.domain.jobBoard.repository.JobBoardRepository;
import team9502.sinchulgwinong.domain.point.enums.UpType;
import team9502.sinchulgwinong.domain.point.service.PointService;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CpUserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(CpUserServiceTest.class);

    @Mock
    private CompanyUserRepository companyUserRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private PointService pointService;

    @Mock
    private JobBoardRepository jobBoardRepository;

    @InjectMocks
    private CpUserService cpUserService;

    private CompanyUser mockCompanyUser;

    @BeforeEach
    void setUp() {
        mockCompanyUser = CompanyUser.builder()
                                     .cpUserId(1L)
                                     .cpName("테스트회사")
                                     .cpEmail("test@company.com")
                                     .cpPassword("encodedPwd")
                                     .cpPhoneNumber("010-1234-5678")
                                     .cpUsername("테스트유저")
                                     .hiringStatus(true)
                                     .employeeCount(100)
                                     .foundationDate(LocalDate.of(2000, 1, 1))
                                     .description("테스트 기업 설명")
                                     .cpNum("ENCRYPTED_CP_NUM")
                                     .averageRating(4.5f)
                                     .reviewCount(10)
                                     .viewCount(50)
                                     .jobBoards(new ArrayList<>())
                                     .build();
    }

    @Nested
    @DisplayName("프로필 조회 테스트")
    class GetCpUserProfileTest {

        @Test
        @DisplayName("프로필 조회 성공")
        void 프로필조회_성공() {
            logger.info("===== 프로필조회_성공 테스트 시작 =====");

            // given
            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));
            when(encryptionService.decryptCpNum(mockCompanyUser.getCpNum())).thenReturn("복호화된번호");

            // when
            CpUserProfileResponseDTO response = cpUserService.getCpUserProfile(1L);

            // then
            assertNotNull(response);
            assertEquals("테스트회사", response.getCpName());
            assertEquals("test@company.com", response.getCpEmail());
            verify(companyUserRepository).findById(1L);
            // 조회 시 viewCount 증가
            verify(companyUserRepository).save(any(CompanyUser.class));
        }

        @Test
        @DisplayName("프로필 조회 실패: 존재하지 않는 사용자")
        void 프로필조회_실패_사용자없음() {
            logger.warn("===== 프로필조회_실패_사용자없음 테스트 시작 =====");

            // given
            when(companyUserRepository.findById(999L)).thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.getCpUserProfile(999L);
            });
            assertEquals(ErrorCode.COMPANY_USER_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("프로필 수정 테스트")
    class UpdateCpUserProfileTest {

        @Test
        @DisplayName("프로필 수정 성공")
        void 프로필수정_성공() {
            logger.info("===== 프로필수정_성공 테스트 시작 =====");

            // given
            CpUserProfileUpdateRequestDTO requestDTO = new CpUserProfileUpdateRequestDTO();
            requestDTO.setEmployeeCount(200);
            requestDTO.setHiringStatus(false);
            requestDTO.setDescription("새로운 기업 설명");
            requestDTO.setCpEmail("new@company.com");
            requestDTO.setCpPhoneNumber("010-9999-8888");

            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));
            when(emailVerificationService.isEmailVerified("new@company.com")).thenReturn(true);

            // when
            CpUserProfileResponseDTO response = cpUserService.updateCpUserProfile(1L, requestDTO);

            // then
            assertEquals(200, response.getEmployeeCount());
            assertEquals(false, response.getHiringStatus());
            assertEquals("새로운 기업 설명", response.getDescription());
            assertEquals("new@company.com", response.getCpEmail());
            assertEquals("010-9999-8888", response.getCpPhoneNumber());
            verify(companyUserRepository).findById(1L);
        }

        @Test
        @DisplayName("프로필 수정 실패: requestDTO가 null")
        void 프로필수정_실패_null입력() {
            logger.warn("===== 프로필수정_실패_null입력 테스트 시작 =====");

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.updateCpUserProfile(1L, null);
            });
            assertEquals(ErrorCode.INVALID_INPUT, exception.getErrorCode());
        }

        @Test
        @DisplayName("프로필 수정 실패: 사용자 없음")
        void 프로필수정_실패_사용자없음() {
            logger.warn("===== 프로필수정_실패_사용자없음 테스트 시작 =====");

            // given
            when(companyUserRepository.findById(999L)).thenReturn(Optional.empty());
            CpUserProfileUpdateRequestDTO requestDTO = new CpUserProfileUpdateRequestDTO();
            requestDTO.setCpEmail("update@company.com");

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.updateCpUserProfile(999L, requestDTO);
            });
            assertEquals(ErrorCode.COMPANY_USER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("프로필 수정 실패: 이메일 미인증")
        void 프로필수정_실패_이메일_미인증() {
            logger.warn("===== 프로필수정_실패_이메일_미인증 테스트 시작 =====");

            // given
            CpUserProfileUpdateRequestDTO requestDTO = new CpUserProfileUpdateRequestDTO();
            requestDTO.setCpEmail("unverified@company.com");
            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));
            when(emailVerificationService.isEmailVerified("unverified@company.com")).thenReturn(false);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.updateCpUserProfile(1L, requestDTO);
            });
            assertEquals(ErrorCode.EMAIL_NOT_VERIFIED, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("비밀번호 수정 테스트")
    class UpdateCpUserPasswordTest {

        @Test
        @DisplayName("비밀번호 수정 성공")
        void 비밀번호수정_성공() {
            logger.info("===== 비밀번호수정_성공 테스트 시작 =====");

            // given
            CpUserPasswordUpdateRequestDTO requestDTO = new CpUserPasswordUpdateRequestDTO();
            requestDTO.setCurrentPassword("currentPwd");
            requestDTO.setNewPassword("newPwd");
            requestDTO.setNewPasswordConfirm("newPwd");

            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));
            when(passwordEncoder.matches("currentPwd", mockCompanyUser.getCpPassword())).thenReturn(true);
            when(passwordEncoder.encode("newPwd")).thenReturn("encodedNewPwd");

            // when
            cpUserService.updateCpUserPassword(1L, requestDTO);

            // then
            verify(companyUserRepository).save(any(CompanyUser.class));
            assertEquals("encodedNewPwd", mockCompanyUser.getCpPassword());
        }

        @Test
        @DisplayName("비밀번호 수정 실패: 사용자 없음")
        void 비밀번호수정_실패_사용자없음() {
            logger.warn("===== 비밀번호수정_실패_사용자없음 테스트 시작 =====");

            // given
            CpUserPasswordUpdateRequestDTO requestDTO = new CpUserPasswordUpdateRequestDTO();
            requestDTO.setCurrentPassword("currentPwd");
            requestDTO.setNewPassword("newPwd");
            requestDTO.setNewPasswordConfirm("newPwd");

            when(companyUserRepository.findById(999L)).thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.updateCpUserPassword(999L, requestDTO);
            });
            assertEquals(ErrorCode.COMPANY_USER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("비밀번호 수정 실패: 현재 비밀번호 불일치")
        void 비밀번호수정_실패_현재비밀번호_불일치() {
            logger.warn("===== 비밀번호수정_실패_현재비밀번호_불일치 테스트 시작 =====");

            // given
            CpUserPasswordUpdateRequestDTO requestDTO = new CpUserPasswordUpdateRequestDTO();
            requestDTO.setCurrentPassword("wrongPwd");
            requestDTO.setNewPassword("newPwd");
            requestDTO.setNewPasswordConfirm("newPwd");

            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));
            when(passwordEncoder.matches("wrongPwd", "encodedPwd")).thenReturn(false);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.updateCpUserPassword(1L, requestDTO);
            });
            assertEquals(ErrorCode.PASSWORD_MISMATCH, exception.getErrorCode());
        }

        @Test
        @DisplayName("비밀번호 수정 실패: 새 비밀번호 불일치")
        void 비밀번호수정_실패_새비밀번호_불일치() {
            logger.warn("===== 비밀번호수정_실패_새비밀번호_불일치 테스트 시작 =====");

            // given
            CpUserPasswordUpdateRequestDTO requestDTO = new CpUserPasswordUpdateRequestDTO();
            requestDTO.setCurrentPassword("currentPwd");
            requestDTO.setNewPassword("newPwd");
            requestDTO.setNewPasswordConfirm("mismatchPwd");

            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));
            when(passwordEncoder.matches("currentPwd", "encodedPwd")).thenReturn(true);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.updateCpUserPassword(1L, requestDTO);
            });
            assertEquals(ErrorCode.PASSWORD_CONFIRM_MISMATCH, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("전체 구인자 조회 테스트")
    class GetAllCompanyUsersTest {

        @Mock
        private Page<CompanyUser> companyUserPage; // 페이징 처리 목 객체

        @Test
        @DisplayName("전체 구인자 조회 성공")
        void 전체구인자조회_성공() {
            logger.info("===== 전체구인자조회_성공 테스트 시작 =====");

            // given
            List<CompanyUser> mockList = new ArrayList<>();
            mockList.add(mockCompanyUser);

            when(companyUserRepository.findAllWithFilters(anyString(), anyFloat(), anyFloat(), any(Pageable.class)))
                .thenReturn(companyUserPage);

            when(companyUserPage.getContent()).thenReturn(mockList);
            when(companyUserPage.getTotalElements()).thenReturn(1L);
            when(companyUserPage.getNumber()).thenReturn(0);
            when(companyUserPage.getTotalPages()).thenReturn(1);

            // when
            CpUserPageResponseDTO response = cpUserService.getAllCompanyUsers("desc", 1.0f, 5.0f, Pageable.unpaged());

            // then
            assertNotNull(response);
            assertEquals(1, response.getTotalCpUserCount());
            assertEquals(1, response.getTotalPages());
            assertFalse(response.getCpUsers().isEmpty());
            verify(companyUserRepository).findAllWithFilters(anyString(), anyFloat(), anyFloat(), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("배너 사용 포인트 차감 테스트")
    class UsePointsForBannerTest {
        @Test
        @DisplayName("배너 사용 포인트 차감 성공")
        void 배너사용포인트차감_성공() {
            logger.info("===== 배너사용포인트차감_성공 테스트 시작 =====");

            // given
            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));

            // when
            cpUserService.usePointsForBanner(1L);

            // then
            verify(pointService).deductPoints(mockCompanyUser, UpType.BANNER);
        }

        @Test
        @DisplayName("배너 사용 포인트 차감 실패: 사용자 없음")
        void 배너사용포인트차감_실패_사용자없음() {
            logger.warn("===== 배너사용포인트차감_실패_사용자없음 테스트 시작 =====");

            // given
            when(companyUserRepository.findById(999L)).thenReturn(Optional.empty());

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.usePointsForBanner(999L);
            });
            assertEquals(ErrorCode.COMPANY_USER_NOT_FOUND, exception.getErrorCode());
            verify(pointService, never()).deductPoints(any(CompanyUser.class), any(UpType.class));
        }
    }

    @Nested
    @DisplayName("구인자 회원탈퇴(삭제) 테스트")
    class DeleteCpUserTest {

        @Test
        @DisplayName("회원탈퇴 성공")
        void 회원탈퇴_성공() {
            logger.info("===== 회원탈퇴_성공 테스트 시작 =====");

            // given
            CpUserDeleteRequestDTO requestDTO = new CpUserDeleteRequestDTO();
            requestDTO.setPassword("passwordForDelete");
            mockCompanyUser.getJobBoards().clear();

            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));
            when(passwordEncoder.matches("passwordForDelete", "encodedPwd")).thenReturn(true);

            // when
            cpUserService.deleteCpUser(1L, requestDTO);

            // then
            verify(jobBoardRepository).deleteAll(mockCompanyUser.getJobBoards());
            verify(pointService).deletePointData(mockCompanyUser.getPoint());
            verify(companyUserRepository).delete(mockCompanyUser);
        }

        @Test
        @DisplayName("회원탈퇴 실패: 사용자 없음")
        void 회원탈퇴_실패_사용자없음() {
            logger.warn("===== 회원탈퇴_실패_사용자없음 테스트 시작 =====");

            // given
            when(companyUserRepository.findById(999L)).thenReturn(Optional.empty());
            CpUserDeleteRequestDTO requestDTO = new CpUserDeleteRequestDTO();
            requestDTO.setPassword("anyPassword");

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.deleteCpUser(999L, requestDTO);
            });
            assertEquals(ErrorCode.COMPANY_USER_NOT_FOUND, exception.getErrorCode());
            verify(companyUserRepository, never()).delete(any(CompanyUser.class));
        }

        @Test
        @DisplayName("회원탈퇴 실패: 비밀번호 불일치")
        void 회원탈퇴_실패_비밀번호_불일치() {
            logger.warn("===== 회원탈퇴_실패_비밀번호_불일치 테스트 시작 =====");

            // given
            CpUserDeleteRequestDTO requestDTO = new CpUserDeleteRequestDTO();
            requestDTO.setPassword("wrongPassword");

            when(companyUserRepository.findById(1L)).thenReturn(Optional.of(mockCompanyUser));
            when(passwordEncoder.matches("wrongPassword", "encodedPwd")).thenReturn(false);

            // when & then
            ApiException exception = assertThrows(ApiException.class, () -> {
                cpUserService.deleteCpUser(1L, requestDTO);
            });
            assertEquals(ErrorCode.PASSWORD_MISMATCH, exception.getErrorCode());
            verify(companyUserRepository, never()).delete(any(CompanyUser.class));
        }
    }
}