package com.zcxAgent.advisor;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

/**
 * 违禁词拦截器
 */
@Slf4j
public class ForbiddenWordsAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    public AdvisedRequest before(AdvisedRequest advisedRequest) {
        String userText = advisedRequest.userText();
        if (SensitiveWordHelper.contains(userText)) {
            log.info("用户输入的文本包含违禁词：{}", SensitiveWordHelper.findAll(userText));
            throw new IllegalArgumentException("用户输入的文本包含违禁词：" + SensitiveWordHelper.findAll(userText));
        }
        return advisedRequest;
    }


    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 前置处理
        advisedRequest = before(advisedRequest);
        // 调用下一个拦截器
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

        return advisedResponse;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = before(advisedRequest);

        return chain.nextAroundStream(advisedRequest);

    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
