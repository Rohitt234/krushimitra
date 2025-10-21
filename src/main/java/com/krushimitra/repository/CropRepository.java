package com.krushimitra.repository;

import com.krushimitra.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {
    
    List<Crop> findBySeason(String season);
    
    List<Crop> findBySoilType(String soilType);
    
    List<Crop> findByClimate(String climate);
    
    @Query("SELECT c FROM Crop c WHERE c.season = :season AND c.soilType = :soilType")
    List<Crop> findBySeasonAndSoilType(@Param("season") String season, @Param("soilType") String soilType);
    
    @Query("SELECT c FROM Crop c WHERE c.name LIKE %:name% OR c.description LIKE %:description%")
    List<Crop> searchCrops(@Param("name") String name, @Param("description") String description);
    
    List<Crop> findByNameContainingIgnoreCase(String name);
}
