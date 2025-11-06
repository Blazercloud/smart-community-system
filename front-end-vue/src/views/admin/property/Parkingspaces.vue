<template>
  <div class="parking-space-management">
    <!-- 操作栏：与公告页面保持一致的布局和间距 -->
    <div class="operation-bar" v-if="activeTab === 'parkingSpaces'">
      <el-button type="primary" @click="handleAddParkingSpace" :loading="loading">
        <el-icon>
          <Plus />
        </el-icon>
        添加车位
      </el-button>

      <!-- 搜索框：调整宽度和间距，与公告页面统一 -->
      <el-input v-model="searchKeyword" placeholder="请输入车牌号搜索" style="width: 250px; margin-left: 20px;" clearable
        @clear="loadParkingSpaces" @keyup.enter="loadParkingSpaces" />

      <!-- 查询按钮：与公告页面样式统一 -->
      <el-button type="primary" @click="loadParkingSpaces" :loading="loading" style="margin-left: 20px;">
        <el-icon>
          <Search />
        </el-icon>
        查询
      </el-button>
    </div>
    <!-- 操作栏：与公告页面保持一致的布局和间距 -->
    <div class="operation-bar" v-if="activeTab === 'applications'">
   
         <!-- 状态筛选 -->
      <el-select v-model="status" placeholder="请选择状态" style="width: 150px; " @change="loadNotices">
        <el-option label="全部" value="" />
        <el-option label="待审批" value="0" />
        <el-option label="已同意" value="1" />
        <el-option label="已退回" value="2" />
      </el-select>

      <!-- 查询按钮：与公告页面样式统一 -->
      <el-button type="primary" @click="loadApplications" :loading="loading" style="margin-left: 20px;">
        <el-icon>
          <Search />
        </el-icon>
        查询
      </el-button>
    </div>

    <!-- 标签页：简化样式，与公告页面的导航风格对齐 -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange" class="main-tabs">
      <!-- 车位管理标签页 -->
      <el-tab-pane label="车位管理" name="parkingSpaces">
        <div class="table-container">
          <!-- 表格：添加序号列，与公告页面保持一致 -->
          <el-table :data="parkingSpaces" border v-loading="loading" style="width: 100%;"
            :row-class-name="tableRowClassName">
            <!-- 连续序号列 -->
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
                <!-- 状态标签样式与公告页面统一 -->
                <el-tag :type="scope.row.status === '空闲' ? 'success' : 'warning'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="userName" label="车主姓名" width="300" align="center" />
            <el-table-column prop="carNumber" label="车牌号" width="120" align="center" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="scope">
                <el-button link type="primary" @click="handleEdit(scope.row)" :loading="loading">
                  编辑
                </el-button>
                <el-button link type="danger" @click="handleDelete(scope.row)" :loading="loading">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页：与公告页面布局一致（去掉jumper） -->
          <div class="pagination">
            <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total"
              :page-sizes="[10, 20, 30, 50]" layout="total, sizes, prev, pager, next" @size-change="handleSizeChange"
              @current-change="handleCurrentChange" :disabled="loading" />
          </div>
        </div>
        
      </el-tab-pane>

      <!-- 申请管理标签页 -->
      <el-tab-pane label="申请管理" name="applications">
        <div class="table-container">
          <el-table :data="applications" border v-loading="loading" style="width: 100%;"
            :row-class-name="tableRowClassName">
            <el-table-column label="序号" width="60">
              <template #default="{ $index }">
                {{ (appCurrentPage - 1) * appPageSize + $index + 1 }}
              </template>
            </el-table-column>

            <el-table-column prop="username" label="用户名" width="300" align="center" />
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
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="scope">
                <el-button link type="success" :disabled="scope.row.status !== '0'" @click="handleApprove(scope.row)"
                  :loading="loading">
                  同意
                </el-button>
                <el-button link type="danger" :disabled="scope.row.status !== '0'" @click="handleReject(scope.row)"
                  :loading="loading">
                  退回
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination">
            <el-pagination v-model:current-page="appCurrentPage" v-model:page-size="appPageSize" :total="appTotal"
              :page-sizes="[10, 20, 30, 50]" layout="total, sizes, prev, pager, next" @size-change="handleAppSizeChange"
              @current-change="handleAppCurrentChange" :disabled="loading" />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 车位编辑对话框：调整宽度和样式，与公告弹窗统一 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '添加车位' : '编辑车位'" width="60%"
      :close-on-click-modal="false" class="parking-dialog">
      <el-form ref="parkingSpaceFormRef" :model="parkingSpaceForm" :rules="rules" label-width="100px">
        <el-form-item label="车位编号" prop="spaceNumber">
          <el-input v-model="parkingSpaceForm.spaceNumber" placeholder="请输入车位编号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="parkingSpaceForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="空闲" value="空闲" />
            <el-option label="已占用" value="已占用" />
          </el-select>
        </el-form-item>
        <el-form-item label="车主姓名" prop="userName">
          <el-input v-model.number="parkingSpaceForm.userName" placeholder="请输入车主姓名（可选）" />
        </el-form-item>
        <el-form-item label="车牌号" prop="carNumber">
          <el-input v-model="parkingSpaceForm.carNumber" placeholder="请输入车牌号（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" :loading="loading">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="loading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import {
  getParkingSpaces,
  updateParkingSpace,
  addParkingSpace,
  deleteParkingSpace,
  getParkingApplications,
  updateParkingApplicationStatus
} from '@/api/admin.js'

