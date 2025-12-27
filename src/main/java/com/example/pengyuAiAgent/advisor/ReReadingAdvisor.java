package com.example.pengyuAiAgent.advisor;

import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;


public class ReReadingAdvisor implements BaseAdvisor {

	private static final String DEFAULT_RE2_ADVISE_TEMPLATE = """
			{re2_input_query}
			Read the question again: {re2_input_query}
			""";

	private final String re2AdviseTemplate;

	private int order = 0;

	public ReReadingAdvisor() {
		this(DEFAULT_RE2_ADVISE_TEMPLATE);
	}

	public ReReadingAdvisor(String re2AdviseTemplate) {
		this.re2AdviseTemplate = re2AdviseTemplate;
	}
	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain){
		return chain.nextAroundCall(this.before(advisedRequest));
	}
	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain){
		return chain.nextAroundStream(this.before(advisedRequest));
	}
	@Override
	public AdvisedRequest before(AdvisedRequest request){
		String originalUserText = request.userText();
		String augmentedUserText = this.re2AdviseTemplate
				.replace("{re2_input_query}", originalUserText);

		return AdvisedRequest.from(request)
				.userText(augmentedUserText)
				.build();
	}
	@Override
	public AdvisedResponse after(AdvisedResponse response){
		return response;
	}
	@Override
	public int getOrder() {
		return this.order;
	}

	public ReReadingAdvisor withOrder(int order) {
		this.order = order;
		return this;
	}
}
