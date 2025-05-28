<template>
  <div class="home">
    <div class="background-animation"></div>
    <el-card class="welcome-card">
      <h1>欢迎使用AI助手系统</h1>
      <div class="app-list">
        <div
          class="app-button primary-button"
          @click="$router.push('/study-assistant')"
        >
          <div class="button-content">
            <div class="icon">🎓</div>
            <div class="text">
              <div class="title">AI学习助手</div>
              <div class="desc">智能学习伙伴，为您解答疑惑</div>
            </div>
          </div>
        </div>
        <div
          class="app-button success-button"
          @click="$router.push('/super-agent')"
        >
          <div class="button-content">
            <div class="icon">🤖</div>
            <div class="text">
              <div class="title">AI超级智能体</div>
              <div class="desc">强大的AI助手，助您事半功倍</div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #1a1a1a;
  position: relative;
  overflow: hidden;
}

.background-animation {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, #ff6b6b, #4ecdc4, #45b7d1, #6c5ce7);
  animation: gradient 15s ease infinite;
  transform-origin: center center;
  z-index: 1;
  filter: blur(20px);
}

@keyframes gradient {
  0% {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.welcome-card {
  position: relative;
  z-index: 2;
  width: 500px;
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(10px);
  border: none !important;
  border-radius: 20px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1) !important;
  padding: 40px !important;
}

.welcome-card h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 40px;
  font-weight: 600;
}

.app-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.app-button {
  padding: 20px;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.app-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, rgba(255,255,255,0.1), rgba(255,255,255,0));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.app-button:hover::before {
  opacity: 1;
}

.app-button:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.primary-button {
  background: linear-gradient(135deg, #4e54c8, #8f94fb);
}

.success-button {
  background: linear-gradient(135deg, #43c6ac, #28a745);
}

.button-content {
  display: flex;
  align-items: center;
  gap: 20px;
  color: white;
}

.icon {
  font-size: 32px;
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.text {
  text-align: left;
}

.title {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 4px;
}

.desc {
  font-size: 14px;
  opacity: 0.8;
}

:deep(.el-card__body) {
  padding: 0;
}
</style>