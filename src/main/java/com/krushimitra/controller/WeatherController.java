package com.krushimitra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krushimitra.service.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/current/{city}")
    public ResponseEntity<String> getWeather(@PathVariable String city) {
        String weatherData = weatherService.getWeatherByCity(city);
        return ResponseEntity.ok(weatherData);
    }
}
