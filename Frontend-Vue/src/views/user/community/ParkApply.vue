<template>
  <div class="parking-apply-dialog">
    <el-dialog
      v-model="dialogVisible"
      title="车位申请"
      width="600px"
      :close-on-click-modal="false"
      @close="handleClose"
    >
      <el-form 
        :model="applyForm" 
        ref="applyFormRef" 
        :rules="rules" 
        label-width="120px"
      >
        <el-form-item label="车位编号" prop="spaceNumber">
          <el-input 
            v-model="applyForm.spaceNumber" 
            placeholder="请输入车位编号"
            readonly 
          />
        </el-form-item>
        <el-form-item label="车牌号" prop="carNumber">
          <el-input 
            v-model="applyForm.carNumber" 
            placeholder="请输入车牌号（如：粤A12345）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" @click="submitApply">提交申请</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createParkSpaceApplication } from '@/api/user' // 导入申请接口
import { useUserStore } from '@/stores/user.js'
import { ElMessage } from 'element-plus'

// 路由和用户信息
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 对话框显示控制
const dialogVisible = ref(true)

// 表单数据
const applyForm = ref({
  userId: userStore.userInfo?.id, // 当前登录用户ID（从store获取）
  spaceNumber: '', // 车位编号（从列表页跳转时携带）
  carNumber: '', // 车牌号
})

// 表单验证规则
const rules = {
  spaceNumber: [
    { required: true, message: '请选择车位', trigger: 'blur' }
  ],
  carNumber: [
    { required: true, message: '请输入车牌号', trigger: 'blur' },
    { pattern: /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{5}$/, message: '车牌号格式错误（如：粤A12345）', trigger: 'blur' }
  ]
}

// 表单引用
const applyFormRef = ref(null)

// 页面加载时获取路由参数（若从列表页跳转）
onMounted(() => {
  if (route.query.spaceNumber) {
    applyForm.value.spaceNumber = route.query.spaceNumber
  }
})

// 提交申请
const submitApply = async () => {
  // 表单验证
  await applyFormRef.value.validate()
  
  try {
    // 调用后端申请接口
    const res = await createParkSpaceApplication(applyForm.value)
    if (res.code === 200) {
      ElMessage.success('申请提交成功，等待审批')
      handleClose()
      // 返回车位列表页
      router.push('/user/community/parkspace')
    } else {
      ElMessage.error(res.message || '申请提交失败')
    }
  } catch (err) {
    console.error('申请失败', err)
    ElMessage.error('网络错误，请重试')
  }
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
  // 返回车位列表页
  router.push('/user/community/parkspace')
}
</script>

<style scoped>
.parking-apply-dialog {
  width: 100%;
  height: 100%;
}

.dialog-footer {
  padding: 20px 0;
  text-align: right;
}
</style>