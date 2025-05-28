package com.zcxAgent.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZxcManus extends ToolCallAgent{


    public ZxcManus(ToolCallback[] allTools, ChatModel  dashscopeChatModel, ToolCallbackProvider toolCallbackProvider) {
        super(allTools);
        // 名字
        setName("ZxcManus");
        // 大模型
        ChatClient chatClient = ChatClient
                .builder(dashscopeChatModel)
                .defaultTools(toolCallbackProvider)
                .build();
        setChatClient(chatClient);
        // System Prompt
        String systemPrompt = """  
                You are ZxcManus, an all-capable AI assistant, aimed at solving any task presented by the user.
                You have various tools at your disposal that you can call upon to efficiently complete complex requests.
                """;
        setSystemPrompt(systemPrompt);
        // Next Prompt
        String nextPrompt ="""  
                Based on user needs, proactively select the most appropriate tool or combination of tools.
                For complex tasks, you can break down the problem and use different tools step by step to solve it.
                After using each tool, clearly explain the execution results and suggest the next steps.
                If you want to stop the interaction at any point, use the `terminate` tool/function call.
                """;
        setNextPrompt(nextPrompt);
    }
}
