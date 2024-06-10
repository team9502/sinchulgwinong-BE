package team9502.sinchulgwinong.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.point.entity.Point;
import team9502.sinchulgwinong.domain.point.entity.SavedPoint;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.repository.PointRepository;
import team9502.sinchulgwinong.domain.point.repository.SavedPointRepository;
import team9502.sinchulgwinong.global.exception.ApiException;
import team9502.sinchulgwinong.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final SavedPointRepository savedPointRepository;

    @Transactional
    public void earnPoints(Long pointId, SpType spType) {

        int points = getPointsByType(spType);

        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new ApiException(ErrorCode.POINT_NOT_FOUND));
        point.setPoint(point.getPoint() + points);
        pointRepository.save(point);

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
            case REVIEW, BOARD -> 100;
            case SIGNUP -> 500;
            default -> 0;
        };
    }
}
