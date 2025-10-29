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
        <el-form-item label="报修类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择报修类型">
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

        <el-form-item label="图片上传">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-change="handleImageChange"
            :on-remove="handleImageRemove"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
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
        <el-table-column prop="id" label="报修编号" width="100" />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            {{ getRepairTypeName(row.type) }}
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="报修时间" width="180" />
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
            {{ getRepairTypeName(currentRepair.type) }}
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
          <el-descriptions-item label="处理记录" v-if="currentRepair.processNote">
            {{ currentRepair.processNote }}
          </el-descriptions-item>
        </el-descriptions>
        <div class="repair-images" v-if="currentRepair.images?.length">
          <h4>报修图片：</h4>
          <el-image
            v-for="(img, index) in currentRepair.images"
            :key="index"
            :src="img"
            :preview-src-list="currentRepair.images"
            fit="cover"
            class="repair-image"
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
import { submitRepair, getRepairList } from '@/api/community'

// 表单数据
const formData = reactive({
  type: '',
  title: '',
  description: '',
  phone: '',
  expectedTime: '',
  images: []
})

// 表单验证规则
const rules = {
  type: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
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

// 图片处理
const handleImageChange = (file) => {
  formData.images.push(file.raw)
}

const handleImageRemove = (file) => {
  const index = formData.images.indexOf(file.raw)
  if (index !== -1) {
    formData.images.splice(index, 1)
  }
}

// 禁用过去的日期
const disabledDate = (time) => {
  return time.getTime() < Date.now()
}

// 提交表单
const submitForm = async () => {
  if (!repairForm.value) return
  
  try {
    await repairForm.value.validate()
    
    // 创建 FormData 对象上传图片
    const formDataToSubmit = new FormData()
    Object.keys(formData).forEach(key => {
      if (key === 'images') {
        formData.images.forEach(img => {
          formDataToSubmit.append('images', img)
        })
      } else {
        formDataToSubmit.append(key, formData[key])
      }
    })

    await submitRepair(formDataToSubmit)
    ElMessage.success('报修提交成功')
    resetForm()
    loadRepairList()
  } catch (error) {
    console.error('提交报修失败:', error)
    ElMessage.error('提交失败，请重试')
  }
}

// 重置表单
const resetForm = () => {
  if (repairForm.value) {
    repairForm.value.resetFields()
    formData.images = []
  }
}

// 加载报修记录
const loadRepairList = async () => {
  try {
    const response = await getRepairList({
      page: currentPage.value,
      size: pageSize.value
    })
    repairRecords.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    console.error('获取报修记录失败:', error)
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
const getRepairTypeName = (type) => {
  const types = {
    plumbing: '水电维修',
    furniture: '家具维修',
    appliance: '电器维修',
    other: '其他'
  }
  return types[type] || type
}

const getStatusName = (status) => {
  const statuses = {
    pending: '待处理',
    processing: '处理中',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statuses[status] || status
}

const getStatusType = (status) => {
  const types = {
    pending: 'warning',
    processing: 'primary',
    completed: 'success',
    cancelled: 'info'
  }
  return types[status] || ''
}

onMounted(() => {
  loadRepairList()
})
</script>

<style scoped>
.repair-container {
  padding: 20px;
}

.repair-form {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.repair-list {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}

.repair-detail {
  padding: 20px;
}

.repair-images {
  margin-top: 20px;
}

.repair-image {
  width: 100px;
  height: 100px;
  margin-right: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
}
</style>