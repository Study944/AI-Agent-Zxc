package com.zcxAgent.rag;

import com.zcxAgent.rag.bestpractice.MyTokenSplitter;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 初始化向量数据库和存储文档切片
 */
@Configuration
public class StudyVectorStoreConfig {

    @Resource
    private StudyDocReader studyDocReader;
    @Resource
    private MyKeyWordEnricher myKeyWordEnricher;
    @Resource
    private MyTokenSplitter myTokenSplitter;


    @Bean
    public VectorStore studyVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        // 创建向量库
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> documentList = studyDocReader.loadMarkdown();
        // 文档元数据加词
        //List<Document> enrichDocuments = myKeyWordEnricher.enrichDocuments(documentList);
        // 文档二次切片
        //List<Document> documentList1 = myTokenSplitter.splitCustomized(documentList);

        simpleVectorStore.doAdd(documentList);
        return simpleVectorStore;
    }

}
