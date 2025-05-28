package com.zcxAgent.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 网页爬虫工具
 */
public class WebScrapingTool {

    @Tool(description = "网页爬虫工具")
    public String scrape(@ToolParam(description = "需要爬取的网页地址") String url){
        try {
            Document document = Jsoup.connect(url).get();
            return document.text();
        } catch (Exception e) {
            return url+"爬取失败"+e.getMessage();
        }
    }

}
