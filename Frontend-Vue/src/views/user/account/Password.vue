<template>
  <div class="password-page">
    <h2>修改密码</h2>
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="原密码" prop="oldPassword">
        <el-input type="password" v-model="form.oldPassword" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input type="password" v-model="form.newPassword" />
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input type="password" v-model="form.confirmPassword" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleChange">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { resetPassword } from '@/api/auth'

const form = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })
const rules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min:6, message:'密码至少6位', trigger:'blur'}],
  confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }]
}

const handleChange = async () => {
  if (form.value.newPassword !== form.value.confirmPassword) {
    ElMessage.error('两次输入密码不一致')
    return
  }
  try {
    await resetPassword({ oldPassword: form.value.oldPassword, newPassword: form.value.newPassword })
    ElMessage.success('密码修改成功')
  } catch (err) {
    ElMessage.error('修改失败')
  }
}
</script>

<style scoped>
.password-page { padding: 20px; background: #fff }
</style>