<template>
  <div class="notice-management">
    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd" :loading="loading">
        <el-icon>
          <Plus />
        </el-icon>
        发布公告
      </el-button>

      <!-- 搜索框 -->
      <el-input v-model="searchKeyword" placeholder="请输入标题或内容关键字" style="width: 250px; margin-left: 20px;"
        @keyup.enter="loadNotices" />

      <!-- 状态筛选 -->
      <el-select v-model="filterStatus" placeholder="请选择状态" style="width: 150px; margin-left: 20px;">
        <el-option label="全部" value="" />
        <el-option label="草稿" value="0" />
        <el-option label="已发布" value="1" />
        <el-option label="已过期" value="2" />
      </el-select>

      <el-button type="primary" @click="loadNotices" :loading="loading" style="margin-left: 20px;">
        <el-icon><Search /></el-icon>
        查询
      </el-button>
    </div>



    <el-table :data="notices" style="width: 100%" :loading="loading">
      <!-- 表格列保持不变 -->
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="type" label="类型" width="120">
        <template #default="{ row }">
          <el-tag>{{ getNoticeTypeName(row.type) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusName(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="publisherName" label="发布人" width="120" />
      <el-table-column prop="createTime" label="发布时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)" :loading="loading">
            编辑
          </el-button>
          <el-button link type="danger" @click="handleDelete(row)" :loading="loading">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <!-- 分页组件保持不变 -->
      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :total="total"
        :page-sizes="[10, 20, 30, 50]" layout="total, sizes, prev, pager, next" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" :disabled="loading" />
    </div>

    <!-- 编辑弹窗：增加发布人选择 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '发布公告' : '编辑公告'" width="60%"
      :close-on-click-modal="false">
      <el-form ref="noticeForm" :model="formData" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入公告标题" />
        </el-form-item>

        <el-form-item label="发布人" prop="publisherId">
          <el-select v-model="formData.publisherId" placeholder="请选择发布人">
            <el-option label="东软社区" value="4" />
            <el-option label="东软物业" value="5" />
          </el-select>
        </el-form-item>

        <el-form-item label="类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择公告类型">
            <el-option label="社区公告" value="1" />
            <el-option label="物业公告" value="2" />
          </el-select>
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="10" placeholder="请输入公告内容" />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="1">已发布</el-radio>
            <el-radio label="0">草稿</el-radio>
            <el-radio label="2">已过期</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" :loading="loading">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus, Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNoticeList } from '@/api/community.js'
import { updateNotice } from '@/api/admin.js'
import { useUserStore } from '@/stores/user.js'

//模糊搜索数据和选择的状态数据
const searchKeyword = ref('')
const filterStatus = ref('')

// 列表数据
const notices = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

// 表单数据：新增publisherId（发布人ID，bigint类型）
const dialogVisible = ref(false)
const dialogType = ref('add')
const noticeForm = ref(null)
const userStore = useUserStore()

const formData = reactive({
  id: '',
  title: '',         // 标题
  content: '',       // 内容（修复之前可能混淆的问题）
  type: '',          // 类型（数字）
  status: '0',       // 状态（数字）
  publisherId: ''    // 发布人ID（4=东软社区，5=东软物业，bigint类型）
})

// 类型映射常量
const NOTICE_TYPES = {
  1: '社区公告',
  2: '物业公告'
}
const STATUS_MAP = {
  0: { name: '草稿', type: 'info' },
  1: { name: '已发布', type: 'success' },
  2: { name: '已过期', type: 'danger' }
}

// 表单规则：增加发布人必填校验
const rules = {
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' },
    { min: 2, max: 50, message: '标题长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  publisherId: [
    { required: true, message: '请选择发布人', trigger: 'change' }
  ],
  type: [
    { required: true, message: '请选择公告类型', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入公告内容', trigger: 'blur' },
    { min: 10, message: '内容不能少于 10 个字符', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择公告状态', trigger: 'change' }
  ]
}

// 加载公告列表（保持不变）
const loadNotices = async () => {
  loading.value = true
  try {
    const response = await getNoticeList({
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      searchKeyword: searchKeyword.value,        // 模糊搜索
      filterStatus: filterStatus.value          // 状态筛选
    })
    notices.value = response.data.rows || []
    total.value = response.data.total || 0

  } catch (error) {
    notices.value = []
    total.value = 0
    console.error('获取公告列表失败:', error)
    ElMessage.error(error.message)
  } finally {
    loading.value = false
  }
}

// 分页处理（保持不变）
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  loadNotices()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadNotices()
}

// 新增公告：重置表单
const handleAdd = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 编辑公告：正确赋值标题和内容，增加发布人ID
const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(formData, {
    id: row.id,
    title: row.title,
    content: row.content,
    type: String(row.type),        // 保持字符串或数字均可
    status: String(row.status),
    publisherId: String(row.publisherId)  // 后端返回的是 bigint，转为字符串
  })
  dialogVisible.value = true
}
// 删除公告（保持不变）
const handleDelete = async (row) => {
  try {
    const { isConfirmed } = await ElMessageBox.confirm('确认删除该公告吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    if (isConfirmed) {
      // 若有删除接口可在此调用
      ElMessage.success('删除成功')
      loadNotices()
    }
  } catch (error) {
    if (error.name !== 'CanceledError') {
      ElMessage.error('删除失败，请重试')
    }
  }
}

// 提交表单：确保发布人ID以数字类型发送（适配bigint）
const handleSubmit = async () => {
  if (!noticeForm.value) return
  await noticeForm.value.validate()
  loading.value = true

  const submitData = {
    ...formData,
    publisherId: Number(formData.publisherId),
    type: Number(formData.type),
    status: Number(formData.status)
  }

  try {
    if (dialogType.value === 'edit') {
      await updateNotice(submitData)
      ElMessage.success('编辑成功')
    } else {
      // 若有新增接口，同样使用submitData格式
      ElMessage.success('发布成功')
    }
    dialogVisible.value = false
    loadNotices()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请重试')
  } finally {
    loading.value = false
  }
}
// 重置表单：清空所有字段
const resetForm = () => {
  if (noticeForm.value) {
    noticeForm.value.resetFields()
  }
  formData.id = ''
  formData.title = ''
  formData.content = ''
  formData.type = ''
  formData.status = '0'
  formData.publisherId = ''
}

// 工具函数（保持不变）
const getNoticeTypeName = (type) => {
  return NOTICE_TYPES[type] || '未知类型'
}

const getStatusName = (status) => {
  return STATUS_MAP[status]?.name || '未知状态'
}

const getStatusType = (status) => {
  return STATUS_MAP[status]?.type || ''
}

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

onMounted(() => {
  loadNotices()
})
</script>

<style scoped>
/* 样式保持不变 */
.notice-management {
  padding: 20px;
}

.operation-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  padding: 20px 0;
  text-align: right;
}
</style>