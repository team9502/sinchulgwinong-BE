package team9502.sinchulgwinong.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team9502.sinchulgwinong.domain.category.entity.JobCategory;

import java.util.Set;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {

    @Query("SELECT DISTINCT j.majorCategoryName FROM JobCategory j")
    Set<String> findMajorCategoryName();

    @Query("SELECT DISTINCT j.minorCategoryName FROM JobCategory j WHERE j.majorCategoryName = :majorCategoryName")
    Set<String> findMinorCategoryName(@Param("majorCategoryName") String majorCategoryName);

    JobCategory findByMajorCategoryNameAndMinorCategoryName(String majorCategoryName,String minorCategoryName);

}
