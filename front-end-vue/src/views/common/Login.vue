<template>
  <div class="login-container">
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
              <el-button type="primary" style="width: 100%" @click="handleUserLogin">登录</el-button>
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
import { useUserStore } from '../../stores/user.js'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('user')

const userForm = ref({
  phone: '13800138001',
  password: 'user123'
})

const adminForm = ref({
  username: 'admin',
  password: 'admin123'
})

const userFormRef = ref()
const adminFormRef = ref()

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

const handleTabChange = () => {
  // 切换标签时重置表单
}

const handleUserLogin = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await login(userForm.value)
        userStore.setToken(res.data.token)
        userStore.setUserInfo({ id: res.data.userId, phone: res.data.phone, name: res.data.name })
        ElMessage.success('登录成功')
        router.push('/user/home')
      } catch (error) {
        ElMessage.error('登录失败')
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
        ElMessage.error('登录失败')
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 450px;
}

.card-header {
  text-align: center;
  color: #333;
}

.card-header h2 {
  margin: 0;
}
</style>

