package com.zcxAgent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PromptTemplateAppTest {

    @Resource
    private PromptTemplateApp promptTemplateApp;

    @Test
    void doChat() {
        UUID chatId = UUID.fastUUID();
        String question = "你好，我叫卢泽铭，我想更加了解另一半";
        promptTemplateApp.doChat(question, chatId.toString());
    }
}