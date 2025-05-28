package com.zcxAgent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 终端操作工具
 */
public class TerminalOperationTool {

    @Tool(description = "Windows终端操作工具")
    public String executeCommand(@ToolParam(description = "需要执行的Windows终端命令") String command) {
        // 执行命令并获取输出
        StringBuffer output = new StringBuffer();
        try {
            // 构建终端命令
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = processBuilder.start();
            // 同时读取标准输出和错误输出
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                while ((line = errorReader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("命令执行失败，并显示退出代码： ").append(exitCode);
            }
        } catch (Exception e) {
            output.append("执行命令时出错： ").append(e.getMessage());
        }
        return output.toString();
    }

}
