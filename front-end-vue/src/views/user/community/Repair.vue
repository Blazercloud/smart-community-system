<template>
  <div class="repair-container">
    <div class="repair-form">
      <h2>报事报修</h2>
      <el-form
        ref="repairForm"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="报修类型" prop="repairType">
          <el-select v-model="formData.repairType" placeholder="请选择报修类型">
            <el-option label="水电维修" value="plumbing" />
            <el-option label="家具维修" value="furniture" />
            <el-option label="电器维修" value="appliance" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>

        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入报修标题" />
        </el-form-item>

        <el-form-item label="详细描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请详细描述问题"
          />
        </el-form-item>

        <!-- 单图上传组件 -->
        <el-form-item label="图片上传">
          <el-upload
            action="#"
            list-type="picture-card"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleImageChange"
            :on-remove="handleImageRemove"
            accept="image/*"
          >
            <img v-if="formData.imgUrl" :src="formData.imgUrl" class="avatar" />
            <el-icon v-else><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip" style="margin-top: 8px; font-size: 12px; color: #666;">
            支持JPG、PNG格式，最大2MB
          </div>
        </el-form-item>

        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" />
        </el-form-item>

        <el-form-item label="期望时间" prop="expectedTime">
          <el-date-picker
            v-model="formData.expectedTime"
            type="datetime"
            placeholder="选择期望维修时间"
            :disabled-date="disabledDate"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">提交报修</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="repair-list">
      <h2>报修记录</h2>
      <el-table :data="repairRecords" style="width: 100%">
        <el-table-column prop="id" label="报修编号" width="100" >
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
        <el-table-column prop="createTime" label="报修时间" width="180" >
              <template #default="{ row }">
          {{ formatDateTime(row.createTime) }}
          </template>
      </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
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
            {{ currentRepair.expectedTime }}
          </el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(currentRepair.status)">
              {{ getStatusName(currentRepair.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理记录" v-if="currentRepair.handleDetail">
            {{ currentRepair.handleDetail }}
          </el-descriptions-item>
        </el-descriptions>
        <!-- 单图展示逻辑 -->
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
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { submitRepair, getRepairList, imgUpload } from '@/api/community'
import { useUserStore } from '@/stores/user'

// 用户状态（获取当前登录用户ID）
const userStore = useUserStore()

// 表单数据：与后端字段完全匹配
const formData = reactive({
  repairType: '',
  title: '',
  description: '',
  phone: '',
  expectedTime: '',
  imgUrl: '', // 单图URL
  userId: userStore.userInfo.id // 关联当前用户
})

// 表单验证规则
const rules = {
  repairType: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入报修标题', trigger: 'blur' }],
  description: [{ required: true, message: '请输入详细描述', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  expectedTime: [{ required: true, message: '请选择期望时间', trigger: 'change' }]
}

const repairForm = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const repairRecords = ref([])
const dialogVisible = ref(false)
const currentRepair = ref(null)
const tempFile = ref(null) // 临时存储选中的文件

// 图片选择事件：单图模式
const handleImageChange = (file) => {
  tempFile.value = file.raw // 暂存文件对象
  formData.imgUrl = URL.createObjectURL(file.raw) // 本地预览
}

// 移除图片
const handleImageRemove = () => {
  formData.imgUrl = ''
  tempFile.value = null
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7 // 禁止选择今天之前的日期
}

// 提交报修：先上传图片再提交表单
const submitForm = async () => {
  if (!repairForm.value) return

  try {
    await repairForm.value.validate()

    // 上传图片到MinIO
    if (tempFile.value) {
      const formDataUpload = new FormData()
      formDataUpload.append('file', tempFile.value)
      const res = await imgUpload(formDataUpload)
      formData.imgUrl = res.data // 更新为服务器端URL
    }

    // 提交报修表单
    await submitRepair(formData)
    ElMessage.success('报修提交成功')

    // 重置并刷新
    resetForm()
    loadRepairList()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请重试')
  }
}

// 重置表单
const resetForm = () => {
  if (repairForm.value) repairForm.value.resetFields()
  formData.imgUrl = ''
  tempFile.value = null
}

// 加载报修记录（带分页）
const loadRepairList = async () => {
  try {
    const response = await getRepairList({
      current: currentPage.value,
      size: pageSize.value,
      userId: userStore.userInfo.id
    })
    
    // 修复：确保response.data存在且不为null
    if (response.data && response.data.rows) {
      repairRecords.value = response.data.rows
      total.value = response.data.total || 0
    } else {
      // 处理无数据情况
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
    2: '已完成',
    3: '已取消'
  }
  return statuses[status] || '未知状态'
}

const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'info'
  }
  return types[status] || ''
}

// 工具函数：格式化日期时间
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
.repair-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.repair-form, .repair-list {
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

/* 上传组件样式优化 */
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