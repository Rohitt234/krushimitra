package com.krushimitra.controller;

import com.krushimitra.entity.MarketPrice;
import com.krushimitra.repository.MarketPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/market-prices")
@CrossOrigin(origins = "*")
public class MarketPriceController {
    @Autowired
    private MarketPriceRepository marketPriceRepository;
    
    @GetMapping
    public ResponseEntity<List<MarketPrice>> getAllMarketPrices(
            @RequestParam(required = false) String commodityName) {
        if (commodityName != null && !commodityName.isEmpty()) {
            return ResponseEntity.ok(marketPriceRepository.findPricesByCommodity(commodityName));
        }
        return ResponseEntity.ok(marketPriceRepository.findApprovedPricesOrderByDate());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MarketPrice> getMarketPriceById(@PathVariable Long id) {
        Optional<MarketPrice> marketPrice = marketPriceRepository.findById(id);
        return marketPrice.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/commodity/{commodityName}")
    public ResponseEntity<List<MarketPrice>> getPricesByCommodity(@PathVariable String commodityName) {
        return ResponseEntity.ok(marketPriceRepository.findPricesByCommodity(commodityName));
    }
    
    @GetMapping("/state/{state}")
    public ResponseEntity<List<MarketPrice>> getPricesByState(@PathVariable String state) {
        return ResponseEntity.ok(marketPriceRepository.findPricesByState(state));
    }
    
    @GetMapping("/commodities")
    public ResponseEntity<List<String>> getDistinctCommodities() {
        return ResponseEntity.ok(marketPriceRepository.findDistinctCommodityNames());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MarketPrice> createMarketPrice(@RequestBody MarketPrice marketPrice) {
        return ResponseEntity.ok(marketPriceRepository.save(marketPrice));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MarketPrice> updateMarketPrice(@PathVariable Long id, @RequestBody MarketPrice marketPrice) {
        Optional<MarketPrice> existingPrice = marketPriceRepository.findById(id);
        if (existingPrice.isPresent()) {
            marketPrice.setId(id);
            return ResponseEntity.ok(marketPriceRepository.save(marketPrice));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMarketPrice(@PathVariable Long id) {
        Optional<MarketPrice> marketPrice = marketPriceRepository.findById(id);
        if (marketPrice.isPresent()) {
            marketPriceRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Market price deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }
}
