package com.zcxAgent.rag.bestpractice;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 多查询拓展
 */
//@Component
public class MyQueryExpander {

    private final ChatClient.Builder  chatClientBuilder;

    public MyQueryExpander(ChatClient.Builder dashscopeChatClientBuilder) {
        this.chatClientBuilder = dashscopeChatClientBuilder;
    }

    public List<Query> expandQuery(Query query) {
        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        return multiQueryExpander.expand(query);
    }
}
