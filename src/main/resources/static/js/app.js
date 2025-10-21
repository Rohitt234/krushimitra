// Global variables
let currentUser = null;
let authToken = localStorage.getItem('authToken');

// API Base URL
const API_BASE_URL = 'http://localhost:8080/api';

// DOM Content Loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    loadInitialData();
});

// Initialize Application
function initializeApp() {
    // Check if user is already logged in
    if (authToken) {
        // Validate token and get user info
        validateToken();
    }
    
    // Setup mobile navigation
    setupMobileNav();
}

// Setup Event Listeners
function setupEventListeners() {
    // Login form
    document.getElementById('login-form').addEventListener('submit', handleLogin);
    
    // Register form
    document.getElementById('register-form').addEventListener('submit', handleRegister);
    
    // Modal close events
    window.addEventListener('click', function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    });
    
    // Smooth scrolling for navigation links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// Mobile Navigation
function setupMobileNav() {
    const navToggle = document.getElementById('nav-toggle');
    const navMenu = document.getElementById('nav-menu');
    
    navToggle.addEventListener('click', function() {
        navMenu.classList.toggle('active');
    });
}

// Modal Functions
function showLoginModal() {
    document.getElementById('login-modal').style.display = 'block';
}

function showRegisterModal() {
    document.getElementById('register-modal').style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// Toggle Role Fields in Registration
function toggleRoleFields() {
    const role = document.getElementById('register-role').value;
    const expertFields = document.getElementById('expert-fields');
    const farmerFields = document.getElementById('farmer-fields');
    
    expertFields.style.display = 'none';
    farmerFields.style.display = 'none';
    
    if (role === 'EXPERT') {
        expertFields.style.display = 'block';
    } else if (role === 'FARMER') {
        farmerFields.style.display = 'block';
    }
}

// Authentication Functions
async function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        
        const data = await response.json();
        
        if (response.ok) {
            authToken = data.token;
            currentUser = data.user;
            localStorage.setItem('authToken', authToken);
            localStorage.setItem('user', JSON.stringify(currentUser));
            
            closeModal('login-modal');
            updateUIAfterLogin();
            showMessage('Login successful!', 'success');
        } else {
            showMessage(data.error || 'Login failed', 'error');
        }
    } catch (error) {
        showMessage('Network error. Please try again.', 'error');
    }
}

async function handleRegister(event) {
    event.preventDefault();
    
    const formData = {
        username: document.getElementById('register-username').value,
        email: document.getElementById('register-email').value,
        password: document.getElementById('register-password').value,
        firstName: document.getElementById('register-firstname').value,
        lastName: document.getElementById('register-lastname').value,
        role: document.getElementById('register-role').value,
        phoneNumber: document.getElementById('register-phone').value,
        expertise: document.getElementById('register-expertise').value,
        qualifications: document.getElementById('register-qualifications').value,
        farmSize: document.getElementById('register-farmsize').value,
        primaryCrops: document.getElementById('register-primarycrops').value
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        
        const data = await response.json();
        
        if (response.ok) {
            closeModal('register-modal');
            showMessage('Registration successful! Please login.', 'success');
            // Clear form
            event.target.reset();
        } else {
            showMessage(data.error || 'Registration failed', 'error');
        }
    } catch (error) {
        showMessage('Network error. Please try again.', 'error');
    }
}

function validateToken() {
    // This would typically validate the token with the backend
    // For now, we'll just check if it exists
    if (authToken) {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        if (user.id) {
            currentUser = user;
            updateUIAfterLogin();
        }
    }
}

function logout() {
    authToken = null;
    currentUser = null;
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
    updateUIAfterLogout();
    showMessage('Logged out successfully', 'success');
}

function updateUIAfterLogin() {
    const authButtons = document.querySelector('.auth-buttons');
    authButtons.innerHTML = `
        <span style="color: white; margin-right: 10px;">Welcome, ${currentUser.firstName}!</span>
        <button class="btn btn-outline" onclick="logout()">Logout</button>
    `;
    
    // Update Q&A section
    updateQASection();
}

function updateUIAfterLogout() {
    const authButtons = document.querySelector('.auth-buttons');
    authButtons.innerHTML = `
        <button class="btn btn-outline" onclick="showLoginModal()">Login</button>
        <button class="btn btn-primary" onclick="showRegisterModal()">Register</button>
    `;
    
    // Reset Q&A section
    document.getElementById('qa-container').innerHTML = `
        <div class="qa-login-prompt">
            <p>Please login to access the Q&A forum and ask questions to experts.</p>
            <button onclick="showLoginModal()" class="btn btn-primary">Login</button>
        </div>
    `;
}

