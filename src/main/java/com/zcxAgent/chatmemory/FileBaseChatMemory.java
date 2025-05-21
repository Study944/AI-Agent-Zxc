package com.zcxAgent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义对话记录持久化存储
 */
public class FileBaseChatMemory implements ChatMemory {
    // 文件路径
    private final String FILE_PATH;
    // kryo序列化
    private static final Kryo kryo = new Kryo();
    // 初始化kryo
    static {
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }
    public FileBaseChatMemory(String filePath) {
        this.FILE_PATH = filePath;
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> oldMessages = loadConversation(conversationId);
        oldMessages.addAll(messages);
        saveConversation(conversationId, oldMessages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> messages = loadConversation(conversationId);
        return messages.stream()
                .skip(Math.max(0, messages.size() - lastN))
                .collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        File conversationFile = getConversationFile(conversationId);
        if (conversationFile.exists()){
            conversationFile.delete();
        }
    }

     /**
     * 读取对话记录
     * @param conversationId
     */

    public List<Message> loadConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                messages = kryo.readObject(input, ArrayList.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }
     /**
     * 保存对话记录
     * @param conversationId
     * @param messages
     */
    public void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))){
            kryo.writeObject(output, messages);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取对话记录文件
     * @param conversationId
     */
    public File getConversationFile(String conversationId) {
        return new File(FILE_PATH, conversationId+".kryo");
    }
}

