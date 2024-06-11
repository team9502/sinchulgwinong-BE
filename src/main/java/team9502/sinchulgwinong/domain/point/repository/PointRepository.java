package team9502.sinchulgwinong.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.point.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long> {
}
