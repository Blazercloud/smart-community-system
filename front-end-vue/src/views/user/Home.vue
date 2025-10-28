<template>
  <div class="user-home">
    <el-container>
      <el-header>
        <div class="header-content">
          <h2>智慧社区用户端</h2>
          <div class="user-info">
            <span>欢迎, {{ userInfo?.name }}</span>
            <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
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
              <el-icon><Home /></el-icon>
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
import { Home, User, Wallet, ShoppingBag, Document, Service } from '@element-plus/icons-vue'

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
  height: 100vh;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info span {
  color: #333;
  font-size: 14px;
}
</style>

