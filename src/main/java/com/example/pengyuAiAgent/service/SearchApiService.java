package com.example.pengyuAiAgent.service;

import com.example.pengyuAiAgent.config.SearchApiConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * SearchAPI service for web search functionality
 * Handles HTTP requests to SearchAPI and response parsing
 */
@Service
@Slf4j
public class SearchApiService {
    
    private final OkHttpClient client;
    private final SearchApiConfig config;
    private final ObjectMapper objectMapper;
    
    public SearchApiService(SearchApiConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.client = new OkHttpClient.Builder()
                .connectTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
    
    /**
     * Perform a web search using SearchAPI
     * 
     * @param query Search query keywords
     * @return Formatted search results as plain text
     */
    public String search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "错误：搜索关键词不能为空";
        }
        
        try {
            log.info("Performing search for query: {}", query);
            
            // Build request URL
            HttpUrl.Builder urlBuilder = HttpUrl.get(config.getBaseUrl()).newBuilder();
            urlBuilder.addQueryParameter("engine", config.getEngine());
            urlBuilder.addQueryParameter("q", query);
            urlBuilder.addQueryParameter("api_key", config.getApiKey());
            urlBuilder.addQueryParameter("location", config.getLocation());
            urlBuilder.addQueryParameter("hl", config.getLanguage());
            urlBuilder.addQueryParameter("gl", config.getCountry());
            
            // Build request
            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .build();
            
            // Execute request
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("SearchAPI request failed with status: {}", response.code());
                    return String.format("搜索请求失败，状态码：%d", response.code());
                }
                
                String responseBody = response.body().string();
                log.debug("SearchAPI response: {}", responseBody);
                
                // Parse and format response
                return formatSearchResults(responseBody, query);
            }
            
        } catch (IOException e) {
            log.error("Error performing search", e);
            return "搜索时发生错误：" + e.getMessage();
        } catch (Exception e) {
            log.error("Unexpected error during search", e);
            return "搜索时发生意外错误：" + e.getMessage();
        }
    }
    
    /**
     * Format search results into readable text
     * 
     * @param jsonResponse Raw JSON response from SearchAPI
     * @param query Original search query
     * @return Formatted text with search results
     */
    private String formatSearchResults(String jsonResponse, String query) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            StringBuilder result = new StringBuilder();
            
            result.append("搜索结果：").append(query).append("\n\n");
            
            // Extract organic results
            JsonNode organicResults = root.path("organic_results");
            if (organicResults.isArray() && organicResults.size() > 0) {
                int count = 0;
                for (JsonNode resultNode : organicResults) {
                    if (count >= 5) break; // Limit to 5 results
                    
                    count++;
                    String title = resultNode.path("title").asText("");
                    String link = resultNode.path("link").asText("");
                    String snippet = resultNode.path("snippet").asText("");
                    
                    result.append(count).append(". ").append(title).append("\n");
                    result.append("   链接：").append(link).append("\n");
                    if (!snippet.isEmpty()) {
                        result.append("   摘要：").append(snippet).append("\n");
                    }
                    result.append("\n");
                }
                
                if (count == 0) {
                    result.append("未找到相关搜索结果\n");
                }
            } else {
                // Try to extract answer box if no organic results
                JsonNode answerBox = root.path("answer_box");
                if (!answerBox.isMissingNode()) {
                    String answer = answerBox.path("answer").asText("");
                    if (!answer.isEmpty()) {
                        result.append("答案：").append(answer).append("\n");
                    }
                } else {
                    result.append("未找到相关搜索结果\n");
                }
            }
            
            return result.toString();
            
        } catch (Exception e) {
            log.error("Error formatting search results", e);
            return "解析搜索结果时发生错误：" + e.getMessage();
        }
    }
}
