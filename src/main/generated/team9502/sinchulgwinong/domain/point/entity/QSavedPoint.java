package team9502.sinchulgwinong.domain.point.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSavedPoint is a Querydsl query type for SavedPoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSavedPoint extends EntityPathBase<SavedPoint> {

    private static final long serialVersionUID = -123691960L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSavedPoint savedPoint = new QSavedPoint("savedPoint");

    public final team9502.sinchulgwinong.global.entity.QBaseTimeEntity _super = new team9502.sinchulgwinong.global.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QPoint point;

    public final NumberPath<Integer> spAmount = createNumber("spAmount", Integer.class);

    public final NumberPath<Integer> spBalance = createNumber("spBalance", Integer.class);

    public final NumberPath<Long> spId = createNumber("spId", Long.class);

    public final EnumPath<team9502.sinchulgwinong.domain.point.enums.SpType> spType = createEnum("spType", team9502.sinchulgwinong.domain.point.enums.SpType.class);

    public QSavedPoint(String variable) {
        this(SavedPoint.class, forVariable(variable), INITS);
    }

    public QSavedPoint(Path<? extends SavedPoint> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSavedPoint(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSavedPoint(PathMetadata metadata, PathInits inits) {
        this(SavedPoint.class, metadata, inits);
    }

    public QSavedPoint(Class<? extends SavedPoint> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.point = inits.isInitialized("point") ? new QPoint(forProperty("point")) : null;
    }

}

