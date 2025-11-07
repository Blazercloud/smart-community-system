<template>
  <div class="complaint-container">
    <div class="complaint-list">
      <h2>投诉管理</h2>

      <!-- 筛选区域 -->
      <div class="filter-container">
        <el-select v-model="filterStatus" placeholder="请选择处理状态" clearable style="width: 150px;" @clear="filterStatus = null">
          <el-option label="未处理" :value="0" />
          <el-option label="已处理" :value="1" />
        </el-select>
      </div>

      <!-- 投诉列表 -->
      <el-table :data="filteredComplaints" style="width: 100%">
        <el-table-column prop="id" label="投诉编号" width="100">
          <template #default="{ $index }">
            {{ (currentPage - 1) * pageSize + $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="content" label="投诉内容" />
        <el-table-column prop="isHandled" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.isHandled)">
              {{ getStatusName(row.isHandled) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="投诉时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewDetail(row)">查看详情</el-button>
            <el-button link type="primary" @click="openEditDialog(row)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="dialogVisible" title="投诉详情" width="50%">
      <div v-if="currentComplaint">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="投诉用户ID">
            {{ currentComplaint.userId }}
          </el-descriptions-item>
          <el-descriptions-item label="投诉内容">
            {{ currentComplaint.content }}
          </el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(currentComplaint.isHandled)">
              {{ getStatusName(currentComplaint.isHandled) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item v-if="currentComplaint.handleResult" label="处理结果">
            {{ currentComplaint.handleResult }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 编辑弹窗 -->
    <el-dialog v-model="editDialogVisible" title="处理投诉" width="50%">
      <el-form ref="editForm" :model="editFormData" label-width="120px">
        <el-form-item label="状态">
          <el-select v-model="editFormData.isHandled" placeholder="请选择状态">
            <el-option label="未处理" :value="0" />
            <el-option label="已处理" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input
            v-model="editFormData.handleResult"
            type="textarea"
            placeholder="请输入处理结果说明"
            rows="4"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getComplaintList, updateComplaint } from '@/api/admin'

// 状态筛选
const filterStatus = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const complaints = ref([])
const dialogVisible = ref(false)
const editDialogVisible = ref(false)
const currentComplaint = ref(null)

const editForm = ref(null)
const editFormData = reactive({
  id: '',
  isHandled: 0,
  handleResult: ''
})

// 过滤列表
const filteredComplaints = computed(() => {
  if (filterStatus.value === null) return complaints.value
  return complaints.value.filter(item => item.isHandled === filterStatus.value)
})

// 加载投诉列表
const loadComplaints = async () => {
  try {
    const res = await getComplaintList({
      current: currentPage.value,
      size: pageSize.value
    })
    if (res.data && res.data.rows) {
      complaints.value = res.data.rows
      total.value = res.data.total || 0
    } else {
      complaints.value = []
      total.value = 0
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('获取投诉记录失败')
  }
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  loadComplaints()
}
const handleCurrentChange = (val) => {
  currentPage.value = val
  loadComplaints()
}

// 查看详情
const viewDetail = (row) => {
  currentComplaint.value = row
  dialogVisible.value = true
}

// 打开编辑弹窗
const openEditDialog = (row) => {
  editFormData.id = row.id
  editFormData.isHandled = row.isHandled || 0
  editFormData.handleResult = row.handleResult || ''
  editDialogVisible.value = true
}

// 提交修改
const submitEdit = async () => {
  try {
    await updateComplaint({
      id: editFormData.id,
      isHandled: parseInt(editFormData.isHandled),
      handleResult: editFormData.handleResult
    })
    ElMessage.success('投诉处理成功')
    editDialogVisible.value = false
    loadComplaints()
  } catch (error) {
    console.error('修改失败:', error)
    ElMessage.error('修改失败，请重试')
  }
}

// 状态名与标签
const getStatusName = (val) => (val === 1 ? '已处理' : '未处理')
const getStatusType = (val) => (val === 1 ? 'success' : 'warning')

// 格式化时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

onMounted(() => {
  loadComplaints()
})
</script>

<style scoped>
.complaint-container {
  padding: 20px;
  background-color: #f5f7fa;
}
.complaint-list {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
}
h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
