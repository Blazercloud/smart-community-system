<template>
  <div class="notice-management">
    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        发布公告
      </el-button>
    </div>

    <el-table :data="notices" style="width: 100%">
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
      <el-table-column prop="publisher" label="发布人" width="120" />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button link type="danger" @click="handleDelete(row)">
            删除
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

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '发布公告' : '编辑公告'"
      width="60%"
    >
      <el-form
        ref="noticeForm"
        :model="formData"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入公告标题" />
        </el-form-item>

        <el-form-item label="类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择公告类型">
            <el-option label="通知" value="notice" />
            <el-option label="活动" value="activity" />
            <el-option label="规则" value="rule" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            :rows="10"
            placeholder="请输入公告内容"
          />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="draft">草稿</el-radio>
            <el-radio label="published">发布</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNoticeList, createNotice, updateNotice, deleteNotice } from '@/api/admin'

// 列表数据
const notices = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 表单数据
const dialogVisible = ref(false)
const dialogType = ref('add') // add 或 edit
const noticeForm = ref(null)

const formData = reactive({
  id: '',
  title: '',
  type: '',
  content: '',
  status: 'draft'
})

const rules = {
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' },
    { min: 2, max: 50, message: '标题长度在 2 到 50 个字符', trigger: 'blur' }
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

// 加载公告列表
const loadNotices = async () => {
  try {
    const response = await getNoticeList({
      page: currentPage.value,
      size: pageSize.value
    })
    notices.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    console.error('获取公告列表失败:', error)
  }
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  loadNotices()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadNotices()
}

// 新增公告
const handleAdd = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 编辑公告
const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除公告
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该公告吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteNotice(row.id)
    ElMessage.success('删除成功')
    loadNotices()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除公告失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!noticeForm.value) return

  try {
    await noticeForm.value.validate()
    
    if (dialogType.value === 'add') {
      await createNotice(formData)
      ElMessage.success('发布成功')
    } else {
      await updateNotice(formData)
      ElMessage.success('更新成功')
    }

    dialogVisible.value = false
    loadNotices()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请重试')
  }
}

// 重置表单
const resetForm = () => {
  if (noticeForm.value) {
    noticeForm.value.resetFields()
  }
  Object.assign(formData, {
    id: '',
    title: '',
    type: '',
    content: '',
    status: 'draft'
  })
}

// 工具函数
const getNoticeTypeName = (type) => {
  const types = {
    notice: '通知',
    activity: '活动',
    rule: '规则',
    other: '其他'
  }
  return types[type] || type
}

const getStatusName = (status) => {
  const statuses = {
    draft: '草稿',
    published: '已发布',
    expired: '已过期'
  }
  return statuses[status] || status
}

const getStatusType = (status) => {
  const types = {
    draft: 'info',
    published: 'success',
    expired: 'danger'
  }
  return types[status] || ''
}

onMounted(() => {
  loadNotices()
})
</script>

<style scoped>
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