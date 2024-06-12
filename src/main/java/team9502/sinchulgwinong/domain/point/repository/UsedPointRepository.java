package team9502.sinchulgwinong.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.point.entity.UsedPoint;

import java.util.List;

public interface UsedPointRepository extends JpaRepository<UsedPoint, Long> {

    List<UsedPoint> findByPointPointId(Long pointId);
}
