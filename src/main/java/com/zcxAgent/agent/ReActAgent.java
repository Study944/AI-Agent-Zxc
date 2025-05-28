package com.zcxAgent.agent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * ReActAgent
 * 思考think->执行act
 */
@Data
@Slf4j
public abstract class ReActAgent extends BaseAgent{

    /**
     * 思考是否需要调用工具，调用什么工具
     * @return true:需要调用工具
     */
    public abstract boolean think();

    /**
     * 调用工具的方法
     * @return 工具调用结果
     */
    public abstract String act();

    /**
     * 拆分执行步骤 = think->act
     * @return 执行结果
     */
    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            if (!shouldAct){
                return "Thinking complete - no action needed";
            }
            return act();
        } catch (Exception e) {
            return "Error during step: " + e.getMessage();
        }
    }
}
