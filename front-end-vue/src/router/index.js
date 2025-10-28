import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/common/Login.vue')
  },
  {
    path: '/user',
    component: () => import('../views/user/Home.vue'),
    children: [
      {
        path: 'home',
        name: 'UserHome',
        component: () => import('../views/user/Index.vue')
      }
    ]
  },
  {
    path: '/admin',
    component: () => import('../views/admin/Home.vue'),
    children: [
      {
        path: 'home',
        name: 'AdminHome',
        component: () => import('../views/admin/Index.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
