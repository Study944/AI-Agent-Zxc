package com.zcxAgent.controller;

import com.zcxAgent.agent.ZxcManus;
import com.zcxAgent.app.StudyApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private StudyApp studyApp;
    @Resource
    private ToolCallbackProvider toolCallbackProvider;
    @Resource
    private ToolCallback[] allTools;
    @Resource
    private ChatModel dashscopeChatModel;

    @GetMapping("/study/chat/async")
    public String studyChatAsync(String question, String chatId) {
        String chatWithRag = studyApp.doChatWithRag(question, chatId);
        return chatWithRag;
    }

    @GetMapping(value = "/study/chat/sse",produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> studyChatSSE(String question, String chatId) {
        Flux<String> stringFlux = studyApp.doChatByStream(question, chatId);
        return stringFlux;
    }

    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        ZxcManus yuManus = new ZxcManus(allTools, dashscopeChatModel, toolCallbackProvider);
        return yuManus.runStream(message);
    }

    @GetMapping("/study/chat/serverSentEvent")
    public Flux<ServerSentEvent<String>> studyChatServerSentEvent(String question,String chatId) {
        Flux<ServerSentEvent<String>> map = studyApp.doChatByStream(question, chatId).
                map(chunk -> ServerSentEvent.<String>builder().data(chunk).build());
        return map;
    }

    @GetMapping("/study/chat/sseEmitter")
    public SseEmitter studyChatSseEmitter(String question, String chatId) {
        SseEmitter sseEmitter = new SseEmitter();
        studyApp.doChatByStream(question, chatId)
                .subscribe(chunk->{
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                },
                        sseEmitter::completeWithError,
                        sseEmitter::complete
                );
        return sseEmitter;
    }



}
