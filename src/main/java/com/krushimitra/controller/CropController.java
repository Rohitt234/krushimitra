package com.krushimitra.controller;

import com.krushimitra.entity.Crop;
import com.krushimitra.service.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "*")
public class CropController {
    @Autowired
    private CropService cropService;
    
    @GetMapping
    public ResponseEntity<List<Crop>> getAllCrops() {
        return ResponseEntity.ok(cropService.getAllCrops());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Crop> getCropById(@PathVariable Long id) {
        Optional<Crop> crop = cropService.getCropById(id);
        return crop.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/season/{season}")
    public ResponseEntity<List<Crop>> getCropsBySeason(@PathVariable String season) {
        return ResponseEntity.ok(cropService.getCropsBySeason(season));
    }
    
    @GetMapping("/soil/{soilType}")
    public ResponseEntity<List<Crop>> getCropsBySoilType(@PathVariable String soilType) {
        return ResponseEntity.ok(cropService.getCropsBySoilType(soilType));
    }
    
    @GetMapping("/climate/{climate}")
    public ResponseEntity<List<Crop>> getCropsByClimate(@PathVariable String climate) {
        return ResponseEntity.ok(cropService.getCropsByClimate(climate));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Crop>> searchCrops(@RequestParam String query) {
        return ResponseEntity.ok(cropService.searchCrops(query));
    }
    
    @GetMapping("/recommendations")
    public ResponseEntity<List<Crop>> getCropRecommendations(
            @RequestParam String season,
            @RequestParam String soilType,
            @RequestParam(required = false) String climate) {
        return ResponseEntity.ok(cropService.getCropRecommendations(season, soilType, climate));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Crop> createCrop(@RequestBody Crop crop) {
        return ResponseEntity.ok(cropService.saveCrop(crop));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Crop> updateCrop(@PathVariable Long id, @RequestBody Crop crop) {
        Optional<Crop> existingCrop = cropService.getCropById(id);
        if (existingCrop.isPresent()) {
            crop.setId(id);
            return ResponseEntity.ok(cropService.saveCrop(crop));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCrop(@PathVariable Long id) {
        Optional<Crop> crop = cropService.getCropById(id);
        if (crop.isPresent()) {
            cropService.deleteCrop(id);
            return ResponseEntity.ok(Map.of("message", "Crop deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }
}
