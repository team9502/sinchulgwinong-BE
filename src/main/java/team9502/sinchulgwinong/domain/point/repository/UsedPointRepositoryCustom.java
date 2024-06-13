package team9502.sinchulgwinong.domain.point.repository;

import team9502.sinchulgwinong.domain.point.entity.UsedPoint;

import java.util.List;

public interface UsedPointRepositoryCustom {

    List<UsedPoint> findUsedPointsWithCursor(Long pointId, Long cursorId, int limit);
}
