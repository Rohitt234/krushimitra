package com.krushimitra.repository;

import com.krushimitra.entity.GovernmentScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GovernmentSchemeRepository extends JpaRepository<GovernmentScheme, Long> {
    
    List<GovernmentScheme> findByIsActive(Boolean isActive);
    
    List<GovernmentScheme> findByIsApproved(Boolean isApproved);
    
    List<GovernmentScheme> findByCategory(String category);
    
    @Query("SELECT g FROM GovernmentScheme g WHERE g.isActive = true AND g.isApproved = true ORDER BY g.createdAt DESC")
    List<GovernmentScheme> findActiveAndApprovedSchemes();
    
    @Query("SELECT g FROM GovernmentScheme g WHERE g.title LIKE %:title% OR g.description LIKE %:description% AND g.isActive = true AND g.isApproved = true")
    List<GovernmentScheme> searchSchemes(@Param("title") String title, @Param("description") String description);
    
    @Query("SELECT g FROM GovernmentScheme g WHERE g.category = :category AND g.isActive = true AND g.isApproved = true")
    List<GovernmentScheme> findByCategoryAndActive(@Param("category") String category);
}
