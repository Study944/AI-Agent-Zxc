<template>
  <div class="chat-container">
    <div class="chat-header">
      <el-button @click="$router.push('/')" type="primary" plain size="small">
        返回主页
      </el-button>
      <span class="header-title">AI学习助手</span>
      <span class="chat-id">(会话ID: {{ chatId }})</span>
    </div>
    
    <div class="chat-messages" ref="messageContainer">
      <div
        v-for="(message, index) in messages"
        :key="index"
        :class="['message', message.type === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="message-content">
          <div class="avatar">
            {{ message.type === 'user' ? '👤' : '🤖' }}
          </div>
          <div class="text">{{ message.content }}</div>
        </div>
      </div>
    </div>

    <div class="chat-input">
      <div class="input-wrapper">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题..."
          @keyup.enter.exact="sendMessage"
          resize="none"
        />
        <div class="button-wrapper">
          <el-button
            type="primary"
            :disabled="!inputMessage.trim() || loading"
            @click="sendMessage"
            size="large"
            class="send-button"
          >
            {{ loading ? '发送中...' : '发送' }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import axios from 'axios'

const chatId = ref(generateChatId())
const messages = ref([])
const inputMessage = ref('')
const loading = ref(false)
const messageContainer = ref(null)

function generateChatId() {
  return 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
}

async function sendMessage() {
  if (!inputMessage.value.trim() || loading.value) return

  const userMessage = inputMessage.value.trim()
  messages.value.push({ type: 'user', content: userMessage })
  inputMessage.value = ''
  loading.value = true

  await nextTick()
  messageContainer.value.scrollTop = messageContainer.value.scrollHeight

  try {
    const eventSource = new EventSource(
      `http://localhost:8123/api/ai/study/chat/sse?question=${encodeURIComponent(userMessage)}&chatId=${chatId.value}`
    )

    let aiResponse = ''
    eventSource.onmessage = (event) => {
      const data = event.data
      if (data === '[DONE]') {
        eventSource.close()
        loading.value = false
        return
      }
      
      aiResponse += data
      const lastMessage = messages.value[messages.value.length - 1]
      if (lastMessage && lastMessage.type === 'ai') {
        lastMessage.content = aiResponse
      } else {
        messages.value.push({ type: 'ai', content: aiResponse })
      }
      
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight
    }

    eventSource.onerror = (error) => {
      console.error('SSE Error:', error)
      eventSource.close()
      loading.value = false
      // 只在没有收到任何响应时显示错误消息
      if (!aiResponse) {
        messages.value.push({ type: 'ai', content: '抱歉，发生了一些错误，请稍后重试。' })
      }
    }
  } catch (error) {
    console.error('Error:', error)
    loading.value = false
    messages.value.push({ type: 'ai', content: '抱歉，发生了一些错误，请稍后重试。' })
  }
}

onMounted(() => {
  messages.value.push({ type: 'ai', content: '你好！我是AI学习助手，有什么我可以帮你的吗？' })
})
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
}

.chat-header {
  padding: 12px 24px;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.header-title {
  font-size: 18px;
  font-weight: 500;
  color: #202124;
}

.chat-id {
  font-size: 14px;
  color: #999;
}

.chat-messages {
  flex: 1;
  padding: 32px 24px;
  overflow-y: auto;
  background-color: #f8f9fa;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.message {
  width: 100%;
  max-width: 800px;
  margin-bottom: 28px;
  display: flex;
  justify-content: flex-start;
}

.message-content {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  max-width: 90%;
}

.avatar {
  font-size: 20px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #e8eaed;
  border-radius: 50%;
  flex-shrink: 0;
}

.text {
  padding: 16px 20px;
  border-radius: 16px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  white-space: pre-wrap;
  font-size: 15px;
  line-height: 1.6;
  color: #202124;
}

.user-message {
  justify-content: flex-end;
}

.user-message .message-content {
  flex-direction: row-reverse;
}

.user-message .text {
  background-color: #e8f0fe;
  color: #202124;
}

.chat-input {
  padding: 16px 24px;
  background-color: #f8f9fa;
  border-top: 1px solid #e6e6e6;
  display: flex;
  justify-content: center;
  align-items: center;
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  width: 100%;
  max-width: 800px;
  background-color: #fff;
  border-radius: 24px;
  border: 1px solid #dadce0;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  overflow: hidden;
}

.input-wrapper.focused {
  border-color: #1a73e8;
  box-shadow: 0 1px 6px rgba(26, 115, 232, 0.2);
}

.input-wrapper:hover {
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.15);
}

.el-textarea__inner {
  box-shadow: none !important;
  border: none !important;
  resize: none !important;
  padding: 12px 16px !important;
  font-size: 16px !important;
  line-height: 1.5 !important;
  background-color: transparent !important;
}

.button-wrapper {
  padding: 8px 16px;
}

.send-button {
  background-color: #1a73e8;
  border-color: #1a73e8;
  border-radius: 20px;
  padding: 10px 20px;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.send-button:hover {
  background-color: #1558b0;
  border-color: #1558b0;
}

.send-button:disabled {
  background-color: #c5d9f5 !important;
  border-color: #c5d9f5 !important;
  cursor: not-allowed;
}
</style>