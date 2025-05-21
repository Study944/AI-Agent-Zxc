package com.zcxAgent.app;

import com.zcxAgent.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.core.io.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * 实现提示模板PromptTemplate
 */
@Component
@Slf4j
public class PromptTemplateApp {

    private final ChatClient chatClient;

    public PromptTemplateApp(ChatModel ollamaChatModel,
                             @Value("classpath:prompt/systemPrompt.st") Resource systemPromptResource){
        // 加载系统提示模板
        SystemPromptTemplate systemPrompt = new SystemPromptTemplate(systemPromptResource);
        String systemPromptTemplate = systemPrompt.getTemplate();
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(ollamaChatModel)
                .defaultSystem(systemPromptTemplate)
                .defaultAdvisors(
                        new MyLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }

    record LoveRecord(String title, List<String> suggestions){}

    public void doChat(String question, String chatId){
        LoveRecord entity = chatClient
                .prompt()
                .user(question)
                // 设置对话记忆存储的ID和检索数量
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveRecord.class);
        log.info("loveRecord:{}",entity);
    }

}
