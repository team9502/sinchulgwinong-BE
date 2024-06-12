package team9502.sinchulgwinong.domain.point.repository;

import team9502.sinchulgwinong.domain.point.entity.SavedPoint;

import java.util.List;

public interface SavedPointRepositoryCustom {

    List<SavedPoint> findSavedPointsWithCursor(Long pointId, Long cursorId, int limit);
}
