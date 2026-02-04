package com.example.pengyuAiAgent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SearchAPI configuration properties
 * Reads configuration from application-local.yml under 'searchapi' prefix
 */
@Configuration
@ConfigurationProperties(prefix = "searchapi")
@Data
public class SearchApiConfig {
    
    /**
     * SearchAPI API key
     */
    private String apiKey;
    
    /**
     * SearchAPI base URL
     */
    private String baseUrl = "https://www.searchapi.io/api/v1/search";
    
    /**
     * Search engine to use (default: google)
     */
    private String engine = "google";
    
    /**
     * Request timeout in milliseconds (default: 30 seconds)
     */
    private Integer timeout = 30000;
    
    /**
     * Default search location
     */
    private String location = "New York,United States";
    
    /**
     * Interface language (default: en)
     */
    private String language = "en";
    
    /**
     * Country code (default: us)
     */
    private String country = "us";
}
