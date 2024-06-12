package team9502.sinchulgwinong.domain.scrap.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScrap is a Querydsl query type for Scrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScrap extends EntityPathBase<Scrap> {

    private static final long serialVersionUID = -614516015L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScrap scrap = new QScrap("scrap");

    public final team9502.sinchulgwinong.domain.board.entity.QBoard board;

    public final NumberPath<Long> scrapId = createNumber("scrapId", Long.class);

    public final team9502.sinchulgwinong.domain.user.entity.QUser user;

    public QScrap(String variable) {
        this(Scrap.class, forVariable(variable), INITS);
    }

    public QScrap(Path<? extends Scrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScrap(PathMetadata metadata, PathInits inits) {
        this(Scrap.class, metadata, inits);
    }

    public QScrap(Class<? extends Scrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new team9502.sinchulgwinong.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.user = inits.isInitialized("user") ? new team9502.sinchulgwinong.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

