package com.zcxAgent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PgVectorTest {

    @Resource
    private VectorStore pgVectorVectorStore;

    @Test
    public void testConnect() {

/*        List<Document> documents = List.of(
                new Document("卢泽铭是一名东莞理工学院计算机专业的学生", Map.of("meta1", "meta1")),
                new Document("卢泽铭非常热爱计算机编程"),
                new Document("卢泽铭爱吃水果", Map.of("meta2", "meta2")));

        pgVectorVectorStore.add(documents);*/

        List<Document> results = this.pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("怎么学计算机").topK(5).build());
        Assertions.assertNotNull(results);
    }

}
