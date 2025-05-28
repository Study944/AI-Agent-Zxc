package com.zcxAgent.tool;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    @Test
    void scrape() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String result = webScrapingTool.scrape("https://www.searchapi.io/docs/baidu");
        System.out.println(result);
    }

    @Test
    void ts() throws Exception {
        Process process = new ProcessBuilder("cmd.exe", "/c","dir").start();
        process.waitFor();
        System.out.println(process.exitValue());
    }
}