// Weather Functions
async function getWeather() {
    const city = document.getElementById('city-input').value;
    if (!city) {
        showMessage('Please enter a city name', 'error');
        return;
    }
    
    const resultDiv = document.getElementById('weather-result');
    resultDiv.innerHTML = '<div class="loading"></div> Loading weather data...';
    
    try {
        const response = await fetch(`${API_BASE_URL}/weather/current/${encodeURIComponent(city)}`);
        const data = await response.json();
        
        if (data.error) {
            resultDiv.innerHTML = `<div class="message error">${data.error}</div>`;
        } else {
            displayWeatherData(data);
        }
    } catch (error) {
        resultDiv.innerHTML = '<div class="message error">Unable to fetch weather data</div>';
    }
}

function displayWeatherData(data) {
    const resultDiv = document.getElementById('weather-result');
    
    resultDiv.innerHTML = `
        <h3>${data.city}, ${data.country || 'India'}</h3>
        <div class="weather-info">
            <div class="weather-item">
                <i class="fas fa-thermometer-half"></i>
                <div>
                    <strong>Temperature</strong><br>
                    ${data.temperature}°C
                </div>
            </div>
            <div class="weather-item">
                <i class="fas fa-tint"></i>
                <div>
                    <strong>Humidity</strong><br>
                    ${data.humidity}%
                </div>
            </div>
            <div class="weather-item">
                <i class="fas fa-wind"></i>
                <div>
                    <strong>Wind Speed</strong><br>
                    ${data.wind_speed} m/s
                </div>
            </div>
            <div class="weather-item">
                <i class="fas fa-cloud"></i>
                <div>
                    <strong>Condition</strong><br>
                    ${data.description}
                </div>
            </div>
        </div>
    `;
}

// Crop Functions
async function getCropRecommendations() {
    const season = document.getElementById('season-select').value;
    const soilType = document.getElementById('soil-select').value;
    const climate = document.getElementById('climate-select').value;
    
    if (!season || !soilType) {
        showMessage('Please select season and soil type', 'error');
        return;
    }
    
    const resultsDiv = document.getElementById('crop-results');
    resultsDiv.innerHTML = '<div class="loading"></div> Loading recommendations...';
    
    try {
        let url = `${API_BASE_URL}/crops/recommendations?season=${encodeURIComponent(season)}&soilType=${encodeURIComponent(soilType)}`;
        if (climate) {
            url += `&climate=${encodeURIComponent(climate)}`;
        }
        
        const response = await fetch(url);
        const crops = await response.json();
        
        if (crops.length === 0) {
            resultsDiv.innerHTML = '<div class="message">No crops found for the selected criteria</div>';
        } else {
            displayCropResults(crops);
        }
    } catch (error) {
        resultsDiv.innerHTML = '<div class="message error">Unable to fetch crop recommendations</div>';
    }
}

function displayCropResults(crops) {
    const resultsDiv = document.getElementById('crop-results');
    
    const cropsHTML = crops.map(crop => `
        <div class="crop-card">
            <h3>${crop.name}</h3>
            <p>${crop.description || 'No description available'}</p>
            <div class="crop-details">
                <div class="crop-detail">
                    <span>Season:</span>
                    <span>${crop.season || 'N/A'}</span>
                </div>
                <div class="crop-detail">
                    <span>Soil Type:</span>
                    <span>${crop.soilType || 'N/A'}</span>
                </div>
                <div class="crop-detail">
                    <span>Climate:</span>
                    <span>${crop.climate || 'N/A'}</span>
                </div>
                <div class="crop-detail">
                    <span>Growth Duration:</span>
                    <span>${crop.growthDuration || 'N/A'}</span>
                </div>
                <div class="crop-detail">
                    <span>Yield per Hectare:</span>
                    <span>${crop.yieldPerHectare || 'N/A'}</span>
                </div>
            </div>
        </div>
    `).join('');
    
    resultsDiv.innerHTML = cropsHTML;
}

// Market Prices Functions
async function searchMarketPrices() {
    const commodity = document.getElementById('commodity-input').value;
    
    const resultsDiv = document.getElementById('market-results');
    resultsDiv.innerHTML = '<div class="loading"></div> Loading market prices...';
    
    try {
        let url = `${API_BASE_URL}/market-prices`;
        if (commodity) {
            url += `?commodityName=${encodeURIComponent(commodity)}`;
        }
        
        const response = await fetch(url);
        const prices = await response.json();
        
        if (prices.length === 0) {
            resultsDiv.innerHTML = '<div class="message">No market prices found</div>';
        } else {
            displayMarketPrices(prices);
        }
    } catch (error) {
        resultsDiv.innerHTML = '<div class="message error">Unable to fetch market prices</div>';
    }
}

function displayMarketPrices(prices) {
    const resultsDiv = document.getElementById('market-results');
    
    const pricesHTML = prices.map(price => `
        <div class="market-item">
            <div>
                <h4>${price.commodityName}</h4>
                <p>${price.marketName || 'N/A'} - ${price.district || 'N/A'}, ${price.state || 'N/A'}</p>
            </div>
            <div class="price-info">
                <div class="price">₹${price.modalPrice || price.maxPrice || 'N/A'}</div>
                <small>per ${price.unit || 'quintal'}</small>
            </div>
            <div>
                <small>Date: ${price.date || 'N/A'}</small>
            </div>
        </div>
    `).join('');
    
    resultsDiv.innerHTML = pricesHTML;
}

