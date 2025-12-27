package com.example.pengyuAiAgent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.document.Document;
import java.util.List;

/**
 *  恋爱大师向量数据库配置 （初始化基于内存的向量数据库 bean）
 */
@Configuration
public class LoveAppVectorStoreConfig {
    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> documentList = loveAppDocumentLoader.loadMarkdowns();
        simpleVectorStore.add(documentList);
        return simpleVectorStore;
    }


}
