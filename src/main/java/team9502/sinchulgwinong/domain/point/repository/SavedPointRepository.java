package team9502.sinchulgwinong.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.point.entity.SavedPoint;

public interface SavedPointRepository extends JpaRepository<SavedPoint, Long> {
}
