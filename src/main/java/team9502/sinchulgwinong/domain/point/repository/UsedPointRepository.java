package team9502.sinchulgwinong.domain.point.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.point.entity.UsedPoint;
import team9502.sinchulgwinong.domain.point.enums.UpType;

import java.util.List;

public interface UsedPointRepository extends JpaRepository<UsedPoint, Long>, UsedPointRepositoryCustom {

    List<UsedPoint> findByPointPointId(Long pointId);

    List<UsedPoint> findTop3ByUpTypeOrderByCreatedAtDesc(UpType upType, Pageable pageable);

    void deleteByPointPointId(Long pointId);
}
