import { createRouter, createWebHistory } from 'vue-router'
import HobbyForm from "@/views/HobbyForm.vue";
import UserProfile from '@/views/UserProfile.vue'
import NameForm from "@/views/NameForm.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/hobby-form',
      name: 'HobbyForm',
      props: (route) => ({
        userId: Number(route.query.userId),
      }),
      component: HobbyForm,
    },
    {
      path: '/profile',
      name: 'UserProfile',
      component: UserProfile,
    },
    {
      path: '/users',
      name: 'NameForm',
      component: NameForm,
    },
  ],
})

export default router
