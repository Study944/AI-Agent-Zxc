package com.zcxAgent.tool;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebSearchToolTest {

    @Value("${search-api.api-key}")
    String apiKey;

    @Test
    void search() {
        WebSearchTool webSearchTool = new WebSearchTool(apiKey);
        String result = webSearchTool.search("程序员鱼皮编程导航 codefather.cn");
        System.out.println(result);
    }
}