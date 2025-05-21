
package com.zcxAgent.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.ai.chat.model.MessageAggregator;

/**
 * 自定义日志Advisor，更改日志输出级别为INFO
 */
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

	private static final Logger logger = LoggerFactory.getLogger(MyLoggerAdvisor.class);

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public int getOrder() {
		return 100;
	}

	/**
	 * 前置处理，输出usePrompt
	 * @param request
	 */
	private AdvisedRequest before(AdvisedRequest request) {
		logger.info("request: {}", request.userText());
		return request;
	}

	/**
	 * 后置处理，输出assistPrompt
	 * @param advisedResponse
	 */
	private void observeAfter(AdvisedResponse advisedResponse) {
		logger.info("response: {}", advisedResponse.response().getResult().getOutput().getText());
	}

	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
		// 前置处理
		advisedRequest = before(advisedRequest);
		// 调用下一个拦截器
		AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
		// 后置处理
		observeAfter(advisedResponse);

		return advisedResponse;
	}

	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {

		advisedRequest = before(advisedRequest);

		Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);

		return new MessageAggregator().aggregateAdvisedResponse(advisedResponses, this::observeAfter);
	}

}
