package com.zcxAgent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MultimodalAppTest {

    @Resource
    private MultimodalApp multimodalApp;

    @Test
    void doChat() {
        UUID chatId = UUID.fastUUID();
        multimodalApp.doChat("static/xx.png", chatId.toString());
    }
}