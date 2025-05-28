package com.zcxAgent.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文件操作工具
 */
public class FileOperationTool {

     static final String FILE_PATH = System.getProperty("user.dir")+"/tmp";

    @Tool(description = "文件读取工具")
    public String ReadFile(@ToolParam(description = "需要读取的文件名") String fileName){
        String filePath = FILE_PATH+"/files"+"/"+fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (IORuntimeException e) {
            return "文件读取失败"+e.getMessage();
        }
    }

    @Tool(description = "文件写入工具")
    public String WriteFile(
            @ToolParam(description = "需要写入内容的文件名") String fileName,
            @ToolParam(description = "需要写入的内容") String content){
        String fileDir = FILE_PATH + "/files";
        String filePath = fileDir+"/"+fileName;
        try {
            FileUtil.mkdir(fileDir);
            FileUtil.writeUtf8String(content,filePath);
            return "文件写入成功到"+filePath;
        } catch (IORuntimeException e) {
            return "文件写入失败"+e.getMessage();
        }
    }

}