// 搜索和状态筛选
const status = ref('')


// 车位管理相关数据
const activeTab = ref('parkingSpaces')
const parkingSpaces = ref([])
const loading = ref(false)  // 只保留一个loading声明
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchKeyword = ref('')

// 申请管理相关数据
const applications = ref([])
const appCurrentPage = ref(1)
const appPageSize = ref(10)
const appTotal = ref(0)
// 移除重复的loading声明

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add') // 'add' 或 'edit'
const parkingSpaceForm = ref({
  id: '',
  spaceNumber: '',
  status: '空闲',
  userName: '',
  searchKeyword: ''
})

// 表单验证规则
const rules = {
  spaceNumber: [
    { required: true, message: '请输入车位编号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 表单引用
const parkingSpaceFormRef = ref(null)

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
const tableRowClassName = ({ row, rowIndex }) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
}

// 处理标签页切换
const handleTabChange = (tabName) => {
  if (tabName === 'parkingSpaces') {
    loadParkingSpaces()
  } else if (tabName === 'applications') {
    loadApplications()
  }
}

// 加载车位数据
const loadParkingSpaces = async () => {
  loading.value = true
  try {
    // 构造符合后端要求的参数（id和carNumber用于搜索）
    const data = {
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      // 如果有搜索框绑定的是carNumber，直接传递；如果是其他字段（如keyword），需要映射
      carNumber: searchKeyword.value || undefined  // 假设搜索框v-model绑定的是searchKeyword
      // 如果需要根据id搜索，可以添加id参数：id: xxx
    }

    const res = await getParkingSpaces(data)  // 传递参数对象

    if (res.code === 200) {
      // 后端返回格式是PageResult，包含rows和total
      parkingSpaces.value = res.data.rows || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.message || '加载车位数据失败')
      parkingSpaces.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('加载车位数据失败:', error)
    ElMessage.error('加载车位数据失败')
    parkingSpaces.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 处理分页变化
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1 // 页码重置为1
  loadParkingSpaces()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadParkingSpaces()
}

// 加载申请数据
const loadApplications = async () => {
  loading.value = true
  try {
    const res = await getParkingApplications({
      currentPage: appCurrentPage.value,
      pageSize: appPageSize.value,
      status: status.value,
    })

    if (res.code === 200) {
      applications.value = res.data.rows|| []
      appTotal.value = res.data.total|| 0
    } else {
      ElMessage.error(res.message || '加载申请数据失败')
      applications.value = []
      appTotal.value = 0
    }
  } catch (error) {

    applications.value = []
    appTotal.value = 0
    console.error('加载申请数据失败:', error)
    ElMessage.error('加载申请数据失败')
   
  } finally {
    loading.value = false
  }
}

// 处理申请分页变化
const handleAppSizeChange = (val) => {
  appPageSize.value = val
  appCurrentPage.value = 1
  loadApplications()
}

const handleAppCurrentChange = (val) => {
  appCurrentPage.value = val
  loadApplications()
}

// 添加车位
const handleAddParkingSpace = () => {
  dialogType.value = 'add'
  parkingSpaceForm.value = {
    id: '',
    spaceNumber: '',
    status: '空闲',
    userName: '',
    carNumber: ''
  }
  dialogVisible.value = true
}

// 编辑车位
const handleEdit = (row) => {
  dialogType.value = 'edit'
  parkingSpaceForm.value = { ...row }
  dialogVisible.value = true
}

// 删除车位
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除车位 ${row.spaceNumber} 吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await deleteParkingSpace(row.id)
      if (res.code === 200) {
        ElMessage.success('删除成功')
        loadParkingSpaces()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 提交表单
const submitForm = () => {
  parkingSpaceFormRef.value.validate(async (valid) => {
    if (!valid) return

    try {
      let res
      if (dialogType.value === 'add') {
        res = await addParkingSpace(parkingSpaceForm.value)
      } else {
        res = await updateParkingSpace(parkingSpaceForm.value)
      }

      if (res.code === 200) {
        ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
        dialogVisible.value = false
        loadParkingSpaces()
      } else {
        ElMessage.error(res.message || '操作失败')
      }
    } catch (error) {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    } finally {
      loading.value = false
    }
  })
}

// 处理申请同意
const handleApprove = (row) => {
  ElMessageBox.confirm(
    `确定要同意用户 ${row.username || row.userId} 的车位申请吗？`,
    '确认同意',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      loading.value = true
      const res = await updateParkingApplicationStatus(row.id, { status: '1' })
      if (res.code === 200) {
        ElMessage.success('已同意申请')
        loadApplications()
      } else {
        ElMessage.error(res.message || '操作失败')
      }
    } catch (error) {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    } finally {
      loading.value = false
    }
  })
}

// 处理申请退回
const handleReject = (row) => {
  ElMessageBox.confirm(
    `确定要退回用户 ${row.username || row.userId} 的车位申请吗？`,
    '确认退回',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      loading.value = true
      const res = await updateParkingApplicationStatus(row.id, { status: '2' })
      if (res.code === 200) {
        ElMessage.success('已退回申请')
        loadApplications()
      } else {
        ElMessage.error(res.message || '操作失败')
      }
    } catch (error) {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    } finally {
      loading.value = false
    }
  })
}

