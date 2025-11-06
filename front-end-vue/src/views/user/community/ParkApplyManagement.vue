<template>
  <div class="application-management">
    <!-- 添加返回按钮 -->
    <div class="page-header">
      <el-button 
        size="small" 
        @click="goBack"
        style="margin-bottom: 10px; margin-right: 20px;"
      >
        <el-icon><ArrowLeft  /></el-icon>
        返回上一级
      </el-button>
      <h2>我的车位申请</h2>
    </div>

    <!-- 申请列表表格 -->
    <el-table 
      :data="applications" 
      border 
      style="width: 100%"
      v-loading="loading"
      :row-class-name="tableRowClassName"
    >
      <!-- 表格列定义保持不变 -->
      <el-table-column label="序号" width="60">
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="spaceNumber" label="车位编号" width="150" align="center">
        <template #default="scope">
          <el-tag type="primary">{{ scope.row.spaceNumber }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="carNumber" label="车牌号" width="120" align="center" />
      <el-table-column prop="status" label="状态" width="120" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.status === '0'" type="info">待审批</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="success">已同意</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="danger">已退回</el-tag>
          <el-tag v-else>{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="applyTime" label="申请时间" width="220" align="center">
        <template #default="scope">
          {{ formatTime(scope.row.applyTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <!-- 只有待审批状态可操作 -->
          <el-button 
            link 
            type="primary" 
            @click="handleEdit(scope.row)"
            :disabled="scope.row.status !== '0'"
          >
            修改
          </el-button>
          <el-button 
            link 
            type="danger" 
            @click="handleWithdraw(scope.row)"
            :disabled="scope.row.status !== '0'"
          >
            撤回
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getParkSpaceApplication } from '@/api/community.js'
import { useUserStore } from '@/stores/user.js'
import { ArrowLeft  } from '@element-plus/icons-vue'

// 状态管理
const applications = ref([])
const loading = ref(false)
const userStore = useUserStore()

// 页面初始化加载申请列表
onMounted(() => {
  fetchApplications()
})

// 获取用户申请列表
const fetchApplications = async () => {
  if (!userStore.userInfo?.id) {
    ElMessage.warning('请先登录')
    return
  }

  loading.value = true
  try {
    // 调用用户申请查询接口（传入当前用户ID）
    const params = {
      userId: userStore.userInfo.id
    }
    const res = await getParkSpaceApplication(params)
    
    if (res.code === 200) {
      applications.value = res.data || []
    } else {
      ElMessage.error(res.message || '获取申请列表失败')
      applications.value = []
    }
  } catch (error) {
    console.error('获取申请列表失败:', error)
    ElMessage.error('加载申请数据失败')
    applications.value = []
  } finally {
    loading.value = false
  }
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  return new Date(timeStr).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 表格斑马纹样式
const tableRowClassName = ({ rowIndex }) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
}

// 修改申请（预留接口）
const handleEdit = (row) => {
  // 此处可打开修改弹窗，编辑后调用更新接口
  ElMessage.info('修改功能待实现')
}

// 撤回申请（带确认弹窗）
const handleWithdraw = (row) => {
  ElMessageBox.confirm(
    `确定要撤回车位 ${row.spaceNumber} 的申请吗？`,
    '确认撤回',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    // 确认撤回后调用接口
    loading.value = true
    try {
      // 预留撤回接口调用（实际开发中替换为真实接口）
      ElMessage.success('申请已撤回')
      fetchApplications() // 重新加载列表
    } catch (error) {
      console.error('撤回申请失败:', error)
      ElMessage.error('撤回申请失败')
    } finally {
      loading.value = false
    }
  }).catch(() => {
    // 用户取消撤回
  })
}

// 新增：返回上一级页面
const goBack = () => {
  window.history.back()
}
</script>

<style scoped>
.application-management {
  padding: 20px;
  background-color: #fff;
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

/* 表格样式优化 */
:deep(.el-table) {
  border: 1px solid #e8e8e8;
  border-radius: 4px;
}

:deep(.el-table__header th) {
  background-color: #f5f7fa;
  font-weight: 500;
}

/* 斑马纹样式 */
:deep(.even-row) {
  background-color: #f9f9f9;
}

:deep(.odd-row) {
  background-color: #fff;
}

/* 操作按钮样式调整 */
:deep(.el-button--text) {
  padding: 0 8px;
}

:deep(.el-button--primary.is-text) {
  color: #165dff;
}

:deep(.el-button--danger.is-text) {
  color: #f56c6c;
}

/* 加载状态优化 */
:deep(.el-loading-mask) {
  background-color: rgba(255, 255, 255, 0.7);
}
</style>