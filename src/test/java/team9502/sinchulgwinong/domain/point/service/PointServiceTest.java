package team9502.sinchulgwinong.domain.point.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import team9502.sinchulgwinong.domain.point.CommonPoint;
import team9502.sinchulgwinong.domain.point.dto.response.PagedResponseDTO;
import team9502.sinchulgwinong.domain.point.dto.response.PointSummaryResponseDTO;
import team9502.sinchulgwinong.domain.point.dto.response.SavedPointDetailResponseDTO;
import team9502.sinchulgwinong.domain.point.dto.response.UsedPointDetailResponseDTO;
import team9502.sinchulgwinong.domain.point.entity.Point;
import team9502.sinchulgwinong.domain.point.entity.SavedPoint;
import team9502.sinchulgwinong.domain.point.entity.UsedPoint;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.enums.UpType;
import team9502.sinchulgwinong.domain.point.repository.PointRepository;
import team9502.sinchulgwinong.domain.point.repository.SavedPointRepository;
import team9502.sinchulgwinong.domain.point.repository.UsedPointRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PointServiceTest.class);

    @Mock
    private PointRepository pointRepository;
    @Mock
    private SavedPointRepository savedPointRepository;
    @Mock
    private UsedPointRepository usedPointRepository;

    @InjectMocks
    private PointService pointService;

    static class DummyCommonPoint implements CommonPoint {
        private Point point;

        @Override
        public Point getPoint() {
            return point;
        }

        @Override
        public void setPoint(Point point) {
            this.point = point;
        }
    }

    private DummyCommonPoint dummyCommonPoint;

    @BeforeEach
    void setUp() {
        dummyCommonPoint = new DummyCommonPoint();
    }

    @Nested
    @DisplayName("earnPoints 메서드 테스트")
    class EarnPointsTest {
        @Test
        @DisplayName("earnPoints: 기존 포인트 없음 (null) → 새 Point 생성")
        void earnPoints_NoExistingPoint() {
            logger.info("===== earnPoints_NoExistingPoint 테스트 시작 =====");
            SpType spType = SpType.REVIEW;
            when(pointRepository.save(any(Point.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(savedPointRepository.save(any(SavedPoint.class))).thenAnswer(invocation -> invocation.getArgument(0));

            pointService.earnPoints(dummyCommonPoint, spType);

            assertNotNull(dummyCommonPoint.getPoint());
            assertEquals(300, dummyCommonPoint.getPoint().getPoint());
            verify(pointRepository).save(any(Point.class));
            verify(savedPointRepository).save(any(SavedPoint.class));
        }

        @Test
        @DisplayName("earnPoints: 기존 포인트 있음 → 누적 포인트 증가")
        void earnPoints_ExistingPoint() {
            logger.info("===== earnPoints_ExistingPoint 테스트 시작 =====");
            SpType spType = SpType.SIGNUP;
            Point existingPoint = new Point(500);
            dummyCommonPoint.setPoint(existingPoint);

            when(pointRepository.save(any(Point.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(savedPointRepository.save(any(SavedPoint.class))).thenAnswer(invocation -> invocation.getArgument(0));

            pointService.earnPoints(dummyCommonPoint, spType);

            assertEquals(800, dummyCommonPoint.getPoint().getPoint());
            verify(pointRepository).save(any(Point.class));
            verify(savedPointRepository).save(any(SavedPoint.class));
        }
    }

    @Nested
    @DisplayName("deductPoints 메서드 테스트")
    class DeductPointsTest {
        @Test
        @DisplayName("deductPoints: 포인트 없음 → POINT_NOT_FOUND 예외")
        void deductPoints_NoPoint() {
            logger.warn("===== deductPoints_NoPoint 테스트 시작 =====");
            dummyCommonPoint.setPoint(null);
            ApiException exception = assertThrows(ApiException.class, () -> pointService.deductPoints(dummyCommonPoint, UpType.BANNER));
            assertEquals(ErrorCode.POINT_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("deductPoints: 잔여 포인트 부족 → INSUFFICIENT_POINTS 예외")
        void deductPoints_Insufficient() {
            logger.warn("===== deductPoints_Insufficient 테스트 시작 =====");
            Point point = new Point(100);
            dummyCommonPoint.setPoint(point);
            ApiException exception = assertThrows(ApiException.class, () -> pointService.deductPoints(dummyCommonPoint, UpType.BANNER));
            assertEquals(ErrorCode.INSUFFICIENT_POINTS, exception.getErrorCode());
        }

        @Test
        @DisplayName("deductPoints: 정상 차감")
        void deductPoints_Success() {
            logger.info("===== deductPoints_Success 테스트 시작 =====");
            Point point = new Point(5000);
            dummyCommonPoint.setPoint(point);
            when(pointRepository.save(any(Point.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(usedPointRepository.save(any(UsedPoint.class))).thenAnswer(invocation -> invocation.getArgument(0));

            pointService.deductPoints(dummyCommonPoint, UpType.BANNER);

            assertEquals(2000, dummyCommonPoint.getPoint().getPoint());
            verify(pointRepository).save(any(Point.class));
            verify(usedPointRepository).save(any(UsedPoint.class));
        }
    }

    @Nested
    @DisplayName("getPointSummary 메서드 테스트")
    class GetPointSummaryTest {
        private UserDetailsImpl userDetails;
        private User mockUser;

        @BeforeEach
        void setUpUser() {
            mockUser = new User();
            mockUser.setUserId(10L);
            Point point = new Point(2000);
            ReflectionTestUtils.setField(point, "pointId", 500L);
            mockUser.setPoint(point);

            userDetails = new UserDetailsImpl(
                mockUser.getEmail(),
                mockUser.getPassword(),
                List.of(),
                "ROLE_USER",
                mockUser
            );
        }

        @Test
        @DisplayName("getPointSummary: 정상 요약 반환")
        void getPointSummary_Success() {
            logger.info("===== getPointSummary_Success 테스트 시작 =====");
            SavedPoint sp1 = SavedPoint.builder().spAmount(300).build();
            ReflectionTestUtils.setField(sp1, "createdAt", LocalDateTime.now());
            SavedPoint sp2 = SavedPoint.builder().spAmount(300).build();
            ReflectionTestUtils.setField(sp2, "createdAt", LocalDateTime.now());
            List<SavedPoint> savedPoints = Arrays.asList(sp1, sp2);
            when(savedPointRepository.findByPointPointId(500L)).thenReturn(savedPoints);

            UsedPoint up1 = UsedPoint.builder().upAmount(100).build();
            ReflectionTestUtils.setField(up1, "createdAt", LocalDateTime.now());
            List<UsedPoint> usedPoints = List.of(up1);
            when(usedPointRepository.findByPointPointId(500L)).thenReturn(usedPoints);

            PointSummaryResponseDTO summary = pointService.getPointSummary(userDetails);

            assertEquals(600, summary.getTotalSaved());
            assertEquals(100, summary.getTotalUsed());
            assertEquals(2000, summary.getTotalBalance());
        }

        @Test
        @DisplayName("getPointSummary: 포인트 없음 → POINT_NOT_FOUND 예외")
        void getPointSummary_NoPoint() {
            logger.warn("===== getPointSummary_NoPoint 테스트 시작 =====");
            mockUser.setPoint(null);
            userDetails = new UserDetailsImpl(
                mockUser.getEmail(),
                mockUser.getPassword(),
                List.of(),
                "ROLE_USER",
                mockUser
            );
            ApiException exception = assertThrows(ApiException.class, () -> pointService.getPointSummary(userDetails));
            assertEquals(ErrorCode.POINT_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("getSpDetails 메서드 테스트")
    class GetSpDetailsTest {
        private UserDetailsImpl userDetails;

        @BeforeEach
        void setUpUser() {
            User mockUser = new User();
            mockUser.setUserId(20L);
            Point point = new Point(1500);
            ReflectionTestUtils.setField(point, "pointId", 600L);
            mockUser.setPoint(point);
            userDetails = new UserDetailsImpl(
                mockUser.getEmail(),
                mockUser.getPassword(),
                List.of(),
                "ROLE_USER",
                mockUser
            );
        }

        @Test
        @DisplayName("getSpDetails: 정상 페이징 조회 (다음 페이지 있음)")
        void getSpDetails_Success_WithNextPage() {
            logger.info("===== getSpDetails_Success_WithNextPage 테스트 시작 =====");
            Long cursorId = null;
            int limit = 2;
            SavedPoint sp1 = SavedPoint.builder().spId(1L).spAmount(300).build();
            ReflectionTestUtils.setField(sp1, "createdAt", LocalDateTime.now());
            SavedPoint sp2 = SavedPoint.builder().spId(2L).spAmount(300).build();
            ReflectionTestUtils.setField(sp2, "createdAt", LocalDateTime.now());
            SavedPoint sp3 = SavedPoint.builder().spId(3L).spAmount(300).build();
            ReflectionTestUtils.setField(sp3, "createdAt", LocalDateTime.now());
            List<SavedPoint> savedPoints = new ArrayList<>(Arrays.asList(sp1, sp2, sp3));
            when(savedPointRepository.findSavedPointsWithCursor(eq(600L), anyLong(), eq(limit + 1)))
                .thenReturn(savedPoints);

            PagedResponseDTO<SavedPointDetailResponseDTO> response = pointService.getSpDetails(userDetails, cursorId, limit);
            assertEquals(2, response.getData().size());
            assertTrue(response.isHasNextPage());
        }
    }

    @Nested
    @DisplayName("getUpDetails 메서드 테스트")
    class GetUpDetailsTest {
        private UserDetailsImpl userDetails;

        @BeforeEach
        void setUpUser() {
            User mockUser = new User();
            mockUser.setUserId(30L);
            Point point = new Point(2000);
            ReflectionTestUtils.setField(point, "pointId", 700L);
            mockUser.setPoint(point);
            userDetails = new UserDetailsImpl(
                mockUser.getEmail(),
                mockUser.getPassword(),
                List.of(),
                "ROLE_USER",
                mockUser
            );
        }

        @Test
        @DisplayName("getUpDetails: 정상 페이징 조회 (다음 페이지 없음)")
        void getUpDetails_Success_NoNextPage() {
            logger.info("===== getUpDetails_Success_NoNextPage 테스트 시작 =====");
            Long cursorId = 100L;
            int limit = 3;
            UsedPoint up1 = UsedPoint.builder().upId(1L).upAmount(100).build();
            ReflectionTestUtils.setField(up1, "createdAt", LocalDateTime.now());
            UsedPoint up2 = UsedPoint.builder().upId(2L).upAmount(100).build();
            ReflectionTestUtils.setField(up2, "createdAt", LocalDateTime.now());
            List<UsedPoint> usedPoints = new ArrayList<>(Arrays.asList(up1, up2));
            when(usedPointRepository.findUsedPointsWithCursor(eq(700L), anyLong(), eq(limit + 1)))
                .thenReturn(usedPoints);

            PagedResponseDTO<UsedPointDetailResponseDTO> response = pointService.getUpDetails(userDetails, cursorId, limit);
            assertEquals(2, response.getData().size());
            assertFalse(response.isHasNextPage());
        }
    }

    @Nested
    @DisplayName("getPublicLatestBannerUsage 메서드 테스트")
    class GetPublicLatestBannerUsageTest {
        @Test
        @DisplayName("getPublicLatestBannerUsage: 정상 조회")
        void getPublicLatestBannerUsage_Success() {
            logger.info("===== getPublicLatestBannerUsage_Success 테스트 시작 =====");
            UsedPoint up1 = UsedPoint.builder().upId(1L).upAmount(3000).build();
            ReflectionTestUtils.setField(up1, "createdAt", LocalDateTime.now());
            UsedPoint up2 = UsedPoint.builder().upId(2L).upAmount(3000).build();
            ReflectionTestUtils.setField(up2, "createdAt", LocalDateTime.now());
            UsedPoint up3 = UsedPoint.builder().upId(3L).upAmount(3000).build();
            ReflectionTestUtils.setField(up3, "createdAt", LocalDateTime.now());
            List<UsedPoint> usedPoints = Arrays.asList(up1, up2, up3);
            when(usedPointRepository.findTop3ByUpTypeOrderByCreatedAtDesc(eq(UpType.BANNER), any(Pageable.class)))
                .thenReturn(usedPoints);

            List<UsedPointDetailResponseDTO> response = pointService.getPublicLatestBannerUsage();
            assertNotNull(response);
            assertEquals(3, response.size());
        }
    }

    @Nested
    @DisplayName("deletePointData 메서드 테스트")
    class DeletePointDataTest {
        @Test
        @DisplayName("deletePointData: 포인트 데이터 삭제 성공")
        void deletePointData_Success() {
            logger.info("===== deletePointData_Success 테스트 시작 =====");
            Point point = new Point(1000);
            ReflectionTestUtils.setField(point, "pointId", 800L);
            doNothing().when(savedPointRepository).deleteByPointPointId(800L);
            doNothing().when(usedPointRepository).deleteByPointPointId(800L);
            doNothing().when(pointRepository).delete(point);

            pointService.deletePointData(point);

            verify(savedPointRepository).deleteByPointPointId(800L);
            verify(usedPointRepository).deleteByPointPointId(800L);
            verify(pointRepository).delete(point);
        }

        @Test
        @DisplayName("deletePointData: null 포인트이면 삭제 없음")
        void deletePointData_NullPoint() {
            logger.info("===== deletePointData_NullPoint 테스트 시작 =====");
            assertDoesNotThrow(() -> pointService.deletePointData(null));
            verify(savedPointRepository, never()).deleteByPointPointId(anyLong());
            verify(usedPointRepository, never()).deleteByPointPointId(anyLong());
            verify(pointRepository, never()).delete(any(Point.class));
        }
    }
}