package com.zcxAgent.app;

import com.zcxAgent.advisor.ForbiddenWordsAdvisor;
import com.zcxAgent.advisor.MyLoggerAdvisor;
import com.zcxAgent.rag.bestpractice.StudyAppCustomAdvisorFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class StudyApp {

    @Resource
    private VectorStore studyVectorStore;
    @Resource
    private Advisor studyRagCloudAdvisor;
/*    @Resource
    private VectorStore pgVectorVectorStore;*/

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "我是你的计算机专业学习助手，专注解决学习、技术、求职难题。\n" +
            "大一学生：\n" +
            "编程基础是否卡点？\n" +
            "算法入门是否吃力？\n" +
            "大二学生：\n" +
            "核心课（如数据结构）是否难掌握？\n" +
            "项目实战经验不足？\n" +
            "大三大四学生：\n" +
            "实习/考研/求职准备是否迷茫？\n" +
            "简历/面试是否需要优化？\n" +
            "告诉我你的年级、具体问题，我会给出针对性建议。";

    /**
     * 初始化ChatClient客户端
     * @param dashscopeChatModel
     */
    public StudyApp(ChatModel dashscopeChatModel){
        InMemoryChatMemory  chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                // 添加Advisors，对话记忆存储
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 添加日志记录
                        new MyLoggerAdvisor(),
                        // 添加敏感词过滤
                        new ForbiddenWordsAdvisor()
                )
                .build();
    }

    /**
     * 执行记忆对话
     * @param question
     * @param chatId
     */
    public  void doChat(String question, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(question)
                // 设置对话记忆存储的ID和检索数量
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
    }
    // 创建学习记录
    record StudyRecord(String title, List<String> suggestions) {}

    /**
     * 结构化输出，输出结构化数据Java类
     * @param question
     * @param chatId
     */
    public StudyRecord doChatWithRecord(String question, String chatId){
        return chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(question)
                // 设置对话记忆存储的ID和检索数量
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(StudyRecord.class);
    }

    /**
     * Rag知识库搜索
     * @param question
     * @param chatId
     */
    public String doChatWithRag(String question, String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(question)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 添加本地RAG检索增强顾问
                //.advisors(new QuestionAnswerAdvisor(studyVectorStore))
                // 自定义增强顾问
                .advisors(StudyAppCustomAdvisorFactory.createStudyAppCustomAdvisor(
                        "大一",studyVectorStore
                ))
                .call()
                .chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }

    /**
     * 云Rag知识库搜索
     * @param question
     * @param chatId
     */
    public String doChatWithCloudRag(String question, String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(question)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(studyRagCloudAdvisor)
                .call()
                .chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }

    /**
     * 流式输出
     * @param message
     * @param chatId
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new QuestionAnswerAdvisor(studyVectorStore))
                .stream()
                .content();
    }


    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .tools(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        return content;
    }

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    public String doChatWithMcp(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        return response.getResult().getOutput().getText();
    }





    /**
     * PgVector向量库搜索
     * @param question
     * @param chatId
     */
/*    public String doChatWithPgVector(String question, String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(question)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new MyLoggerAdvisor())
                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                .call()
                .chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }*/
}
