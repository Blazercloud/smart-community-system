<template>
  <div class="parking-list">
    <div class="page-header">
  <h2>车位列表</h2>
  
  <div style="display: flex; gap: 10px; margin-top: 10px;">
    <el-button type="primary" @click="handleToParkApply">查看我的申请</el-button>
    <el-button 
      type="primary" 
      @click="handleToggleMyParking"
      :loading="loading"
    >
      {{ showMyParking ? '查看全部车位' : '查看我的车位' }}
    </el-button>
  </div>
</div>


    <!-- 车位列表表格：与管理页表格样式统一 -->
    <el-table 
      :data="parkingList" 
      border 
      style="width: 100%"
      v-loading="loading"
      :row-class-name="tableRowClassName"
    >
      <!-- 连续序号列：与管理页保持一致 -->
      <el-table-column label="序号" width="60">
        <template #default="{ $index }">
          {{ (currentPage - 1) * pageSize + $index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="spaceNumber" label="车位编号" width="150" align="center">
        <template #default="scope">
          <el-tag type="primary">{{ scope.row.spaceNumber }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120" align="center">
        <template #default="scope">
          <el-tag 
            :type="scope.row.status === '空闲' ? 'success' : 'warning'"
          >
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="车主" width="300" align="center" />
      <el-table-column prop="carNumber" label="车牌号" width="120" align="center" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="scope">
          <el-button 
            size="small" 
            type="success" 
            @click="handleApply(scope.row)"
            :disabled="scope.row.status !== '空闲'"
          >
            申请此车位
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页栏：与管理页样式、布局完全一致 -->
    <div class="pagination">
      <el-pagination 
        v-model:current-page="currentPage" 
        v-model:page-size="pageSize" 
        :total="total"
        :page-sizes="[10, 20, 30, 50]" 
        layout="total, sizes, prev, pager, next" 
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange" 
        :disabled="loading"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getParkingSpaces } from '@/api/admin.js'
import { useUserStore } from '@/stores/user.js'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

// 状态管理：与管理页保持一致的命名规范
const parkingList = ref([])
const searchNumber = ref('')
const showMyParking = ref(false)
const loading = ref(false) // 统一的加载状态

// 分页参数：与管理页保持一致的参数名和默认值
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 页面初始化加载
onMounted(() => {
  fetchParkingList()
})

// 加载车位列表：与管理页loadParkingSpaces逻辑保持一致
const fetchParkingList = async () => {
  loading.value = true
  try {
    // 构造参数：与管理页参数传递风格一致
    const params = {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      // 我的车位筛选条件
      id: showMyParking.value ? userStore.userInfo?.id : undefined,
      // 编号搜索条件：与管理页搜索参数处理方式一致
      searchNumber: searchNumber.value.trim() || undefined
    }

    const res = await getParkingSpaces(params)

    if (res.code === 200) {
      parkingList.value = res.data.rows || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取车位列表失败')
      parkingList.value = []
      total.value = 0
    }
  } catch (err) {
    console.error('查询车位失败', err)
    ElMessage.error('加载车位数据失败')
    parkingList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 切换"我的车位/全部车位"视图：与管理页切换逻辑一致
const handleToggleMyParking = () => {
  showMyParking.value = !showMyParking.value
  currentPage.value = 1 // 切换视图时重置页码
  fetchParkingList()
}


// 分页大小变化：与管理页处理逻辑一致
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1 // 页大小变化时重置页码
  fetchParkingList()
}

// 页码变化：与管理页处理逻辑一致
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchParkingList()
}

// 申请车位：添加确认提示，与管理页操作风格一致
const handleApply = (row) => {
  if (!userStore.userInfo) {
    ElMessage.warning('请先登录')
    return
  }

  ElMessageBox.confirm(
    `确定要申请车位 ${row.spaceNumber} 吗？`,
    '确认申请',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }
  ).then(() => {
    router.push({
      path: '/user/community/parking/apply', 
      query: { spaceNumber: row.spaceNumber }
    })
  }).catch(() => {
    // 用户取消申请
  })
}

const handleToParkApply = () => {
  router.push('/user/community/parking/apply/manage')
}

// 表格斑马纹样式：与管理页保持一致
const tableRowClassName = ({ row, rowIndex }) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
}
</script>

<style scoped>
/* 统一容器样式与管理页一致 */
.parking-list {
  padding: 20px;
  background-color: #fff;
}

/* 操作栏样式：与管理页保持一致 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

/* 分页样式：与管理页完全一致 */
.pagination {
  margin-top: 20px;
  text-align: right;
}

/* 表格样式调整：与管理页统一 */
:deep(.el-table) {
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  margin-bottom: 10px;
}

:deep(.el-table__header th) {
  background-color: #f5f7fa;
  font-weight: 500;
  cursor: default;
}

/* 按钮样式统一：与管理页一致 */
:deep(.el-button--primary) {
  margin-left: 0;
}

/* 斑马纹样式：与管理页统一 */
:deep(.even-row) {
  background-color: #f9f9f9;
}

:deep(.odd-row) {
  background-color: #fff;
}

/* 加载状态优化 */
:deep(.el-loading-mask) {
  background-color: rgba(255, 255, 255, 0.7);
}
</style>