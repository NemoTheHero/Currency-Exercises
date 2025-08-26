import { createRouter, createWebHistory } from 'vue-router'
import HobbyForm from "@/views/HobbyForm.vue";
import UserProfile from '@/views/UserProfile.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/hobby-form',
      name: 'hobbyForm',
      component: HobbyForm,
    },
    {
      path: '/profile',
      name: 'UserProfile',
      component: UserProfile,
    },
  ],
})

export default router
