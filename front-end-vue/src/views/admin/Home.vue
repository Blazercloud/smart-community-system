<template>
  <div class="admin-home">
    <el-container>
      <el-header>
        <div class="header-content">
          <div class="brand">
            <div class="logo" aria-hidden></div>
            <h2 class="brand-title">智慧社区管理端</h2>
          </div>
          <div class="admin-info">
            <span>欢迎, {{ userInfo?.name }}</span>
            <el-button class="logout-btn" size="small" @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px">
          <el-menu
  router
  default-active="/admin/home"
  background-color="#545c64"
  text-color="#fff"
  active-text-color="#ffd04b"
>
  <!-- 首页 -->
  <el-menu-item index="/admin/home">
    <el-icon><View /></el-icon>
    <span>首页</span>
  </el-menu-item>

  <!-- 物业管理 -->
  <el-sub-menu index="property">
    <template #title>
      <el-icon><Document /></el-icon>
      <span>物业管理</span>
    </template>
    <el-menu-item index="/admin/property/bill">物业缴费</el-menu-item>
    <el-menu-item index="/admin/property/parkingspaces">车位管理</el-menu-item>
  </el-sub-menu>

  <!-- 公告与访客 -->
  <el-sub-menu index="community">
    <template #title>
      <el-icon><User /></el-icon>
      <span>社区管理</span>
    </template>
    <el-menu-item index="/admin/property/notices">公告通知</el-menu-item>
    <el-menu-item index="/admin/property/visitors">访客管理</el-menu-item>
  </el-sub-menu>

  <!-- 投诉与报修 -->
  <el-sub-menu index="service">
    <template #title>
      <el-icon><ShoppingBag /></el-icon>
      <span>报事与投诉</span>
    </template>
    <el-menu-item index="/admin/property/complaints">物业投诉</el-menu-item>
    <el-menu-item index="/admin/property/repairs">报事维修</el-menu-item>
  </el-sub-menu>

  <!-- 数据统计 -->
  <el-menu-item index="/admin/statistics">
    <el-icon><DataAnalysis /></el-icon>
    <span>数据统计</span>
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
import { View, User, Shop, ShoppingBag, Document, DataAnalysis } from '@element-plus/icons-vue'

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
.admin-home {
  /* CSS variables must be defined on a real element when styles are scoped.
     Defining them here ensures `var(--primary)` resolves inside this view. */
  --primary: #545c64; /* sidebar / header background */
  --accent: #ffd04b;  /* active / accent color */
  --header-height: 64px;

  height: 100vh;
  background: #f6f7fb;
  overflow: hidden; /* 防止页面整体滚动 */
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

.admin-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.admin-info span {
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
  overflow-y: auto; /* 允许内容区域滚动 */
  height: 100%; /* 确保内容区域充满剩余空间 */
}

/* Make the main container fill viewport and ensure the aside stretches */
.admin-home > .el-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.admin-home > .el-container > .el-container {
  /* nested container (holds aside + main) should expand */
  flex: 1 1 auto;
  display: flex;
  min-height: 0; /* allow children to scroll if needed */
}

.admin-home .el-aside {
  /* make aside take full height under header */
  height: calc(100vh - var(--header-height));
  box-sizing: border-box;
  overflow: auto;
}
</style>
