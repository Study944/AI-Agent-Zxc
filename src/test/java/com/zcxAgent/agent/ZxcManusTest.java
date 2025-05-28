package com.zcxAgent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZxcManusTest {

    @Resource
    private ZxcManus zxcManus;

    @Test
    void run() {
        String userPrompt = """  
                我的另一半居住在东莞理工学院松山湖校区，请帮我找到 5 公里内合适的约会地点，
                并结合一些网络图片，制定一份详细的约会计划，
                并以 PDF 格式输出""";
        String answer = zxcManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }

    @Test
    void run2() {
        String userPrompt = "生成一份‘五一编程学习计划’PDF，包含Java基础学习，算法学习和八股，至少包含5点，500字";
        String answer = zxcManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }

}