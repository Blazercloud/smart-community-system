<template>
  <div class="merchant-home">
    <el-container>
      <el-header>
        <div class="header-content">
          <div class="brand">
            <div class="logo" aria-hidden></div>
            <h2 class="brand-title">商家后台</h2>
          </div>
          <div class="merchant-info">
            <span>欢迎, {{ merchantInfo?.name }}</span>
            <el-button class="logout-btn" size="small" @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px">
          <el-menu
            router
            default-active="/merchant/home"
            background-color="#2d8cf0"
            text-color="#fff"
            active-text-color="#ffd04b"
          >
            <el-menu-item index="/merchant/home">
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/merchant/stores">
              <span>门店管理</span>
            </el-menu-item>
            <el-menu-item index="/merchant/products">
              <span>商品管理</span>
            </el-menu-item>
            <el-menu-item index="/merchant/orders">
              <span>订单管理</span>
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
import { useMerchantStore } from '../../stores/merchant.js'

const router = useRouter()
const merchantStore = useMerchantStore()

const merchantInfo = computed(() => merchantStore.merchantInfo)

const handleLogout = () => {
  merchantStore.clearMerchantInfo()
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.merchant-home {
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
  background: #2d8cf0 !important;
  color: #fff;
  height: 64px;
  padding: 0 20px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}

.brand-title { color: #fff; margin:0 }
.merchant-info { display:flex; gap:12px; align-items:center }
.logout-btn { background:transparent; color:#fff; border-color: rgba(255,255,255,0.18) }
.el-aside { background: #2d8cf0; padding-top: 12px }
.el-main { padding: 20px }
</style>