// 初始化加载
onMounted(() => {
  loadParkingSpaces()
})
</script>

<style scoped>
/* 统一容器样式与公告页面一致 */
.parking-space-management {
  padding: 20px;
  background-color: #fff;
  /* 与公告页面背景统一 */
}

/* 操作栏样式：与公告页面保持一致 */
.operation-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

/* 表格容器样式 */
.table-container {
  width: 100%;
  margin: 0 auto;
}

/* 分页样式：与公告页面一致 */
.pagination {
  margin-top: 20px;
  text-align: right;
}

/* 对话框样式统一 */
.parking-dialog .el-form-item {
  margin-bottom: 16px;
}

.dialog-footer {
  padding: 20px 0;
  text-align: right;
}

/* 表格样式调整：与公告页面统一 */
:deep(.el-table) {
  border: 1px solid #e8e8e8;
  border-radius: 4px;
}

:deep(.el-table__header th) {
  background-color: #f5f7fa;
  font-weight: 500;
  cursor: default;
}

/* 操作按钮样式统一：与公告页面的链接按钮一致 */
:deep(.el-button--text) {
  padding: 0 8px;
}

:deep(.el-button--primary.is-text) {
  color: #165dff;
}

:deep(.el-button--danger.is-text) {
  color: #f56c6c;
}

/* 标签页样式简化：与公告页面的导航风格一致 */
:deep(.el-tabs__nav) {
  padding: 5px 20px;
}

:deep(.el-tabs__active-bar) {
  background-color: #165dff;
}

:deep(.el-tabs__item.is-active) {
  color: #165dff;
}

/* 斑马纹样式 */
:deep(.even-row) {
  background-color: #f9f9f9;
}

:deep(.odd-row) {
  background-color: #fff;
}
</style>