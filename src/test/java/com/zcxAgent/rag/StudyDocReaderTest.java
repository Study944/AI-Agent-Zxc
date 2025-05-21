package com.zcxAgent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudyDocReaderTest {

    @Resource
    private StudyDocReader studyDocReader;

    @Test
    void loadMarkdown() {
        studyDocReader.loadMarkdown();
    }

    @Resource
    private VectorStore vectorStore;

    /**
     * 测试失败，因为dashscopeEmbeddingModel,ollamaEmbeddingModel本地有两个向量转化模型，所以会报错
     */
    @Test
    void pgvectorTest(){

        List<Document> documents = List.of(
                new Document("卢泽铭是一名东莞理工学院计算机专业的学生", Map.of("meta1", "meta1")),
                new Document("卢泽铭非常热爱计算机编程"),
                new Document("卢泽铭爱吃水果", Map.of("meta2", "meta2")));

        vectorStore.add(documents);

        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query("计算机").topK(5).build());
    }

}