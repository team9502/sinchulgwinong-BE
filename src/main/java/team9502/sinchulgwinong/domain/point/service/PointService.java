package team9502.sinchulgwinong.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.point.CommonPoint;
import team9502.sinchulgwinong.domain.point.entity.Point;
import team9502.sinchulgwinong.domain.point.entity.SavedPoint;
import team9502.sinchulgwinong.domain.point.entity.UsedPoint;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.enums.UpType;
import team9502.sinchulgwinong.domain.point.repository.PointRepository;
import team9502.sinchulgwinong.domain.point.repository.SavedPointRepository;
import team9502.sinchulgwinong.domain.point.repository.UsedPointRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

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
}
