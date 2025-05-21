package com.zcxAgent.app;

import com.zcxAgent.advisor.MyLoggerAdvisor;
import com.zcxAgent.advisor.ReReadingAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Component
@Slf4j
public class MultimodalApp {

    private final ChatClient chatClient;

    public MultimodalApp(ChatModel dashscopeChatModel,
                         @Value("classpath:prompt/systemPrompt.st") Resource systemPromptResource
                         ) {
        // 加载系统提示模板
        SystemPromptTemplate systemPrompt = new SystemPromptTemplate(systemPromptResource);
        String systemPromptTemplate = systemPrompt.getTemplate();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(systemPromptTemplate)
                .defaultAdvisors(
                        new MyLoggerAdvisor(),
                        new ReReadingAdvisor(),
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory())
                )
                .build();
    }

    public void doChat(String picPath, String chatId) {
        String content = chatClient.prompt()
                .user(u -> u.text("Explain what do you see on this picture?")
                        .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource(picPath)))
                .call()
                .content();
        log.info("content:{}", content);
    }
}
