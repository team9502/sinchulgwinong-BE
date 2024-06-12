package team9502.sinchulgwinong.domain.comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = -490280559L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment = new QComment("comment");

    public final team9502.sinchulgwinong.global.entity.QBaseTimeEntity _super = new team9502.sinchulgwinong.global.entity.QBaseTimeEntity(this);

    public final team9502.sinchulgwinong.domain.board.entity.QBoard board;

    public final StringPath commentContent = createString("commentContent");

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final team9502.sinchulgwinong.domain.user.entity.QUser user;

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new team9502.sinchulgwinong.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.user = inits.isInitialized("user") ? new team9502.sinchulgwinong.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

