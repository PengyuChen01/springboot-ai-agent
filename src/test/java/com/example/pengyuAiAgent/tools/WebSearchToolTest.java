package com.example.pengyuAiAgent.tools;

import com.example.pengyuAiAgent.service.SearchApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for WebSearchTool
 */
@SpringBootTest
class WebSearchToolTest {
    
    @Autowired
    private WebSearchTool webSearchTool;
    
    @Autowired
    private SearchApiService searchApiService;
    
    @Test
    void testSearchWithValidQuery() {
        // Test basic search functionality
        String query = "relationship communication tips";
        String result = webSearchTool.search(query);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("搜索结果"));
        
        System.out.println("Search result for '" + query + "':");
        System.out.println(result);
    }
    
    @Test
    void testSearchWithEmptyQuery() {
        // Test with empty query
        String result = webSearchTool.search("");
        
        assertNotNull(result);
        assertTrue(result.contains("错误"));
    }
    
    @Test
    void testSearchWithNullQuery() {
        // Test with null query
        String result = webSearchTool.search(null);
        
        assertNotNull(result);
        assertTrue(result.contains("错误"));
    }
    
    @Test
    void testSearchServiceDirectly() {
        // Test SearchApiService directly
        String query = "how to improve relationship";
        String result = searchApiService.search(query);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        System.out.println("Direct service search result:");
        System.out.println(result);
    }
    
    @Test
    void testChineseQuery() {
        // Test with Chinese query
        String query = "如何改善恋爱关系";
        String result = webSearchTool.search(query);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("搜索结果"));
        
        System.out.println("Chinese query result:");
        System.out.println(result);
    }
}
