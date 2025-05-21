package com.zcxAgent.chatmemory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcxAgent.model.Memory;
import com.zcxAgent.service.MemoryService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MysqlChatMemory implements ChatMemory {

    @Resource
    private MemoryService memoryService;

    @Override
    public void add(String conversationId, Message message) {
        String role = message.getMessageType().getValue();
        String content = message.getText();
        Memory memory = Memory.builder()
                .conversation_id(conversationId)
                .role(role)
                .content(content)
                .build();
        memoryService.save(memory);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Memory> memoryList = new ArrayList<>();
        for (Message message : messages) {
            String role = message.getMessageType().getValue();
            String content = message.getText();
            Memory memory = Memory.builder()
                    .conversation_id(conversationId)
                    .role(role)
                    .content(content)
                    .build();
            memoryList.add(memory);
        }
        memoryService.saveBatch(memoryList);
    }

    /**
     * 从数据库中获取对话记录
     * @param conversationId
     * @param lastN
     */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        QueryWrapper<Memory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id", conversationId)
                .orderByAsc("create_time");
        List<Memory> memoryList = memoryService.list(queryWrapper);
        List<Message> messageList = new ArrayList<>();
        for (Memory memory : memoryList) {
            switch (memory.getRole()) {
                case "user":
                    messageList.add(new UserMessage(memory.getContent()));
                    break;
                case "assistant":
                    messageList.add(new AssistantMessage(memory.getContent()));
                    break;
                case "system":
                    messageList.add(new SystemMessage(memory.getContent()));
                    break;
                default:
                    break;
            }
        }
        return messageList.stream()
                .skip(Math.max(0, messageList.size() - lastN))
                .collect(Collectors.toList());
    }

    /**
     * 删除指定会话id的对话记录
     * @param conversationId
     */
    @Override
    public void clear(String conversationId) {
        QueryWrapper<Memory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id", conversationId);
        memoryService.remove(queryWrapper);
    }
}
