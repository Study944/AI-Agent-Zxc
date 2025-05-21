package com.zcxAgent.app;

import cn.hutool.core.lang.UUID;
import com.zcxAgent.model.Memory;
import com.zcxAgent.service.MemoryService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudyAppTest {

    @Resource
    private StudyApp studyApp;
    @Resource
    private MemoryService memoryService;

    @Test
    void testMysql(){
        List<Memory> list = memoryService.list();
    }

    @Test
    void doChat() {
        UUID chatId = UUID.randomUUID();
        // 第一轮对话
        String question = "你好，我叫卢泽铭";
        studyApp.doChat(question, chatId.toString());
        // 第二轮对话
        String question2 = "我的同学叫李兆业";
        studyApp.doChat(question2, chatId.toString());
        // 第三轮对话
        String question3 = "我刚刚提到的同学叫什么？";
        studyApp.doChat(question3, chatId.toString());
    }

    @Test
    void doChatWithRecord() {
        UUID chatId = UUID.randomUUID();
        // 第一轮对话
        String question = "你好，我叫卢泽铭，我是大一新生，没学过编程，不知道从哪里开始";
        StudyApp.StudyRecord studyRecord = studyApp.doChatWithRecord(question, chatId.toString());
        assertNotNull(studyRecord);
        System.out.println(studyRecord);
    }


    @Test
    void doChatWithRag() {
        UUID chatId = UUID.randomUUID();
        String question = "你好，我叫卢泽铭，我是大一新生，没学过编程，想快速入门编程";
        String chatWithRag = studyApp.doChatWithRag(question, chatId.toString());
        assertNotNull(chatWithRag);
    }

    @Test
    void doChatWithCloudRag() {
        UUID chatId = UUID.randomUUID();
        String question = "你好，我叫卢泽铭，我是大一新生，没学过编程，想快速入门编程";
        String chatWithRag = studyApp.doChatWithRag(question, chatId.toString());
        assertNotNull(chatWithRag);
    }

    @Test
    void doChatWithPgVector() {
        UUID chatId = UUID.randomUUID();
        String question = "你好，卢泽铭是谁";
        String chatWithRag = studyApp.doChatWithPgVector(question, chatId.toString());
        assertNotNull(chatWithRag);
    }
}