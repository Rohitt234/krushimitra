package com.krushimitra.controller;

import com.krushimitra.entity.ProductListing;
import com.krushimitra.entity.User;
import com.krushimitra.repository.ProductListingRepository;
import com.krushimitra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-listings")
@CrossOrigin(origins = "*")
public class ProductListingController {
    
    @Autowired
    private ProductListingRepository productListingRepository;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/public")
    public ResponseEntity<List<ProductListing>> getPublicListings() {
        return ResponseEntity.ok(productListingRepository.findApprovedAndAvailable());
    }
    
    @GetMapping
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<List<ProductListing>> getMyListings(Authentication authentication) {
        User farmer = userService.findByUsername(authentication.getName()).orElse(null);
        if (farmer != null) {
            return ResponseEntity.ok(productListingRepository.findByFarmer(farmer));
        }
        return ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductListing> getListingById(@PathVariable Long id) {
        Optional<ProductListing> listing = productListingRepository.findById(id);
        return listing.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductListing>> getListingsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productListingRepository.findByCategoryAndApproved(category));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ProductListing>> searchListings(@RequestParam String productName) {
        return ResponseEntity.ok(productListingRepository.searchByProductName(productName));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ProductListing> createListing(@RequestBody ProductListing listing, Authentication authentication) {
        User farmer = userService.findByUsername(authentication.getName()).orElse(null);
        if (farmer != null) {
            listing.setFarmer(farmer);
            return ResponseEntity.ok(productListingRepository.save(listing));
        }
        return ResponseEntity.badRequest().build();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ProductListing> updateListing(@PathVariable Long id, @RequestBody ProductListing listing, Authentication authentication) {
        Optional<ProductListing> existingListing = productListingRepository.findById(id);
        if (existingListing.isPresent()) {
            User farmer = userService.findByUsername(authentication.getName()).orElse(null);
            if (farmer != null && existingListing.get().getFarmer().getId().equals(farmer.getId())) {
                listing.setId(id);
                listing.setFarmer(farmer);
                return ResponseEntity.ok(productListingRepository.save(listing));
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<?> deleteListing(@PathVariable Long id, Authentication authentication) {
        Optional<ProductListing> listing = productListingRepository.findById(id);
        if (listing.isPresent()) {
            User farmer = userService.findByUsername(authentication.getName()).orElse(null);
            if (farmer != null && listing.get().getFarmer().getId().equals(farmer.getId())) {
                productListingRepository.deleteById(id);
                return ResponseEntity.ok(Map.of("message", "Product listing deleted successfully"));
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveListing(@PathVariable Long id) {
        Optional<ProductListing> listing = productListingRepository.findById(id);
        if (listing.isPresent()) {
            ProductListing productListing = listing.get();
            productListing.setIsApproved(true);
            productListingRepository.save(productListing);
            return ResponseEntity.ok(Map.of("message", "Product listing approved successfully"));
        }
        return ResponseEntity.notFound().build();
    }
}
