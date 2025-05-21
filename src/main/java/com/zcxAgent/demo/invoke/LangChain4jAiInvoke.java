package com.zcxAgent.demo.invoke;

import com.zcxAgent.demo.TestApiKey;
import dev.langchain4j.community.model.dashscope.QwenChatModel;

/**
 * 阿里云灵积大模型LangChain4j调用
 */
public class LangChain4jAiInvoke {

    public static void main(String[] args) {
        QwenChatModel chatModel = QwenChatModel.builder().apiKey(TestApiKey.API_KEY)
                .modelName("qwen-plus")
                .build();
        String chat = chatModel.chat("你好，我是卢泽铭");
        System.out.println(chat);
    }
}
