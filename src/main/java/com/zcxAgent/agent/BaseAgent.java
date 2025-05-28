package com.zcxAgent.agent;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.zcxAgent.constant.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 智能体基本类
 * 基本参数，基本方法
 */
@Data
@Slf4j
public abstract class BaseAgent {
    // 名字
    private String name;
    // 提示词
    private String systemPrompt;
    private String nextPrompt;
    // 大模型
    private ChatClient chatClient;
    // 对话记忆
    private List<Message> messageList = new ArrayList<>();
    // 状态
    private AgentState agentState = AgentState.IDLE;
    // 最大步数
    private int maxSteps = 10;
    // 当前步数
    private int currentSteps = 0;

    public String run(String userPrompt){
        // 1.基本信息校验
        if (agentState != AgentState.IDLE){
            throw new RuntimeException("Agent is not idle");
        }
        if (userPrompt==null){
            throw new RuntimeException("User prompt is null");
        }
        // 2.修改状态，添加用户输入上下文
        agentState = AgentState.RUNNING;
        messageList.add(new UserMessage(userPrompt));
        List<String> resultList = new ArrayList<>();
        try {
            // 3.执行步骤
            for (int i = 0; i < maxSteps && agentState != AgentState.FINISHED; i++){
                currentSteps=i+1;
                log.info("Executing step {}/{}", currentSteps, maxSteps);
                String stepResult = step();
                String result = String.format("Step %d: %s", currentSteps, stepResult);
                resultList.add(result);
                log.info(result);
            }
            // 4.判断是否完成
            if (currentSteps>=maxSteps){
                agentState = AgentState.FINISHED;
                String result = String.format("Terminated: Reached max steps %s" , maxSteps );
                resultList.add(result);
            }
            return String.join("\n", resultList);
        } catch (Exception e) {
            agentState = AgentState.ERROR;
            log.info("Agent is Error :{}", e.getMessage());
            return "Agent is Error :" + e.getMessage();
        }
    }

    /**
     * 运行代理（流式输出）
     *
     * @param userPrompt 用户提示词
     * @return SseEmitter实例
     */
    public SseEmitter runStream(String userPrompt) {
        // 创建SseEmitter，设置较长的超时时间
        SseEmitter emitter = new SseEmitter(300000L); // 5分钟超时

        // 使用线程异步处理，避免阻塞主线程
        CompletableFuture.runAsync(() -> {
            try {
                if (this.agentState != AgentState.IDLE) {
                    emitter.send("错误：无法从状态运行代理: " + this.agentState);
                    emitter.complete();
                    return;
                }
                if (StringUtil.isBlank(userPrompt)) {
                    emitter.send("错误：不能使用空提示词运行代理");
                    emitter.complete();
                    return;
                }

                // 更改状态
                agentState = AgentState.RUNNING;
                // 记录消息上下文
                messageList.add(new UserMessage(userPrompt));

                try {
                    for (int i = 0; i < maxSteps && agentState != AgentState.FINISHED; i++) {
                        int stepNumber = i + 1;
                        currentSteps = stepNumber;
                        log.info("Executing step " + stepNumber + "/" + maxSteps);

                        // 单步执行
                        String stepResult = step();
                        String result = "步骤 " + stepNumber + ": " + stepResult;

                        // 发送每一步的结果
                        emitter.send(result);
                    }
                    // 检查是否超出步骤限制
                    if (currentSteps >= maxSteps) {
                        agentState = AgentState.FINISHED;
                        emitter.send("执行结束: 达到最大步骤 (" + maxSteps + ")");
                    }
                    // 正常完成
                    emitter.complete();
                } catch (Exception e) {
                    agentState = AgentState.ERROR;
                    log.error("执行智能体失败", e);
                    try {
                        emitter.send("执行错误: " + e.getMessage());
                        emitter.complete();
                    } catch (Exception ex) {
                        emitter.completeWithError(ex);
                    }
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        // 设置超时和完成回调
        emitter.onTimeout(() -> {
            this.agentState = AgentState.ERROR;
            log.warn("SSE connection timed out");
        });

        emitter.onCompletion(() -> {
            if (this.agentState == AgentState.RUNNING) {
                this.agentState = AgentState.FINISHED;
            }
            log.info("SSE connection completed");
        });

        return emitter;
    }


    public abstract String step();
}
