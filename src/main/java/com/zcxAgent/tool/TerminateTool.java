package com.zcxAgent.tool;

import org.springframework.ai.tool.annotation.Tool;

public class TerminateTool {
  
    @Tool(description = """  
            当请求得到满足或助手无法继续执行任务时，终止交互。 
            “完成所有任务后，调用此工具结束工作。 
            """)  
    public String doTerminate() {  
        return "任务结束";  
    }  
}
