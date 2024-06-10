package team9502.sinchulgwinong.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.point.entity.UsedPoint;

public interface UsedPointRepository extends JpaRepository<UsedPoint, Long> {
}
