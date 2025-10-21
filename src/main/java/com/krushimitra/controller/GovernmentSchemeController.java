package com.krushimitra.controller;

import com.krushimitra.entity.GovernmentScheme;
import com.krushimitra.repository.GovernmentSchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/government-schemes")
@CrossOrigin(origins = "*")
public class GovernmentSchemeController {
    
    @Autowired
    private GovernmentSchemeRepository governmentSchemeRepository;
    
    @GetMapping("/public")
    public ResponseEntity<List<GovernmentScheme>> getPublicSchemes() {
        return ResponseEntity.ok(governmentSchemeRepository.findActiveAndApprovedSchemes());
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<GovernmentScheme>> getAllSchemes() {
        return ResponseEntity.ok(governmentSchemeRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GovernmentScheme> getSchemeById(@PathVariable Long id) {
        Optional<GovernmentScheme> scheme = governmentSchemeRepository.findById(id);
        return scheme.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<GovernmentScheme>> getSchemesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(governmentSchemeRepository.findByCategoryAndActive(category));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<GovernmentScheme>> searchSchemes(@RequestParam String query) {
        return ResponseEntity.ok(governmentSchemeRepository.searchSchemes(query, query));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GovernmentScheme> createScheme(@RequestBody GovernmentScheme scheme) {
        return ResponseEntity.ok(governmentSchemeRepository.save(scheme));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GovernmentScheme> updateScheme(@PathVariable Long id, @RequestBody GovernmentScheme scheme) {
        Optional<GovernmentScheme> existingScheme = governmentSchemeRepository.findById(id);
        if (existingScheme.isPresent()) {
            scheme.setId(id);
            return ResponseEntity.ok(governmentSchemeRepository.save(scheme));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteScheme(@PathVariable Long id) {
        Optional<GovernmentScheme> scheme = governmentSchemeRepository.findById(id);
        if (scheme.isPresent()) {
            governmentSchemeRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Government scheme deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }
}
