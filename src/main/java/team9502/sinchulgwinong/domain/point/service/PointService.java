package team9502.sinchulgwinong.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.companyUser.entity.CompanyUser;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

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
            case FIRST_BOARD, COMMENT -> 50;
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
        int totalBalance = point.getPoint();

        return new PointSummaryResponseDTO(totalSaved, totalUsed, totalBalance);
    }

    @Transactional(readOnly = true)
    public PagedResponseDTO<SavedPointDetailResponseDTO> getSpDetails(UserDetailsImpl userDetails, Long cursorId, int limit) throws ApiException {

        Long pointId = getPointId(userDetails);

        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }
        List<SavedPoint> savedPoints = savedPointRepository.findSavedPointsWithCursor(pointId, cursorId, limit + 1);
        boolean hasNextPage = savedPoints.size() > limit;

        if (hasNextPage) {
            savedPoints.remove(savedPoints.size() - 1);
        }

        List<SavedPointDetailResponseDTO> dtoList = savedPoints.stream()
                .map(sp -> new SavedPointDetailResponseDTO(
                        sp.getSpId(),
                        sp.getSpType(),
                        sp.getSpAmount(),
                        sp.getCreatedAt().toLocalDate()))
                .collect(Collectors.toList());

        return new PagedResponseDTO<>(dtoList, hasNextPage);
    }

    @Transactional(readOnly = true)
    public PagedResponseDTO<UsedPointDetailResponseDTO> getUpDetails(UserDetailsImpl userDetails, Long cursorId, int limit) throws ApiException {

        Long pointId = getPointId(userDetails);

        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }
        List<UsedPoint> usedPoints = usedPointRepository.findUsedPointsWithCursor(pointId, cursorId, limit + 1);
        boolean hasNextPage = usedPoints.size() > limit;

        if (hasNextPage) {
            usedPoints.remove(usedPoints.size() - 1);
        }

        List<UsedPointDetailResponseDTO> dtoList = usedPoints.stream()
                .map(up -> new UsedPointDetailResponseDTO(
                        up.getUpId(),
                        up.getUpType(),
                        up.getUpAmount(),
                        up.getCreatedAt().toLocalDate()))
                .collect(Collectors.toList());

        return new PagedResponseDTO<>(dtoList, hasNextPage);
    }

    @Transactional(readOnly = true)
    public List<UsedPointDetailResponseDTO> getPublicLatestBannerUsage() {
        List<UsedPoint> usedPoints = usedPointRepository.findTop3ByUpTypeOrderByCreatedAtDesc(UpType.BANNER, PageRequest.of(0, 3));

        return usedPoints.stream()
                .map(up -> new UsedPointDetailResponseDTO(
                        up.getUpId(),
                        up.getUpType(),
                        up.getUpAmount(),
                        up.getCreatedAt().toLocalDate()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePointData(Point point) {

        if (point != null) {
            Long pointId = point.getPointId();
            savedPointRepository.deleteByPointPointId(pointId);
            usedPointRepository.deleteByPointPointId(pointId);
            pointRepository.delete(point);
        }
    }


    /*
        공통 로직을 메서드로 추출
     */

    private Point getUserPoint(UserDetailsImpl userDetails) throws ApiException {

        Object user = userDetails.getUser();

        if (user instanceof User) {
            return ((User) user).getPoint();
        } else if (user instanceof CompanyUser) {
            return ((CompanyUser) user).getPoint();
        } else {
            throw new ApiException(ErrorCode.INVALID_USER_TYPE);
        }
    }

    private Long getPointId(UserDetailsImpl userDetails) throws ApiException {

        Point point = getUserPoint(userDetails);

        if (point == null) {
            throw new ApiException(ErrorCode.POINT_NOT_FOUND);
        }

        return point.getPointId();
    }
}