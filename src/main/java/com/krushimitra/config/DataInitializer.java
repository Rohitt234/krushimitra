package com.krushimitra.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.krushimitra.entity.Crop;
import com.krushimitra.entity.GovernmentScheme;
import com.krushimitra.entity.MarketPrice;
import com.krushimitra.entity.User;
import com.krushimitra.entity.UserRole;
import com.krushimitra.repository.CropRepository;
import com.krushimitra.repository.GovernmentSchemeRepository;
import com.krushimitra.repository.MarketPriceRepository;
import com.krushimitra.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CropRepository cropRepository;
    
    @Autowired
    private GovernmentSchemeRepository governmentSchemeRepository;
    
    @Autowired
    private MarketPriceRepository marketPriceRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize data only if database is empty
        if (userRepository.count() == 0) {
            initializeUsers();
        }
        if (cropRepository.count() == 0) {
            initializeCrops();
        }
        if (governmentSchemeRepository.count() == 0) {
            initializeGovernmentSchemes();
        }
        if (marketPriceRepository.count() == 0) {
            initializeMarketPrices();
        }
    }
    
    private void initializeUsers() {
        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@krushimitra.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(UserRole.ADMIN);
        admin.setPhoneNumber("9876543210");
        admin.setAddress("Admin Address");
        admin.setCity("Mumbai");
        admin.setState("Maharashtra");
        admin.setPincode("400001");
        userRepository.save(admin);
        
        // Create sample farmer
        User farmer = new User();
        farmer.setUsername("farmer1");
        farmer.setEmail("farmer1@example.com");
        farmer.setPassword(passwordEncoder.encode("farmer123"));
        farmer.setFirstName("Rajesh");
        farmer.setLastName("Patel");
        farmer.setRole(UserRole.FARMER);
        farmer.setPhoneNumber("9876543211");
        farmer.setAddress("Farm Address");
        farmer.setCity("Pune");
        farmer.setState("Maharashtra");
        farmer.setPincode("411001");
        farmer.setFarmSize("5 acres");
        farmer.setPrimaryCrops("Wheat, Rice");
        userRepository.save(farmer);
        
        // Create sample expert
        User expert = new User();
        expert.setUsername("expert1");
        expert.setEmail("expert1@example.com");
        expert.setPassword(passwordEncoder.encode("expert123"));
        expert.setFirstName("Dr. Priya");
        expert.setLastName("Sharma");
        expert.setRole(UserRole.EXPERT);
        expert.setPhoneNumber("9876543212");
        expert.setAddress("Expert Address");
        expert.setCity("Delhi");
        expert.setState("Delhi");
        expert.setPincode("110001");
        expert.setExpertise("Agricultural Science");
        expert.setQualifications("PhD in Agriculture, 10 years experience");
        expert.setRating(4.5);
        expert.setTotalAnswers(25);
        expert.setIsApproved(true);
        userRepository.save(expert);
    }
    
    private void initializeCrops() {
        // Kharif Crops
        Crop rice = new Crop();
        rice.setName("Rice");
        rice.setDescription("Rice is the staple food of India and is grown in almost all states.");
        rice.setSeason("Kharif");
        rice.setSoilType("Clay");
        rice.setClimate("Tropical");
        rice.setWaterRequirement("High");
        rice.setGrowthDuration("120-150 days");
        rice.setYieldPerHectare("4-6 tons");
        rice.setMarketPrice("₹1800-2200 per quintal");
        cropRepository.save(rice);
        
        Crop maize = new Crop();
        maize.setName("Maize");
        maize.setDescription("Maize is a versatile crop used for food, feed, and industrial purposes.");
        maize.setSeason("Kharif");
        maize.setSoilType("Loamy");
        maize.setClimate("Tropical");
        maize.setWaterRequirement("Medium");
        maize.setGrowthDuration("90-120 days");
        maize.setYieldPerHectare("3-4 tons");
        maize.setMarketPrice("₹1400-1800 per quintal");
        cropRepository.save(maize);
        
        // Rabi Crops
        Crop wheat = new Crop();
        wheat.setName("Wheat");
        wheat.setDescription("Wheat is the second most important cereal crop in India.");
        wheat.setSeason("Rabi");
        wheat.setSoilType("Loamy");
        wheat.setClimate("Temperate");
        wheat.setWaterRequirement("Medium");
        wheat.setGrowthDuration("110-130 days");
        wheat.setYieldPerHectare("3-5 tons");
        wheat.setMarketPrice("₹2000-2400 per quintal");
        cropRepository.save(wheat);
        
        Crop mustard = new Crop();
        mustard.setName("Mustard");
        mustard.setDescription("Mustard is an important oilseed crop grown in India.");
        mustard.setSeason("Rabi");
        mustard.setSoilType("Sandy");
        mustard.setClimate("Temperate");
        mustard.setWaterRequirement("Low");
        mustard.setGrowthDuration("90-110 days");
        mustard.setYieldPerHectare("1.5-2 tons");
        mustard.setMarketPrice("₹4500-5500 per quintal");
        cropRepository.save(mustard);
        
        // Zaid Crops
        Crop watermelon = new Crop();
        watermelon.setName("Watermelon");
        watermelon.setDescription("Watermelon is a popular summer fruit crop.");
        watermelon.setSeason("Zaid");
        watermelon.setSoilType("Sandy");
        watermelon.setClimate("Tropical");
        watermelon.setWaterRequirement("High");
        watermelon.setGrowthDuration("80-100 days");
        watermelon.setYieldPerHectare("25-30 tons");
        watermelon.setMarketPrice("₹15-25 per kg");
        cropRepository.save(watermelon);
    }
    
    private void initializeGovernmentSchemes() {
        // PM-KISAN
        GovernmentScheme pmKisan = new GovernmentScheme();
        pmKisan.setTitle("PM-KISAN (Pradhan Mantri Kisan Samman Nidhi)");
        pmKisan.setDescription("Direct income support of ₹6,000 per year to eligible farmer families.");
        pmKisan.setCategory("Income Support");
        pmKisan.setEligibility("Small and marginal farmers with landholding up to 2 hectares");
        pmKisan.setBenefits("₹6,000 per year in three equal installments of ₹2,000 each");
        pmKisan.setApplicationProcess("Apply through Common Service Centers or online portal");
        pmKisan.setDocumentsRequired("Aadhaar card, land records, bank account details");
        pmKisan.setContactInfo("Toll-free: 1800-180-1551");
        pmKisan.setWebsite("https://pmkisan.gov.in");
        pmKisan.setDeadline("Ongoing");
        governmentSchemeRepository.save(pmKisan);
        
        // PMFBY
        GovernmentScheme pmfby = new GovernmentScheme();
        pmfby.setTitle("PMFBY (Pradhan Mantri Fasal Bima Yojana)");
        pmfby.setDescription("Comprehensive crop insurance scheme to protect farmers against natural calamities.");
        pmfby.setCategory("Crop Insurance");
        pmfby.setEligibility("All farmers growing notified crops");
        pmfby.setBenefits("Insurance coverage for crop loss due to natural calamities");
        pmfby.setApplicationProcess("Apply through banks, insurance companies, or online");
        pmfby.setDocumentsRequired("Land records, crop details, bank account");
        pmfby.setContactInfo("Toll-free: 1800-180-1551");
        pmfby.setWebsite("https://pmfby.gov.in");
        pmfby.setDeadline("Ongoing");
        governmentSchemeRepository.save(pmfby);
        
        // Soil Health Card
        GovernmentScheme soilHealth = new GovernmentScheme();
        soilHealth.setTitle("Soil Health Card Scheme");
        soilHealth.setDescription("Free soil testing and recommendations for farmers.");
        soilHealth.setCategory("Soil Management");
        soilHealth.setEligibility("All farmers");
        soilHealth.setBenefits("Free soil testing and fertilizer recommendations");
        soilHealth.setApplicationProcess("Apply at nearest agriculture office");
        soilHealth.setDocumentsRequired("Aadhaar card, land records");
        soilHealth.setContactInfo("Contact local agriculture department");
        soilHealth.setWebsite("https://soilhealth.dac.gov.in");
        soilHealth.setDeadline("Ongoing");
        governmentSchemeRepository.save(soilHealth);
    }
    
    private void initializeMarketPrices() {
        // Rice prices
        MarketPrice ricePrice1 = new MarketPrice();
        ricePrice1.setCommodityName("Rice");
        ricePrice1.setCategory("Cereals");
        ricePrice1.setUnit("quintal");
        ricePrice1.setMinPrice(new BigDecimal("1800"));
        ricePrice1.setMaxPrice(new BigDecimal("2200"));
        ricePrice1.setModalPrice(new BigDecimal("2000"));
        ricePrice1.setMarketName("APMC Mumbai");
        ricePrice1.setState("Maharashtra");
        ricePrice1.setDistrict("Mumbai");
        ricePrice1.setDate("2024-01-15");
        marketPriceRepository.save(ricePrice1);
        
        // Wheat prices
        MarketPrice wheatPrice1 = new MarketPrice();
        wheatPrice1.setCommodityName("Wheat");
        wheatPrice1.setCategory("Cereals");
        wheatPrice1.setUnit("quintal");
        wheatPrice1.setMinPrice(new BigDecimal("2000"));
        wheatPrice1.setMaxPrice(new BigDecimal("2400"));
        wheatPrice1.setModalPrice(new BigDecimal("2200"));
        wheatPrice1.setMarketName("APMC Delhi");
        wheatPrice1.setState("Delhi");
        wheatPrice1.setDistrict("Delhi");
        wheatPrice1.setDate("2024-01-15");
        marketPriceRepository.save(wheatPrice1);
        
        // Mustard prices
        MarketPrice mustardPrice1 = new MarketPrice();
        mustardPrice1.setCommodityName("Mustard");
        mustardPrice1.setCategory("Oilseeds");
        mustardPrice1.setUnit("quintal");
        mustardPrice1.setMinPrice(new BigDecimal("4500"));
        mustardPrice1.setMaxPrice(new BigDecimal("5500"));
        mustardPrice1.setModalPrice(new BigDecimal("5000"));
        mustardPrice1.setMarketName("APMC Jaipur");
        mustardPrice1.setState("Rajasthan");
        mustardPrice1.setDistrict("Jaipur");
        mustardPrice1.setDate("2024-01-15");
        marketPriceRepository.save(mustardPrice1);
        
        // Maize prices
        MarketPrice maizePrice1 = new MarketPrice();
        maizePrice1.setCommodityName("Maize");
        maizePrice1.setCategory("Cereals");
        maizePrice1.setUnit("quintal");
        maizePrice1.setMinPrice(new BigDecimal("1400"));
        maizePrice1.setMaxPrice(new BigDecimal("1800"));
        maizePrice1.setModalPrice(new BigDecimal("1600"));
        maizePrice1.setMarketName("APMC Pune");
        maizePrice1.setState("Maharashtra");
        maizePrice1.setDistrict("Pune");
        maizePrice1.setDate("2024-01-15");
        marketPriceRepository.save(maizePrice1);
    }
}
