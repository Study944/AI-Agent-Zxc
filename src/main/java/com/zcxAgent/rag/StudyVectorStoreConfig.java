package com.zcxAgent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化向量数据库和存储文档切片
 */
@Configuration
public class StudyVectorStoreConfig {

    @Resource
    private StudyDocReader studyDocReader;

    @Bean
    public VectorStore studyVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        // 创建向量库
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        simpleVectorStore.doAdd(studyDocReader.loadMarkdown());
        return simpleVectorStore;
    }

}
