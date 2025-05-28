import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/study-assistant',
    name: 'StudyAssistant',
    component: () => import('../views/StudyAssistant.vue')
  },
  {
    path: '/super-agent',
    name: 'SuperAgent',
    component: () => import('../views/SuperAgent.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router