package team9502.sinchulgwinong.domain.point.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team9502.sinchulgwinong.domain.point.entity.QUsedPoint;
import team9502.sinchulgwinong.domain.point.entity.UsedPoint;

import java.util.List;

@RequiredArgsConstructor
public class UsedPointRepositoryCustomImpl implements UsedPointRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UsedPoint> findUsedPointsWithCursor(Long pointId, Long cursorId, int limit) {
        QUsedPoint qUsedPoint = QUsedPoint.usedPoint;
        return queryFactory
                .selectFrom(qUsedPoint)
                .where(
                        qUsedPoint.point.pointId.eq(pointId)
                                .and(qUsedPoint.upId.gt(cursorId))
                )
                .orderBy(qUsedPoint.upId.asc())
                .limit(limit)
                .fetch();
    }
}
