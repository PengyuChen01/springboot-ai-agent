package com.example.pengyuAiAgent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
@SpringBootTest
class PgVectorVectorStoreConfigTest {
    @Resource
    private VectorStore pgVectorVectorStore;
    @Test
    void pgVectorVectorStore() {
        List<Document> documents = List.of(
                new Document("写这个ai agent项目的人是pengyu", Map.of("meta1", "meta1")),
                new Document("pengyu 写的项目bug特别的少啊"),
                new Document("要不要一起写代码呀.", Map.of("meta2", "meta2")));
        // Add the documents to PGVector
        pgVectorVectorStore.add(documents);

        // Retrieve documents similar to a query
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("谁写的ai agent 项目").topK(3).build());
        Assertions.assertNotNull(results);
    }
}