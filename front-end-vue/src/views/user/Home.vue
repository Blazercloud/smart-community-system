<template>
  <div class="user-home">
    <el-container>
      <el-header>
        <div class="header-content">
          <div class="brand">
            <div class="logo" aria-hidden></div>
            <h2 class="brand-title">智慧社区用户端</h2>
          </div>
          <div class="user-info">
            <span>欢迎, {{ userInfo?.name }}</span>
            <el-button class="logout-btn" size="small" @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px">
          <el-menu
            router
            default-active="/user/home"
            background-color="#545c64"
            text-color="#fff"
            active-text-color="#ffd04b"
          >
            <el-menu-item index="/user/home">
              <el-icon><View /></el-icon>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/user/profile">
              <el-icon><User /></el-icon>
              <span>个人中心</span>
            </el-menu-item>
            <el-menu-item index="/user/wallet">
              <el-icon><Wallet /></el-icon>
              <span>我的钱包</span>
            </el-menu-item>
            <el-menu-item index="/shop/products">
              <el-icon><ShoppingBag /></el-icon>
              <span>商城</span>
            </el-menu-item>
            <el-menu-item index="/community/notices">
              <el-icon><Document /></el-icon>
              <span>社区公告</span>
            </el-menu-item>
            <el-menu-item index="/community/services">
              <el-icon><Service /></el-icon>
              <span>社区服务</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../stores/user.js'
import { View, User, Wallet, ShoppingBag, Document, Service } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)

const handleLogout = () => {
  userStore.clearUserInfo()
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.user-home {
  /* CSS variables must be defined on a real element when styles are scoped.
     Defining them here ensures `var(--primary)` resolves inside this view. */
  --primary: #545c64; /* sidebar / header background */
  --accent: #ffd04b;  /* active / accent color */
  --header-height: 64px;

  height: 100vh;
  background: #f6f7fb;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.el-header {
  background: var(--primary) !important;
  color: #fff;
  height: var(--header-height);
  padding: 0 20px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: linear-gradient(135deg, var(--accent), #ffb84d);
  box-shadow: 0 2px 8px rgba(0,0,0,0.12);
}

.brand-title {
  color: #fff;
  font-size: 18px;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info span {
  color: #fff;
  font-size: 14px;
}

.logout-btn {
  background: transparent;
  color: #fff;
  border-color: rgba(255,255,255,0.18);
}

.el-aside {
  background: var(--primary);
  padding-top: 12px;
}

.el-menu {
  border-right: none;
}

.el-menu .el-menu-item {
  padding-left: 20px;
}

.el-menu .el-menu-item.is-active {
  background: rgba(255,208,75,0.12);
}

.el-main {
  padding: 20px;
  background: #f6f7fb;
}

/* Make the main container fill viewport and ensure the aside stretches */
.user-home > .el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.user-home > .el-container > .el-container {
  /* nested container (holds aside + main) should expand */
  flex: 1 1 auto;
  display: flex;
  min-height: 0; /* allow children to scroll if needed */
}

.user-home .el-aside {
  /* make aside take full height under header */
  height: calc(100vh - var(--header-height));
  box-sizing: border-box;
  overflow: auto;
}
</style>

