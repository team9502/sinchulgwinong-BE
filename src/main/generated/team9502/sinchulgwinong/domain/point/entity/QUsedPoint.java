package team9502.sinchulgwinong.domain.point.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUsedPoint is a Querydsl query type for UsedPoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUsedPoint extends EntityPathBase<UsedPoint> {

    private static final long serialVersionUID = -1375337740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUsedPoint usedPoint = new QUsedPoint("usedPoint");

    public final team9502.sinchulgwinong.global.entity.QBaseTimeEntity _super = new team9502.sinchulgwinong.global.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QPoint point;

    public final NumberPath<Integer> upAmount = createNumber("upAmount", Integer.class);

    public final NumberPath<Integer> upBalance = createNumber("upBalance", Integer.class);

    public final NumberPath<Long> upId = createNumber("upId", Long.class);

    public final EnumPath<team9502.sinchulgwinong.domain.point.enums.UpType> upType = createEnum("upType", team9502.sinchulgwinong.domain.point.enums.UpType.class);

    public QUsedPoint(String variable) {
        this(UsedPoint.class, forVariable(variable), INITS);
    }

    public QUsedPoint(Path<? extends UsedPoint> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUsedPoint(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUsedPoint(PathMetadata metadata, PathInits inits) {
        this(UsedPoint.class, metadata, inits);
    }

    public QUsedPoint(Class<? extends UsedPoint> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.point = inits.isInitialized("point") ? new QPoint(forProperty("point")) : null;
    }

}

