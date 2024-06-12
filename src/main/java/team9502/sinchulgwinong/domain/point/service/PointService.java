package team9502.sinchulgwinong.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
import team9502.sinchulgwinong.domain.companyUser.repository.CompanyUserRepository;
import team9502.sinchulgwinong.domain.point.CommonPoint;
import team9502.sinchulgwinong.domain.point.dto.response.PointSummaryResponseDTO;
import team9502.sinchulgwinong.domain.point.dto.response.SavedPointDetailResponseDTO;
import team9502.sinchulgwinong.domain.point.entity.Point;
import team9502.sinchulgwinong.domain.point.entity.SavedPoint;
import team9502.sinchulgwinong.domain.point.entity.UsedPoint;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.enums.UpType;
import team9502.sinchulgwinong.domain.point.repository.PointRepository;
import team9502.sinchulgwinong.domain.point.repository.SavedPointRepository;
import team9502.sinchulgwinong.domain.point.repository.UsedPointRepository;
import team9502.sinchulgwinong.domain.user.entity.User;
import team9502.sinchulgwinong.domain.user.repository.UserRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;
import team9502.sinchulgwinong.global.security.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;
    private final PointRepository pointRepository;
    private final SavedPointRepository savedPointRepository;
    private final UsedPointRepository usedPointRepository;

    @Transactional
    public void earnPoints(CommonPoint commonPoint, SpType spType) {

        int points = getPointsByType(spType);

        Point point = commonPoint.getPoint();
        if (point == null) {
            point = new Point(points);
        } else {
            point.setPoint(point.getPoint() + points);
        }
        pointRepository.save(point);
        commonPoint.setPoint(point);

        SavedPoint savedPoint = SavedPoint.builder()
                .point(point)
                .spAmount(points)
                .spBalance(point.getPoint())
                .spType(spType)
                .build();
        savedPointRepository.save(savedPoint);
    }

    private int getPointsByType(SpType spType) {

        return switch (spType) {
            case REVIEW, SIGNUP, JOBS -> 300;
            case BOARD, CHAT, EVENT -> 100;
            case FIRST_BOARD -> 50;
        };
    }

    @Transactional
    public void deductPoints(CommonPoint commonPoint, UpType upType) {

        int amount = getPointsDeductedByType(upType);

        Point point = commonPoint.getPoint();

        if (point == null) {
            throw new ApiException(ErrorCode.POINT_NOT_FOUND);
        }

        if (point.getPoint() < amount) {
            throw new ApiException(ErrorCode.INSUFFICIENT_POINTS);
        }

        point.setPoint(point.getPoint() - amount);
        pointRepository.save(point);

        UsedPoint usedPoint = UsedPoint.builder()
                .point(point)
                .upAmount(amount)
                .upBalance(point.getPoint())
                .upType(upType)
                .build();
        usedPointRepository.save(usedPoint);
    }


    private int getPointsDeductedByType(UpType upType) {
        return switch (upType) {
            case BANNER -> 3000;
            case TOP -> 1500;
            case REVIEW -> 100;
            case PROFILE -> 50;
        };
    }

    @Transactional(readOnly = true)
    public PointSummaryResponseDTO getPointSummary(UserDetailsImpl userDetails) {

        Object user = userDetails.getUser();
        Point point;

        if (user instanceof User) {
            point = ((User) user).getPoint();
        } else if (user instanceof CompanyUser) {
            point = ((CompanyUser) user).getPoint();
        } else {
            throw new ApiException(ErrorCode.INVALID_USER_TYPE);
        }

        if (point == null) {
            throw new ApiException(ErrorCode.POINT_NOT_FOUND);
        }

        Long pointId = point.getPointId();
        int totalSaved = savedPointRepository.findByPointPointId(pointId)
                .stream()
                .mapToInt(SavedPoint::getSpAmount)
                .sum();
        int totalUsed = usedPointRepository.findByPointPointId(pointId)
                .stream()
                .mapToInt(UsedPoint::getUpAmount)
                .sum();

        return new PointSummaryResponseDTO(totalSaved, totalUsed);
    }

    @Transactional(readOnly = true)
    public List<SavedPointDetailResponseDTO> getSpDetails(UserDetailsImpl userDetails, Long cursorId, int limit) {
        Long pointId = null;

        Object user = userDetails.getUser();
        if (user instanceof User) {
            pointId = ((User) user).getPoint().getPointId();
        } else if (user instanceof CompanyUser) {
            pointId = ((CompanyUser) user).getPoint().getPointId();
        }

        if (pointId == null) {
            throw new ApiException(ErrorCode.POINT_NOT_FOUND);
        }

        // 커서 값이 null이면 최신 데이터부터 시작
        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }

        List<SavedPoint> savedPoints = savedPointRepository.findSavedPointsWithCursor(pointId, cursorId, limit);
        return savedPoints.stream()
                .map(sp -> new SavedPointDetailResponseDTO(sp.getSpType(), sp.getSpAmount(), sp.getCreatedAt().toLocalDate()))
                .collect(Collectors.toList());
    }
}
