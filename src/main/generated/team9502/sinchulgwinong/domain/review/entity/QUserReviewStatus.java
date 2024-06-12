package team9502.sinchulgwinong.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserReviewStatus is a Querydsl query type for UserReviewStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserReviewStatus extends EntityPathBase<UserReviewStatus> {

    private static final long serialVersionUID = 1916694520L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserReviewStatus userReviewStatus = new QUserReviewStatus("userReviewStatus");

    public final team9502.sinchulgwinong.global.entity.QBaseTimeEntity _super = new team9502.sinchulgwinong.global.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath isPrivate = createBoolean("isPrivate");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QReview review;

    public final NumberPath<Long> urStatusId = createNumber("urStatusId", Long.class);

    public final team9502.sinchulgwinong.domain.user.entity.QUser user;

    public QUserReviewStatus(String variable) {
        this(UserReviewStatus.class, forVariable(variable), INITS);
    }

    public QUserReviewStatus(Path<? extends UserReviewStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserReviewStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserReviewStatus(PathMetadata metadata, PathInits inits) {
        this(UserReviewStatus.class, metadata, inits);
    }

    public QUserReviewStatus(Class<? extends UserReviewStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.user = inits.isInitialized("user") ? new team9502.sinchulgwinong.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

