package team9502.sinchulgwinong.domain.companyUser.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyUser is a Querydsl query type for CompanyUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyUser extends EntityPathBase<CompanyUser> {

    private static final long serialVersionUID = -1362447631L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyUser companyUser = new QCompanyUser("companyUser");

    public final team9502.sinchulgwinong.global.entity.QBaseTimeEntity _super = new team9502.sinchulgwinong.global.entity.QBaseTimeEntity(this);

    public final StringPath cpEmail = createString("cpEmail");

    public final StringPath cpName = createString("cpName");

    public final StringPath cpNum = createString("cpNum");

    public final StringPath cpPassword = createString("cpPassword");

    public final StringPath cpPhoneNumber = createString("cpPhoneNumber");

    public final NumberPath<Long> cpUserId = createNumber("cpUserId", Long.class);

    public final StringPath cpUsername = createString("cpUsername");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Integer> employeeCount = createNumber("employeeCount", Integer.class);

    public final DatePath<java.time.LocalDate> foundationDate = createDate("foundationDate", java.time.LocalDate.class);

    public final BooleanPath hiringStatus = createBoolean("hiringStatus");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final team9502.sinchulgwinong.domain.point.entity.QPoint point;

    public QCompanyUser(String variable) {
        this(CompanyUser.class, forVariable(variable), INITS);
    }

    public QCompanyUser(Path<? extends CompanyUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompanyUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompanyUser(PathMetadata metadata, PathInits inits) {
        this(CompanyUser.class, metadata, inits);
    }

    public QCompanyUser(Class<? extends CompanyUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.point = inits.isInitialized("point") ? new team9502.sinchulgwinong.domain.point.entity.QPoint(forProperty("point")) : null;
    }

}

