package com.krushimitra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.krushimitra.entity.MarketPrice;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
    
    List<MarketPrice> findByCommodityName(String commodityName);
    
    List<MarketPrice> findByCategory(String category);
    
    List<MarketPrice> findByState(String state);
    
    List<MarketPrice> findByDistrict(String district);
    
    List<MarketPrice> findByIsApproved(Boolean isApproved);
    
    @Query("SELECT m FROM MarketPrice m WHERE m.isApproved = true ORDER BY m.date DESC")
    List<MarketPrice> findApprovedPricesOrderByDate();
    
    @Query("SELECT m FROM MarketPrice m WHERE m.commodityName = :commodityName AND m.isApproved = true ORDER BY m.date DESC")
    List<MarketPrice> findPricesByCommodity(@Param("commodityName") String commodityName);
    
    @Query("SELECT m FROM MarketPrice m WHERE m.state = :state AND m.isApproved = true ORDER BY m.date DESC")
    List<MarketPrice> findPricesByState(@Param("state") String state);
    
    @Query("SELECT DISTINCT m.commodityName FROM MarketPrice m WHERE m.isApproved = true")
    List<String> findDistinctCommodityNames();
}