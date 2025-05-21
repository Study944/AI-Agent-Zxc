package com.zcxAgent.app;

import com.zcxAgent.advisor.ForbiddenWordsAdvisor;
import com.zcxAgent.advisor.MyLoggerAdvisor;
import com.zcxAgent.chatmemory.MysqlChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

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
    @Resource
    private VectorStore pgVectorVectorStore;

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
            "告诉我你的年级、具体问题，我会给出针对性建议。\n" +
            "每次对话后都要生成学习结果，标题为{用户名}的学习报告，内容为建议列表，按照下面的模板" +
            "{\n" +
            "  \"title\": \"学习报告\",\n" +
            "  \"suggestions\": [\n" +
            "    \"从基础语法开始：学习变量、数据类型、运算符等。\",\n" +
            "    \"学习编程语言基础：选择Python或JavaScript作为入门。\",\n" +
            "    \"练习编程：通过小项目或代码挑战来巩固基础。\",\n" +
            "    \"寻找学习资源：网上有大量免费的教程和课程。\",\n" +
            "    \"组建学习小组：共同学习可以提高效率。\",\n" +
            "    \"设定目标：设定短期、中期、长期学习目标，比如完成一个小项目。\",\n" +
            "    \"关注编程社区：参与编程论坛或社区，学习和交流。\"\n" +
            "  ]\n" +
            "}";

    /**
     * 初始化ChatClient客户端
     * @param ollamaChatModel
     */
    public StudyApp(ChatModel ollamaChatModel,MysqlChatMemory chatMemory){
        chatClient = ChatClient.builder(ollamaChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                // 添加Advisors，对话记忆存储
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 添加日志记录
                        new MyLoggerAdvisor(),
                        // 重读Advisor
                        //new ReReadingAdvisor(),
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
                .advisors(
                        // 添加本地RAG向量
                        new QuestionAnswerAdvisor(studyVectorStore),
                        new MyLoggerAdvisor()
                        )
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
                .advisors(new MyLoggerAdvisor())
                .advisors(studyRagCloudAdvisor)
                .call()
                .chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }

    /**
     * PgVector向量库搜索
     * @param question
     * @param chatId
     */
    public String doChatWithPgVector(String question, String chatId){
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
    }
}
