<template>
  <div class="profile-page">
    <h2>个人资料</h2>
    <el-form :model="user" label-width="100px">
      <el-form-item label="头像">
        <el-avatar :src="user.avatar" size="64" />
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="user.username" />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="user.phone" disabled />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="user.email" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo } from '@/api/auth'

const user = ref({})

const load = async () => {
  try {
    const res = await getUserInfo()
    user.value = res.data || {}
  } catch (err) {
    console.error(err)
  }
}

const save = () => {
  // 目前无编辑接口，这里先做本地提示
  ElMessage.success('已保存（示例）')
}

onMounted(load)
</script>

<style scoped>
.profile-page { padding: 20px; background: #fff }
</style>