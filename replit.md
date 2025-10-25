# Krushi Mitra - Smart Agriculture Support Platform

## Overview
Krushi Mitra (Friend of Farmers) is a comprehensive agricultural support platform that connects farmers with expert advice, government schemes, market prices, weather information, and product listings. The platform is built using Spring Boot backend with a modern vanilla JavaScript frontend.

## Project Purpose
- Provide farmers with easy access to crop guidance and recommendations
- Connect farmers with agricultural experts through Q&A forum
- Deliver real-time weather updates and market price information
- Share information about government agricultural schemes
- Enable farmers to list and sell their products
- Empower farmers with digital tools to improve agricultural productivity

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.12
- **Language**: Java 17
- **Database**: H2 (in-memory) for development
- **Security**: Spring Security with JWT authentication
- **Build Tool**: Maven

### Frontend
- **HTML/CSS/JavaScript**: Vanilla JS for maximum compatibility
- **Styling**: Custom CSS with responsive design
- **API Integration**: RESTful API calls to Spring Boot backend

## Project Architecture

### Backend Structure
```
src/main/java/com/krushimitra/
├── config/              # Configuration classes
│   └── DataInitializer.java
├── controller/          # REST API controllers
│   ├── AuthController.java
│   ├── CropController.java
│   ├── WeatherController.java
│   └── ...
├── entity/              # JPA entities
│   ├── User.java
│   ├── Crop.java
│   └── ...
├── repository/          # Data access layer
├── security/            # Security configuration
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── SecurityConfig.java
└── service/             # Business logic layer
```

### Frontend Structure
```
src/main/resources/static/
├── index.html           # Main landing page
├── admin.html           # Admin dashboard
├── farmer-dashboard.html
├── expert-dashboard.html
├── visitor.html
├── main.js              # JavaScript logic
└── style.css            # Styling
```

## Key Features

### 1. User Management
- Role-based access control (Farmer, Expert, Admin)
- JWT-based authentication
- Secure password hashing with BCrypt

### 2. Crop Recommendations
- Seasonal crop suggestions (Kharif, Rabi, Zaid)
- Soil type compatibility
- Climate-based recommendations
- Growth duration and yield information

### 3. Weather Information
- Real-time weather data via WeatherAPI
- Location-based forecasts
- Agricultural planning insights

### 4. Market Prices
- Current commodity prices
- Regional market information
- Price trends and analytics

### 5. Government Schemes
- Comprehensive scheme database
- Eligibility criteria
- Application procedures
- Contact information

### 6. Q&A Forum
- Farmer questions
- Expert answers
- Rating system
- Category-based organization

### 7. Product Listings
- Farmers can list products for sale
- Contact information for buyers
- Category-based browsing

## Configuration

### Server Settings
- Port: 5000 (configured for Replit)
- Host: 0.0.0.0 (allows external access)
- Context Path: Root (/) for frontend, /api for backend endpoints

### Database
- Development: H2 in-memory database
- Auto-initialization with sample data
- Schema auto-update enabled

### API Integration
- Weather API: WeatherAPI.com
- Dynamic API URL configuration for Replit environment

## Recent Changes
- **2025-10-25**: Initial project setup in Replit
  - Configured Spring Boot to run on port 5000
  - Integrated frontend from separate repository
  - Updated API URLs to use dynamic origin
  - Removed context path conflict between server config and controller mappings
  - Successfully connected frontend and backend
  - Configured deployment for production

## Sample Users
The application comes with pre-initialized sample users:

1. **Admin**: 
   - Username: `admin`
   - Password: `admin123`

2. **Farmer**:
   - Username: `farmer1`
   - Password: `farmer123`

3. **Expert**:
   - Username: `expert1`
   - Password: `expert123`

## Development Workflow

### Building the Project
```bash
./mvnw clean package -DskipTests
```

### Running the Application
The application runs automatically via the configured workflow. It can also be started manually:
```bash
java -jar target/krushi-mitra-backend-1.0.0.jar
```

### Accessing the Application
- Frontend: http://localhost:5000/
- API Endpoints: http://localhost:5000/api/*
- H2 Console: http://localhost:5000/h2-console

## API Endpoints

### Authentication
- POST `/api/auth/login` - User login
- POST `/api/auth/register` - User registration

### Crops
- GET `/api/crops` - Get all crops
- GET `/api/crops/recommend` - Get crop recommendations

### Weather
- GET `/api/weather/current/{city}` - Get current weather

### Market Prices
- GET `/api/market-prices` - Get market prices
- GET `/api/market-prices/search` - Search by commodity

### Government Schemes
- GET `/api/government-schemes` - Get all schemes

### Q&A Forum
- GET `/api/questions` - Get questions
- POST `/api/questions` - Create question
- POST `/api/answers` - Post answer

### Product Listings
- GET `/api/product-listings` - Get all products
- POST `/api/product-listings` - Create listing

## Security
- All passwords are hashed using BCrypt
- JWT tokens for stateless authentication
- CORS enabled for development
- Session management: Stateless

## Future Enhancements
- PostgreSQL database for production
- Image upload for products and user profiles
- Real-time notifications
- Mobile application
- Multi-language support
- SMS alerts for farmers
- Integration with more government APIs
- Analytics dashboard

## User Preferences
- No specific user preferences documented yet

## Notes
- This is a development setup using H2 database
- For production, consider migrating to PostgreSQL
- Weather API key is included for development
- JWT secret should be changed for production deployment
