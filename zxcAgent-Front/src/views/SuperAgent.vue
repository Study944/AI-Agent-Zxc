<template>
  <div class="chat-container">
    <div class="chat-header">
      <el-button @click="$router.push('/')" type="primary" plain size="small">
        返回主页
      </el-button>
      <span class="header-title">AI超级智能体</span>
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

const messages = ref([])
const inputMessage = ref('')
const loading = ref(false)
const messageContainer = ref(null)

async function sendMessage() {
  if (!inputMessage.value.trim() || loading.value) return

  const userMessage = inputMessage.value.trim()
  messages.value.push({ type: 'user', content: userMessage })
  inputMessage.value = ''
  loading.value = true

  await nextTick()
  messageContainer.value.scrollTop = messageContainer.value.scrollHeight

  try {
    const response = await fetch(
      `http://localhost:8123/api/ai/manus/chat?message=${encodeURIComponent(userMessage)}`,
      {
        headers: {
          'Accept': 'text/event-stream'
        }
      }
    )

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let aiResponse = ''

    while (true) {
      const { value, done } = await reader.read()
      if (done) {
        loading.value = false
        break
      }

      const chunk = decoder.decode(value)
      const lines = chunk.split('\n')

      for (const line of lines) {
        if (line.startsWith('data:')) {
          const data = line.slice(5).trim()
          if (data === '[DONE]') {
            loading.value = false
            return
          }
          
          if (data) { // 只有当data不为空时才添加消息
            try {
              messages.value.push({ type: 'ai', content: data })
              messageContainer.value.scrollTop = messageContainer.value.scrollHeight
            } catch (e) {
              console.error('Error processing message:', e)
            }
          }
        }
      }
    }
  } catch (error) {
    console.error('Error:', error)
    loading.value = false
    messages.value.push({ type: 'ai', content: '抱歉，发生了一些错误，请稍后重试。' })
  }
}

onMounted(() => {
  messages.value.push({ type: 'ai', content: '你好！我是AI超级智能体，有什么我可以帮你的吗？' })
})
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #e0f7fa 0%, #b2ebf2 60%, #e3f2fd 100%);
}

.chat-header {
  padding: 16px 32px;
  background: linear-gradient(90deg, #29b6f6 60%, #00e5ff 100%);
  border-bottom: 2px solid #4dd0e1;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px rgba(41, 182, 246, 0.12);
}

.header-title {
  font-size: 20px;
  font-weight: 700;
  color: #01579b;
  letter-spacing: 2px;
  text-shadow: 0 0 8px #b2ebf2, 0 0 2px #e0f7fa;
}

.chat-messages {
  flex: 1;
  padding: 32px 24px;
  overflow-y: auto;
  background: transparent;
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
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #4dd0e1 0%, #b2ebf2 100%);
  border-radius: 50%;
  flex-shrink: 0;
  color: #01579b;
  border: 2px solid #29b6f6;
  box-shadow: 0 0 8px #b2ebf2;
}

.text {
  padding: 16px 20px;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 1px 8px rgba(41, 182, 246, 0.08);
  white-space: pre-wrap;
  font-size: 15px;
  line-height: 1.6;
  color: #01579b;
}

.user-message {
  justify-content: flex-end;
}

.user-message .message-content {
  flex-direction: row-reverse;
}

.user-message .text {
  background: linear-gradient(90deg, #b2ebf2 60%, #e3f2fd 100%);
  color: #006064;
  font-weight: bold;
}

.user-message .avatar {
  background: #29b6f6;
  color: #fff;
  border: 2px solid #b2ebf2;
  box-shadow: 0 0 8px #b2ebf2;
}

.chat-input {
  padding: 20px 32px;
  background: #e0f7fa;
  border-top: 2px solid #4dd0e1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  width: 100%;
  max-width: 800px;
  background: #fff;
  border-radius: 24px;
  border: 2px solid #4dd0e1;
  box-shadow: 0 1px 12px rgba(41, 182, 246, 0.08);
  transition: all 0.3s ease;
  overflow: hidden;
}

.input-wrapper:focus-within {
  border-color: #29b6f6;
  box-shadow: 0 1px 12px #b2ebf2;
}

.input-wrapper:hover {
  box-shadow: 0 1px 12px #b2ebf2;
}

.el-textarea__inner {
  box-shadow: none !important;
  border: none !important;
  resize: none !important;
  padding: 12px 16px !important;
  font-size: 16px !important;
  line-height: 1.5 !important;
  background-color: transparent !important;
  color: #01579b !important;
}

.button-wrapper {
  padding: 8px 16px;
}

.send-button {
  background: linear-gradient(90deg, #29b6f6 60%, #00e5ff 100%);
  border: none;
  border-radius: 20px;
  padding: 10px 24px;
  font-size: 16px;
  color: #fff;
  font-weight: 700;
  box-shadow: 0 2px 12px #b2ebf244;
  transition: background 0.3s;
}

.send-button:hover {
  background: linear-gradient(90deg, #0288d1 60%, #4dd0e1 100%);
}

.send-button:disabled {
  background: #b2ebf2 !important;
  color: #fff !important;
  cursor: not-allowed;
}
</style>