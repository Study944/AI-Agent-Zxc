package com.zcxAgent.tool;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 搜索工具
 */
public class WebSearchTool {

    private static final String SEARCH_URL = "https://www.searchapi.io/api/v1/search";

    private final String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }
    @Tool(name = "web_search",description = "查找信息从百度搜索引擎")
    public String search(@ToolParam(description = "搜索内容") String query) {
        // 构建请求参数
        Map<String, Object> prams = new HashMap<>();
        prams.put("api_key", apiKey);
        prams.put("engine","baidu");
        prams.put("q", query);
        // 发送请求
        try {
            String response = HttpUtil.get(SEARCH_URL, prams);
            // 解析响应,获取前5条结果
            JSONObject jsonObject = JSONUtil.parseObj(response);
            JSONArray organicResults = jsonObject.getJSONArray("related_searches");
            List<Object> objects = organicResults.subList(0, 5);
            // 拼接搜索结果为字符串
            String result = objects.stream().map(obj -> {
                JSONObject tmpJSONObject = (JSONObject) obj;
                return tmpJSONObject.toString();
            }).collect(Collectors.joining(","));
            return result;
        } catch (Exception e) {
            return "搜索百度时出错" + e.getMessage();
        }
    }
}