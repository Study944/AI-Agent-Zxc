package com.zcxAgent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudyAppTest {

    @Resource
    private StudyApp studyApp;


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
        System.out.println(chatWithRag);
        assertNotNull(chatWithRag);
    }

    @Test
    void doChatWithCloudRag() {
        UUID chatId = UUID.randomUUID();
        String question = "你好，我叫卢泽铭，我是大一新生，没学过编程，想快速入门编程";
        String chatWithRag = studyApp.doChatWithCloudRag(question, chatId.toString());
        System.out.println(chatWithRag);
        assertNotNull(chatWithRag);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("周末想学一下算法，从网上找几道经典算法题");

        // 测试网页抓取：恋爱案例分析
        testMessage("最近写太多java的bug了，看看编程导航网站（https://codefather.cn）的其他程序员是怎么解决bug的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的程序员图片为文件");

        // 测试终端操作：执行代码
        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的学习记录为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘五一编程学习计划’PDF，包含Java，算法和八股");
    }
    @Test
    public void tt(){
        testMessage("我想要知道我2024-2025学年第二学期的课程，访问https://jw.dgut.edu.cn/frame/homes.html?v=83380174831844204942161");
    }

    @Test
    public void ttpdf(){
        // 测试 PDF 生成
        testMessage("生成一份‘五一编程学习计划’PDF，包含Java基础学习，算法学习和八股，至少包含5点，500字");
    }


    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = studyApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "我五一假期想在东莞理工学院松山湖校区附近学习编程，在5公里内有什么推荐的地点";
        String answer = studyApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp2() {
        String chatId = UUID.randomUUID().toString();
        String message = "我想找一点能让我编码久了之后，放松心情的照片";
        String answer = studyApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }


/*    @Test
    void doChatWithPgVector() {
        UUID chatId = UUID.randomUUID();
        String question = "你好，卢泽铭是谁";
        String chatWithRag = studyApp.doChatWithPgVector(question, chatId.toString());
        assertNotNull(chatWithRag);
    }*/
}