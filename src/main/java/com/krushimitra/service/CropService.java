package com.krushimitra.service;

import com.krushimitra.entity.Crop;
import com.krushimitra.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CropService {
    
    @Autowired
    private CropRepository cropRepository;
    
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }
    
    public Optional<Crop> getCropById(Long id) {
        return cropRepository.findById(id);
    }
    
    public Crop saveCrop(Crop crop) {
        return cropRepository.save(crop);
    }
    
    public void deleteCrop(Long id) {
        cropRepository.deleteById(id);
    }
    
    public List<Crop> getCropsBySeason(String season) {
        return cropRepository.findBySeason(season);
    }
    
    public List<Crop> getCropsBySoilType(String soilType) {
        return cropRepository.findBySoilType(soilType);
    }
    
    public List<Crop> getCropsByClimate(String climate) {
        return cropRepository.findByClimate(climate);
    }
    
    public List<Crop> getCropsBySeasonAndSoilType(String season, String soilType) {
        return cropRepository.findBySeasonAndSoilType(season, soilType);
    }
    
    public List<Crop> searchCrops(String searchTerm) {
        return cropRepository.searchCrops(searchTerm, searchTerm);
    }
    
    public List<Crop> findByNameContaining(String name) {
        return cropRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Crop> getCropRecommendations(String season, String soilType, String climate) {
        // Simple recommendation logic - can be enhanced with ML algorithms
        List<Crop> recommendations = cropRepository.findBySeasonAndSoilType(season, soilType);
        
        // Filter by climate if provided
        if (climate != null && !climate.isEmpty()) {
            recommendations = recommendations.stream()
                    .filter(crop -> climate.equalsIgnoreCase(crop.getClimate()))
                    .toList();
        }
        
        return recommendations;
    }
}
