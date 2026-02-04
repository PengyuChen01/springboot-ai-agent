package com.example.pengyuAiAgent.tools;

import com.example.pengyuAiAgent.service.SearchApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * Web search tool for AI agents
 * Provides Google search capabilities through SearchAPI
 */
@Component
@Slf4j
public class WebSearchTool {
    
    private final SearchApiService searchApiService;
    
    public WebSearchTool(SearchApiService searchApiService) {
        this.searchApiService = searchApiService;
    }
    
    /**
     * Search the web for information using Google search engine
     * 
     * @param query Search query keywords (e.g., "how to improve relationship communication")
     * @return Formatted search results with titles, links, and snippets
     */
    @Tool(description = "Search the web for information using Google. " +
          "Use this tool when you need to find up-to-date information, " +
          "research topics, or answer questions that require current knowledge. " +
          "Returns top search results with titles, links, and descriptions.")
    public String search(
            @ToolParam(description = "Search query keywords. " +
                      "Be specific and use clear search terms. " +
                      "Example: 'relationship communication tips', 'how to handle breakup'")
            String query) {
        
        log.info("WebSearchTool.search called with query: {}", query);
        
        if (query == null || query.trim().isEmpty()) {
            return "错误：请提供搜索关键词";
        }
        
        try {
            String result = searchApiService.search(query.trim());
            log.debug("Search completed successfully for query: {}", query);
            return result;
        } catch (Exception e) {
            log.error("Error in WebSearchTool.search", e);
            return "搜索时发生错误，请稍后重试";
        }
    }
}
