package com.krushimitra.repository;

import com.krushimitra.entity.ProductListing;
import com.krushimitra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductListingRepository extends JpaRepository<ProductListing, Long> {
    
    List<ProductListing> findByFarmer(User farmer);
    
    List<ProductListing> findByIsApproved(Boolean isApproved);
    
    List<ProductListing> findByIsAvailable(Boolean isAvailable);
    
    List<ProductListing> findByCategory(String category);
    
    List<ProductListing> findByLocation(String location);
    
    @Query("SELECT p FROM ProductListing p WHERE p.isApproved = true AND p.isAvailable = true")
    List<ProductListing> findApprovedAndAvailable();
    
    @Query("SELECT p FROM ProductListing p WHERE p.productName LIKE %:productName% AND p.isApproved = true")
    List<ProductListing> searchByProductName(@Param("productName") String productName);
    
    @Query("SELECT p FROM ProductListing p WHERE p.category = :category AND p.isApproved = true AND p.isAvailable = true")
    List<ProductListing> findByCategoryAndApproved(@Param("category") String category);
}
