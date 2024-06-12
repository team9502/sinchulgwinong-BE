package team9502.sinchulgwinong.domain.point.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team9502.sinchulgwinong.domain.point.entity.QSavedPoint;
import team9502.sinchulgwinong.domain.point.entity.SavedPoint;

import java.util.List;

@RequiredArgsConstructor
public class SavedPointRepositoryCustomImpl implements SavedPointRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SavedPoint> findSavedPointsWithCursor(Long pointId, Long cursorId, int limit) {
        QSavedPoint qSavedPoint = QSavedPoint.savedPoint;
        return queryFactory
                .selectFrom(qSavedPoint)
                .where(
                        qSavedPoint.point.pointId.eq(pointId)
                                .and(qSavedPoint.spId.lt(cursorId))
                )
                .orderBy(qSavedPoint.spId.desc())
                .limit(limit)
                .fetch();
    }
}
