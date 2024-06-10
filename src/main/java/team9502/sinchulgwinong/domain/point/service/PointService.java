package team9502.sinchulgwinong.domain.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team9502.sinchulgwinong.domain.point.CommonPoint;
import team9502.sinchulgwinong.domain.point.entity.Point;
import team9502.sinchulgwinong.domain.point.entity.SavedPoint;
import team9502.sinchulgwinong.domain.point.enums.SpType;
import team9502.sinchulgwinong.domain.point.repository.PointRepository;
import team9502.sinchulgwinong.domain.point.repository.SavedPointRepository;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final SavedPointRepository savedPointRepository;

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
            case REVIEW, BOARD -> 100;
            case SIGNUP -> 500;
            default -> 0;
        };
    }
}
