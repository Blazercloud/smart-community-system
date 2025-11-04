<template>
  <div class="login-container"id="building">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>东软智慧社区</h2>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="用户登录" name="user">
          <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="80px">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="userForm.password" type="password" placeholder="请输入密码" />
            </el-form-item>
            <el-form-item>
              <div class="button-group">
                <el-button type="primary" style="width: 48%" @click="handleUserLogin">登录</el-button>
                <el-button type="info" style="width: 48%" @click="handleUserRegister">注册</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="管理员登录" name="admin">
          <el-form :model="adminForm" :rules="adminRules" ref="adminFormRef" label-width="80px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="adminForm.username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="adminForm.password" type="password" placeholder="请输入密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="width: 100%" @click="handleAdminLogin">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="商家登录" name="merchant">
          <el-form :model="merchantForm" :rules="merchantRules" ref="merchantFormRef" label-width="80px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="merchantForm.username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="merchantForm.password" type="password" placeholder="请输入密码" />
            </el-form-item>
            <el-form-item>
              <div class="button-group">
                <el-button type="primary" style="width: 48%" @click="handleMerchantLogin">登录</el-button>
                <el-button type="info" style="width: 48%" @click="handleMerchantRegister">注册</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../../api/user.js'
import { adminLogin } from '../../api/admin.js'
// import { merchantLogin } from '../../api/merchant.js'
import { useUserStore } from '../../stores/user.js'
import { useMerchantStore } from '../../stores/merchant.js'

const router = useRouter()
const userStore = useUserStore()
const merchantStore = useMerchantStore()

const activeTab = ref('user')

const userForm = ref({
  phone: '13800001111',
  password: '123456'
})

const adminForm = ref({
  username: 'admin',
  password: 'admin123'
})

const merchantForm = ref({
  username: 'seller',
  password: 'seller123'
})

const userFormRef = ref()
const adminFormRef = ref()
const merchantFormRef = ref()

const rules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const adminRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const merchantRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleTabChange = () => {
  // 切换标签时重置表单
  try {
    userFormRef.value && userFormRef.value.resetFields()
  } catch (e) {}
  try {
    adminFormRef.value && adminFormRef.value.resetFields()
  } catch (e) {}
  try {
    merchantFormRef.value && merchantFormRef.value.resetFields()
  } catch (e) {}
}

const handleUserLogin = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await login(userForm.value)

        // 解包兼容多种后端返回格式
        const data = res.data || {}
        const user = data.user || {}

        userStore.setToken(data.token)
        userStore.setUserInfo({
          id: data.userId || user.id,
          phone: data.phone || user.phone,
          name: data.name || user.name
        })

        ElMessage.success('登录成功')
        router.push('/user/home')
      } catch (error) {
        ElMessage.error(error?.message || '登录失败')
      }
    }
  })
}


const handleAdminLogin = async () => {
  if (!adminFormRef.value) return
  
  await adminFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await adminLogin(adminForm.value)
        userStore.setToken(res.data.token)
        userStore.setUserInfo({ id: res.data.adminId, username: res.data.username, name: res.data.name })
        ElMessage.success('登录成功')
        router.push('/admin/home')
      } catch (error) {
        // Show server error message when available for better feedback
        ElMessage.error(error?.message || '登录失败')
      }
    }
  })
}

const handleMerchantLogin = async () => {
  if (!merchantFormRef.value) return

  await merchantFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await merchantLogin(merchantForm.value)
        merchantStore.setMerchantToken(res.data.token)
        merchantStore.setMerchantInfo({ id: res.data.merchantId || res.data.id, username: res.data.username, name: res.data.name })
        ElMessage.success('登录成功')
        router.push('/merchant/home')
      } catch (error) {
        ElMessage.error(error?.message || '登录失败')
      }
    }
  })
}

const handleUserRegister = () => {
  router.push('/user/register')
}

const handleMerchantRegister = () => {
  router.push('/merchant/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100%;
  position: fixed;
  background: url('/images/BJ1.jpg') no-repeat center center;
  background-size: cover;
}

.login-card {
  width: 450px;
  background: rgba(255, 255, 255, 0.524);    /* 降低背景不透明度 */
  backdrop-filter: blur(10px);              /* 增加模糊效果 */
  border-radius: 16px;                      /* 圆角边框 */
  border: 1px solid rgba(255, 255, 255, 0.3); /* 添加半透明边框 */
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);  /* 柔和的阴影 */
}

.card-header {
  text-align: center;
  color: #333;
}

.card-header h2 {
  margin: 0;
}

.button-group {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

/* 美化表单元素 */
:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

:deep(.el-tabs__item) {
  color: rgba(0, 0, 0, 0.7);
}

:deep(.el-tabs__item.is-active) {
  color: #409eff;
  font-weight: bold;
}

/* 确保内容清晰可见 */
.el-form-item__label {
  color: #2c3e50;
  font-weight: 500;
}
</style>