<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>商家注册</h2>
        </div>
      </template>
      
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" />
        </el-form-item>
        <el-form-item label="商家名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商家名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="商家地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入商家地址" />
        </el-form-item>
        <el-form-item label="商家简介" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入商家简介"
          />
        </el-form-item>
        <el-form-item>
          <div class="button-group">
            <el-button type="primary" @click="handleRegister">注册</el-button>
            <el-button @click="goBack">返回登录</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../../api/shop.js'

const router = useRouter()
const formRef = ref()

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  phone: '',
  address: '',
  description: ''
})

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    if (form.value.confirmPassword !== '') {
      formRef.value.validateField('confirmPassword')
    }
    callback()
  }
}

const validatePass2 = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.value.password) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, message: '用户名长度不能小于4位', trigger: 'blur' }
  ],
  password: [
    { required: true, validator: validatePass, trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePass2, trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入商家名称', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入商家地址', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入商家简介', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const { confirmPassword, ...registerData } = form.value
        const res = await register(registerData)
        ElMessage.success('注册成功，请等待管理员审核')
        router.push('/login')
      } catch (error) {
        ElMessage.error(error?.message || '注册失败')
      }
    }
  })
}

const goBack = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #e0e2eb;
  padding: 20px 0;
}

.register-card {
  width: 500px;
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
  justify-content: center;
  gap: 20px;
}
</style>]]>