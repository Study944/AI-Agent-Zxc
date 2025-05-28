package com.zcxAgent.rag.bestpractice;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 创建自定义的检索增强顾问
 */
public class StudyAppCustomAdvisorFactory {

    public static RetrievalAugmentationAdvisor createStudyAppCustomAdvisor(String  status, VectorStore vectorStore){
        Filter.Expression expression = new FilterExpressionBuilder()
                .eq("status",status)
                .build();

        VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .filterExpression(expression)
                .vectorStore(vectorStore)
                .similarityThreshold(0.5)
                .topK(3)
                .build();

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
//                .queryAugmenter()
                .build();
    }


}
