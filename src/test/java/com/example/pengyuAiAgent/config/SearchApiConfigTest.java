package com.example.pengyuAiAgent.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SearchApiConfig
 */
@SpringBootTest
class SearchApiConfigTest {
    
    @Autowired
    private SearchApiConfig searchApiConfig;
    
    @Test
    void testConfigurationLoaded() {
        // Verify configuration is loaded
        assertNotNull(searchApiConfig, "SearchApiConfig should not be null");
        assertNotNull(searchApiConfig.getApiKey(), "API key should be configured");
        assertNotNull(searchApiConfig.getBaseUrl(), "Base URL should be configured");
        assertNotNull(searchApiConfig.getEngine(), "Engine should be configured");
        
        System.out.println("SearchAPI Configuration:");
        System.out.println("Base URL: " + searchApiConfig.getBaseUrl());
        System.out.println("Engine: " + searchApiConfig.getEngine());
        System.out.println("Timeout: " + searchApiConfig.getTimeout());
        System.out.println("Location: " + searchApiConfig.getLocation());
        System.out.println("Language: " + searchApiConfig.getLanguage());
        System.out.println("Country: " + searchApiConfig.getCountry());
        System.out.println("API Key: " + (searchApiConfig.getApiKey() != null ? "***configured***" : "NOT SET"));
    }
    
    @Test
    void testDefaultValues() {
        // Test that default values are set
        assertEquals("https://www.searchapi.io/api/v1/search", searchApiConfig.getBaseUrl());
        assertEquals("google", searchApiConfig.getEngine());
        assertEquals(30000, searchApiConfig.getTimeout());
    }
}
