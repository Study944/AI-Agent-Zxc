package com.zcxAgent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {PgVectorStoreAutoConfiguration.class})
@MapperScan("com.zcxAgent.mapper")
public class ZcxAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZcxAgentApplication.class, args);
    }

}
