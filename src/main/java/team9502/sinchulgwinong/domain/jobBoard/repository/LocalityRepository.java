package team9502.sinchulgwinong.domain.jobBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team9502.sinchulgwinong.domain.jobBoard.entity.Locality;

import java.util.Set;

public interface LocalityRepository extends JpaRepository<Locality, Long> {

    @Query("SELECT DISTINCT l.regionName FROM Locality l")
    Set<String> findDistinctRegionNames();

    @Query("SELECT DISTINCT l.subRegionName FROM Locality l WHERE l.regionName = :regionName")
    Set<String> findSubRegionNamesByRegionName(@Param("regionName") String regionName);

    @Query("SELECT DISTINCT l.localityName FROM Locality l WHERE l.regionName = :regionName AND l.subRegionName = :subRegionName")
    Set<String> findLocalityNamesByRegionAndSubRegion(
            @Param("regionName") String regionName, @Param("subRegionName") String subRegionName);

    Locality findByRegionNameAndSubRegionNameAndLocalityName(
            String regionName, String subRegionName, String localityName);
}
