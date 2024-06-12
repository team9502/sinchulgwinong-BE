package team9502.sinchulgwinong.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team9502.sinchulgwinong.domain.point.entity.SavedPoint;

import java.util.List;

public interface SavedPointRepository extends JpaRepository<SavedPoint, Long> {

    List<SavedPoint> findByPointPointId(Long pointId);
}