// Government Schemes Functions
async function loadGovernmentSchemes() {
    const schemesDiv = document.getElementById('schemes-list');
    
    try {
        const response = await fetch(`${API_BASE_URL}/government-schemes/public`);
        const schemes = await response.json();
        
        if (schemes.length === 0) {
            schemesDiv.innerHTML = '<div class="message">No government schemes available</div>';
        } else {
            displayGovernmentSchemes(schemes);
        }
    } catch (error) {
        schemesDiv.innerHTML = '<div class="message error">Unable to load government schemes</div>';
    }
}

function displayGovernmentSchemes(schemes) {
    const schemesDiv = document.getElementById('schemes-list');
    
    const schemesHTML = schemes.map(scheme => `
        <div class="scheme-card">
            <h3>${scheme.title}</h3>
            <p>${scheme.description}</p>
            <div class="scheme-details">
                <div class="scheme-detail">
                    <span>Category:</span>
                    <span>${scheme.category || 'N/A'}</span>
                </div>
                <div class="scheme-detail">
                    <span>Eligibility:</span>
                    <span>${scheme.eligibility || 'N/A'}</span>
                </div>
                <div class="scheme-detail">
                    <span>Benefits:</span>
                    <span>${scheme.benefits || 'N/A'}</span>
                </div>
                <div class="scheme-detail">
                    <span>Deadline:</span>
                    <span>${scheme.deadline || 'Ongoing'}</span>
                </div>
            </div>
        </div>
    `).join('');
    
    schemesDiv.innerHTML = schemesHTML;
}

// Product Listings Functions
async function loadProductListings() {
    const productsDiv = document.getElementById('products-list');
    
    try {
        const response = await fetch(`${API_BASE_URL}/product-listings/public`);
        const products = await response.json();
        
        if (products.length === 0) {
            productsDiv.innerHTML = '<div class="message">No products available</div>';
        } else {
            displayProductListings(products);
        }
    } catch (error) {
        productsDiv.innerHTML = '<div class="message error">Unable to load product listings</div>';
    }
}

function displayProductListings(products) {
    const productsDiv = document.getElementById('products-list');
    
    const productsHTML = products.map(product => `
        <div class="product-card">
            <div class="product-image">
                <i class="fas fa-seedling"></i>
            </div>
            <div class="product-info">
                <h3>${product.productName}</h3>
                <p>${product.description || 'No description available'}</p>
                <div class="product-details">
                    <div><strong>Quantity:</strong> ${product.quantity} ${product.unit}</div>
                    <div><strong>Price:</strong> ₹${product.price}</div>
                    <div><strong>Location:</strong> ${product.location || 'N/A'}</div>
                </div>
                <div class="contact-info">
                    <div><strong>Contact:</strong> ${product.contactNumber || 'N/A'}</div>
                    <div><strong>Email:</strong> ${product.contactEmail || 'N/A'}</div>
                </div>
            </div>
        </div>
    `).join('');
    
    productsDiv.innerHTML = productsHTML;
}

// Q&A Functions
function updateQASection() {
    if (!currentUser) return;
    
    const qaContainer = document.getElementById('qa-container');
    
    if (currentUser.role === 'FARMER') {
        qaContainer.innerHTML = `
            <div class="qa-farmer-interface">
                <button class="btn btn-primary" onclick="showAskQuestionModal()">Ask a Question</button>
                <div id="my-questions"></div>
            </div>
        `;
        loadMyQuestions();
    } else if (currentUser.role === 'EXPERT') {
        qaContainer.innerHTML = `
            <div class="qa-expert-interface">
                <div id="unanswered-questions"></div>
            </div>
        `;
        loadUnansweredQuestions();
    }
}

// Utility Functions
function showMessage(message, type = 'info') {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;
    
    document.body.appendChild(messageDiv);
    
    setTimeout(() => {
        messageDiv.remove();
    }, 5000);
}

function scrollToSection(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        section.scrollIntoView({ behavior: 'smooth' });
    }
}

// Load Initial Data
function loadInitialData() {
    loadGovernmentSchemes();
    loadProductListings();
}

// API Helper Functions
async function apiCall(url, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json'
        }
    };
    
    if (authToken) {
        defaultOptions.headers['Authorization'] = `Bearer ${authToken}`;
    }
    
    const finalOptions = { ...defaultOptions, ...options };
    
    try {
        const response = await fetch(`${API_BASE_URL}${url}`, finalOptions);
        return await response.json();
    } catch (error) {
        throw new Error('API call failed');
    }
}
