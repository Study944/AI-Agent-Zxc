package com.zcxAgent.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 资源下载工具
 */
public class ResourceDownloadTool {

    private static final String DOWNLOAD_DIR = System.getProperty("user.dir")+ "/tmp";

    @Tool(description = "资源下载工具")
    public String downloadResource(
            @ToolParam(description = "需要下载的资源地址") String url,
            @ToolParam(description = "保存的资源名称") String fileName) {
        String fileDir = DOWNLOAD_DIR + "/resource";
        String filePath = fileDir + "/" + fileName;
        try {
            FileUtil.mkdir(fileDir);
            HttpUtil.downloadFile(url, filePath);
            return "下载成功，保存路径为：" + filePath;
        } catch (Exception e) {
            return "下载失败：" + e.getMessage();
        }
    }

}
