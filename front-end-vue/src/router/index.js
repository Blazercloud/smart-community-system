import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useMerchantStore } from '../stores/merchant'
import { ElMessage } from 'element-plus'

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
    path: '/user/register',
    name: 'UserRegister',
    component: () => import('../views/user/Register.vue')
  },
  {
    path: '/merchant/register',
    name: 'MerchantRegister',
    component: () => import('../views/merchant/Register.vue')
  },
  {
    path: '/user',
    component: () => import('../views/user/Home.vue'),
    children: [
      {
        path: 'home',
        name: 'UserHome',
        component: () => import('../views/user/Index.vue')
      },
      {
        path: 'account/profile',
        name: 'UserProfile',
        component: () => import('../views/user/account/Profile.vue')
      },
      {
        path: 'account/wallet',
        name: 'UserWallet',
        component: () => import('../views/user/account/Wallet.vue')
      },
      {
        path: 'account/password',
        name: 'UserPassword',
        component: () => import('../views/user/account/Password.vue')
      },
      {
        path: 'community/payment',
        name: 'UserPayment',
        component: () => import('../views/user/community/Payment.vue')
      },
      {
        path: 'community/service',
        name: 'UserService',
        component: () => import('../views/user/community/Service.vue')
      },
      {
        path: 'community/repair',
        name: 'UserRepair',
        component: () => import('../views/user/community/Repair.vue')
      },
      {
        path: 'community/notice',
        name: 'UserNotice',
        component: () => import('../views/user/community/Notice.vue')
      },
      {
        path: 'community/parkspace',
        name: 'ParkSpace',
        component: () => import('../views/user/community/ParkSpace.vue')
      },
      {
        path: 'community/parking/apply',
        name: 'ParkingApply',
        component: () => import('../views/user/community/ParkApply.vue')
      },
      {
        path: 'mall/home',
        name: 'UserMall',
        component: () => import('../views/user/mall/Home.vue')
      },
      {
        path: 'mall/orders',
        name: 'UserMallOrders',
        component: () => import('../views/user/mall/Orders.vue')
      },
      {
        path: 'mall/cart',
        name: 'UserMallCart',
        component: () => import('../views/user/mall/Cart.vue')
      },
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
      },
      {
        path: 'property/bill',
        name: 'AdminBill',
        component: () => import('../views/admin/property/Bill.vue')
      },
      {
        path: 'property/parkingspaces',
        name: 'AdminparkingSpaces',
        component: () => import('../views/admin/property/Parkingspaces.vue')
      },
      {
        path: 'property/notices',
        name: 'AdminNotices',
        component: () => import('../views/admin/property/Notice.vue')
      },
      {
        path: 'property/visitors',
        name: 'AdminVisitors',
        component: () => import('../views/admin/property/Visitors.vue')
      },
      {
        path: 'property/complaints',
        name: 'AdminComplaints',
        component: () => import('../views/admin/property/Complaints.vue')
      },
      {
        path: 'property/repairs',
        name: 'AdminRepairs',
        component: () => import('../views/admin/property/Repairs.vue')
      },
    ]
  },
  {
    path: '/merchant',
    component: () => import('../views/merchant/Home.vue'),
    children: [
      {
        path: 'home',
        name: 'MerchantHome',
        component: () => import('../views/merchant/Index.vue')
      },
      {
        path: 'stores',
        name: 'MerchantStores',
        component: () => import('../views/merchant/stores/StoreManagement.vue')
      },
      {
        path: 'products',
        name: 'MerchantProducts',
        component: () => import('../views/merchant/products/ProductManagement.vue')
      },
      {
        path: 'orders',
        name: 'MerchantOrders',
        component: () => import('../views/merchant/orders/OrderManagement.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const merchantStore = useMerchantStore()
  const userToken = userStore.token
  const merchantToken = merchantStore.merchantToken

  // 访问登录页和注册页逻辑
  if (to.path === '/login' || to.path === '/user/register' || to.path === '/merchant/register') {
    if (merchantToken) {
      next('/merchant/home')
      return
    }
    if (userToken) {
      // 根据用户信息中的角色决定跳转到哪个首页
      if (userStore.userInfo?.username === 'admin') {
        next('/admin/home')
      } else {
        next('/user/home')
      }
      return
    }
    next()
    return
  }

  // 商家路由权限判断
  if (to.path.startsWith('/merchant')) {
    if (!merchantToken) {
      ElMessage.warning('请先登录')
      next('/login')
      return
    }
    next()
    return
  }

  // 管理员路由权限判断
  if (to.path.startsWith('/admin')) {
    if (!userToken || userStore.userInfo?.username !== 'admin') {
      ElMessage.warning('需要管理员权限')
      next('/login')
      return
    }
    next()
    return
  }

  // 用户路由权限判断
  if (to.path.startsWith('/user')) {
    if (!userToken) {
      ElMessage.warning('请先登录')
      next('/login')
      return
    }
    if (userStore.userInfo?.username === 'admin') {
      next('/admin/home')
      return
    }
    next()
    return
  }

  // 其他路由
  next()
})

export default router
