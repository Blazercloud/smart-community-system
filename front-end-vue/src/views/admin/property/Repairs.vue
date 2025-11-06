<template>
  <div class="repair-container">
    <div class="repair-list">
      <h2>报修管理</h2>

      <!-- 筛选区域 -->
<div class="filter-container" >
  <el-select v-model="filterStatus" placeholder="请选择状态" clearable style="width: 150px;"   @clear="filterStatus = null"> 
    <el-option label="待处理" :value="0" />
    <el-option label="处理中" :value="1" />
    <el-option label="已完成" :value="2" />
  </el-select>
</div>

      <el-table :data="filteredRepairRecords" style="width: 100%">
        <el-table-column prop="id" label="报修编号" width="100">
          <template #default="{ $index }">
            {{ (currentPage - 1) * pageSize + $index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="repairType" label="类型" width="120">
          <template #default="{ row }">
            {{ getRepairTypeName(row.repairType) }}
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" width="150" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="报修时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEditDialog(row)">
              编辑
            </el-button>
            <el-button link type="primary" @click="viewDetail(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

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
    <el-dialog v-model="dialogVisible" title="报修详情" width="50%">
      <div class="repair-detail" v-if="currentRepair">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="报修类型">
            {{ getRepairTypeName(currentRepair.repairType) }}
          </el-descriptions-item>
          <el-descriptions-item label="标题">
            {{ currentRepair.title }}
          </el-descriptions-item>
          <el-descriptions-item label="详细描述">
            {{ currentRepair.description }}
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            {{ currentRepair.phone }}
          </el-descriptions-item>
          <el-descriptions-item label="期望时间">
            {{ formatDateTime(currentRepair.expectedTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(currentRepair.status)">
              {{ getStatusName(currentRepair.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理人" v-if="currentRepair.assignedWorker">
            {{ currentRepair.assignedWorker }}
          </el-descriptions-item>
        </el-descriptions>
        <div class="repair-images" v-if="currentRepair.imgUrl">
          <h4>报修图片：</h4>
          <el-image
            :src="currentRepair.imgUrl"
            :preview-src-list="[currentRepair.imgUrl]"
            fit="cover"
            class="repair-image"
            style="width: 300px; height: auto;"
          />
        </div>
        <div class="repair-images" v-if="currentRepair.handleDetail">
          <h4>处理图片：</h4>
          <el-image
            :src="currentRepair.handleDetail"
            :preview-src-list="[currentRepair.handleDetail]"
            fit="cover"
            class="repair-image"
            style="width: 300px; height: auto;"
          />
        </div>
      </div>
    </el-dialog>

    <!-- 编辑弹窗 -->
    <el-dialog v-model="editDialogVisible" title="编辑报修" width="50%">
      <el-form
        ref="editForm"
        :model="editFormData"
        label-width="120px"
      >
        <el-form-item label="状态">
          <el-select v-model="editFormData.status" placeholder="请选择状态">
            <el-option label="待处理" :value="0" />
            <el-option label="处理中" :value="1" />
            <el-option label="已完成" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理人">
          <el-input v-model="editFormData.assignedWorker" placeholder="请输入处理人" />
        </el-form-item>

        <el-form-item label="处理图片上传">
          <el-upload
            action="#"
            list-type="picture-card"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleHandleImageChange"
            :on-remove="handleHandleImageRemove"
            accept="image/*"
          >
            <img v-if="editFormData.handleDetail" :src="editFormData.handleDetail" class="avatar" />
            <el-icon v-else><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip" style="margin-top: 8px; font-size: 12px; color: #666;">
            支持JPG、PNG格式，最大2MB
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">确认修改</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted ,computed} from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { imgUpload } from '@/api/community'
import { getRepairList, updateRepair } from '@/api/admin'


const filterStatus = ref(null) // 筛选状态，默认 null 表示全部

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const repairRecords = ref([])
const dialogVisible = ref(false)
const currentRepair = ref(null)
const editDialogVisible = ref(false)
const editForm = ref(null)
const editFormData = reactive({
  id: '',
  status: 0,
  assignedWorker: '',
  handleDetail: '',
  updateTime: new Date()
})
const tempHandleFile = ref(null) // 声明临时文件变量

const filteredRepairRecords = computed(() => {
  if (filterStatus.value === null) {
    return repairRecords.value
  }
  return repairRecords.value.filter(row => row.status === filterStatus.value)
})

// 加载所有报修记录
const loadRepairList = async () => {
  try {
    const response = await getRepairList({
      current: currentPage.value,
      size: pageSize.value
    })
    if (response.data && response.data.rows) {
      repairRecords.value = response.data.rows
      total.value = response.data.total || 0
    } else {
      repairRecords.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取记录失败:', error)
    ElMessage.error('获取报修记录失败')
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  loadRepairList()
}
const handleCurrentChange = (val) => {
  currentPage.value = val
  loadRepairList()
}

// 查看详情
const viewDetail = (row) => {
  currentRepair.value = row
  dialogVisible.value = true
}

// 打开编辑弹窗
const openEditDialog = (row) => {
  editFormData.id = row.id
  editFormData.status = row.status || 0
  editFormData.assignedWorker = row.assignedWorker || ''
  editFormData.handleDetail = row.handleDetail || ''
  editDialogVisible.value = true
}

// 处理图片选择事件
const handleHandleImageChange = (file) => {
  tempHandleFile.value = file.raw
  const reader = new FileReader()
  reader.onload = (e) => {
    editFormData.handleDetail = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

const handleHandleImageRemove = () => {
  editFormData.handleDetail = ''
  tempHandleFile.value = null
}

// 提交编辑
const submitEdit = async () => {
  if (!editForm.value) return

  try {
    let handleImgUrl = ''
    if (tempHandleFile.value) {
      const formDataUpload = new FormData()
      formDataUpload.append('file', tempHandleFile.value)
      const res = await imgUpload(formDataUpload)
      handleImgUrl = res.data
    }

    const updateData = {
      id: editFormData.id,
      status: parseInt(editFormData.status),
      assignedWorker: editFormData.assignedWorker,
      handleDetail: handleImgUrl || editFormData.handleDetail,
      updateTime: new Date()
    }

    await updateRepair(updateData)
    ElMessage.success('修改成功')
    editDialogVisible.value = false
    loadRepairList()
  } catch (error) {
    console.error('修改失败:', error)
    ElMessage.error('修改失败，请重试')
  }
}

// 工具函数
const getRepairTypeName = (repairType) => {
  const types = {
    plumbing: '水电维修',
    furniture: '家具维修',
    appliance: '电器维修',
    other: '其他'
  }
  return types[repairType] || repairType
}

const getStatusName = (status) => {
  const statuses = {
    0: '待处理',
    1: '处理中',
    2: '已完成'
  }
  return statuses[status] || '未知状态'
}

const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'primary',
    2: 'success'
  }
  return types[status] || ''
}

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
  loadRepairList()
})
</script>

<style scoped>
/* 样式部分保持不变 */
.repair-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.repair-list {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.repair-detail {
  padding: 10px 0;
}

.repair-images {
  margin-top: 20px;
}

.repair-image {
  border-radius: 4px;
  border: 1px solid #eee;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

::v-deep(.el-upload--picture-card) {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

::v-deep(.el-upload--picture-card i) {
  font-size: 24px;
}
</style>