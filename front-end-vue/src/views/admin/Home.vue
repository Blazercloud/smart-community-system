<template>
  <div class="admin-home">
    <el-container>
      <el-header>
        <div class="header-content">
          <h2>智慧社区管理端</h2>
          <div class="admin-info">
            <span>欢迎, {{ userInfo?.name }}</span>
            <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
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
            <el-menu-item index="/admin/home">
              <el-icon><Home /></el-icon>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/admin/users">
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/stores">
              <el-icon><Shop /></el-icon>
              <span>门店管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/products">
              <el-icon><ShoppingBag /></el-icon>
              <span>商品管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/orders">
              <el-icon><Document /></el-icon>
              <span>订单管理</span>
            </el-menu-item>
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
import { Home, User, Shop, ShoppingBag, Document, DataAnalysis } from '@element-plus/icons-vue'

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
  height: 100vh;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.admin-info span {
  color: #333;
  font-size: 14px;
}
</style>

