<template>
  <div class="visitor-management">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd" :loading="loading">
        <el-icon><Plus /></el-icon>
        添加访客
      </el-button>

      <!-- 搜索框 -->
      <el-input
        v-model="searchKeyword"
        placeholder="请输入姓名或来访目的"
        style="width: 250px; margin-left: 20px;"
        @keyup.enter="loadVisitors"
      />

      <!-- 类型筛选 -->
      <el-select
        v-model="filterType"
        placeholder="选择类型"
        style="width: 150px; margin-left: 10px;"
        @change="loadVisitors"
      >
        <el-option label="全部类型" value="" />
        <el-option label="亲友" value="friend" />
        <el-option label="快递/外卖" value="delivery" />
        <el-option label="维修/装修" value="fix" />
        <el-option label="其他" value="other" />
      </el-select>
    </div>

    <!-- 访客表格 -->
    <el-table :data="visitorList" style="width: 100%" :loading="loading">
      <el-table-column label="序号" width="80">
        <template #default="{ $index }">
          {{ ($index + 1) + (currentPage - 1) * pageSize }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="visitorType" label="类型" width="120">
        <template #default="{ row }">
          {{ VISITOR_TYPE_MAP[row.visitorType] || row.visitorType }}
        </template>
      </el-table-column>
      <el-table-column prop="purpose" label="来访目的" />
      <el-table-column prop="visitTime" label="到访时间" width="180">
        <template #default="{ row }">{{ formatTime(row.visitTime) }}</template>
      </el-table-column>
      <el-table-column prop="relatedUser" label="关联业主" width="120">
        <template #default="{ row }">{{ row.relatedUser || '未关联' }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusName(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
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
        :disabled="loading"
      />
    </div>

    <!-- 添加访客弹窗 -->
    <el-dialog v-model="dialogVisible" title="添加访客" width="40%" :close-on-click-modal="false">
      <el-form ref="visitorForm" :model="formData" :rules="rules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入访客姓名" />
        </el-form-item>
        <el-form-item label="类型" prop="visitorType">
          <el-select v-model="formData.visitorType" placeholder="请选择类型">
            <el-option label="亲友" value="friend" />
            <el-option label="快递/外卖" value="delivery" />
            <el-option label="维修/装修" value="fix" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="来访目的" prop="purpose">
          <el-input v-model="formData.purpose" placeholder="请输入来访目的" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="0">未到访</el-radio>
            <el-radio :label="1">已到访</el-radio>
            <el-radio :label="2">已离开</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="关联业主ID" prop="relatedUser">
          <el-input-number v-model="formData.relatedUser" placeholder="可不填写" :min="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" :loading="loading">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="loading">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getVisitorList, addVisitor, deleteVisitor } from '@/api/admin.js'

// 表格与分页
const visitorList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

// 弹窗表单
const dialogVisible = ref(false)
const visitorForm = ref(null)
const formData = reactive({
  id: '',
  name: '',
  visitorType: '',
  purpose: '',
  status: 0,
  relatedUser: null
})

// 表单规则
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  visitorType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  purpose: [{ required: true, message: '请输入来访目的', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 类型映射
const VISITOR_TYPE_MAP = {
  friend: '亲友',
  delivery: '快递/外卖',
  fix: '维修/装修',
  other: '其他'
}

// 状态映射
const STATUS_MAP = {
  0: { name: '未到访', type: 'info' },
  1: { name: '已到访', type: 'success' },
  2: { name: '已离开', type: 'warning' }
}
const getStatusName = (status) => STATUS_MAP[status]?.name || '未知'
const getStatusType = (status) => STATUS_MAP[status]?.type || ''

// 搜索与类型筛选
const searchKeyword = ref('')
const filterType = ref('')

// 加载访客列表
const loadVisitors = async () => {
  loading.value = true
  try {
    const res = await getVisitorList({
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchKeyword.value,
      type: filterType.value
    })
    visitorList.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (err) {
    visitorList.value = []
    total.value = 0
    ElMessage.error('获取访客列表失败')
  } finally {
    loading.value = false
  }
}

// 分页
const handleSizeChange = (val) => { pageSize.value = val; currentPage.value = 1; loadVisitors() }
const handleCurrentChange = (val) => { currentPage.value = val; loadVisitors() }

// 添加访客
const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

// 删除访客
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该访客吗？', '提示', { type: 'warning' })
    loading.value = true
    await deleteVisitor(row.id)
    ElMessage.success('删除成功')
    loadVisitors()
  } catch (err) {
    if (err.name !== 'CanceledError') ElMessage.error('删除失败')
  } finally {
    loading.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  await visitorForm.value.validate()
  loading.value = true
  try {
    await addVisitor(formData)
    ElMessage.success('添加成功')
    dialogVisible.value = false
    loadVisitors()
  } catch (err) {
    ElMessage.error('添加失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (visitorForm.value) visitorForm.value.resetFields()
  formData.id = ''
  formData.name = ''
  formData.visitorType = ''
  formData.purpose = ''
  formData.status = 0
  formData.relatedUser = null
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  return new Date(timeStr).toLocaleString('zh-CN', { hour12: false })
}

onMounted(() => loadVisitors())
</script>

<style scoped>
.visitor-management {
  padding: 20px;
}
.operation-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
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
