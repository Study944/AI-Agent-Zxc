package com.zcxAgent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云百炼知识库云文档检索配置
 */
@Configuration
public class StudyRagCloudAdvisorsConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean
    public Advisor studyRagCloudAdvisor() {
        DashScopeApi dashScopeApi = new DashScopeApi(apiKey);
        DashScopeDocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName("计算机助学助手")
                        .build());
        // RetrievalAugmentationAdvisor 检索增强顾问
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();

    }

}
