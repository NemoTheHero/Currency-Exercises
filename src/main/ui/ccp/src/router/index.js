import { createRouter, createWebHistory } from 'vue-router'
import HobbyForm from "@/views/HobbyForm.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/hobby-form',
      name: 'hobbyForm',
      component: HobbyForm,
    }
  ],
})

export default router
