<template>
  <div class="bill-management">
    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-select v-model="filterType" placeholder="选择费用类型" clearable style="width: 150px;">
        <el-option label="全部" value="" />
        <el-option label="物业费" value="property" />
        <el-option label="停车费" value="parking" />
        <el-option label="其他" value="other" />
      </el-select>

      <el-select v-model="filterStatus" placeholder="选择支付状态" clearable style="width: 150px; margin-left: 10px;">
        <el-option label="全部" value="" />
        <el-option label="待支付" value="pending" />
        <el-option label="已支付" value="paid" />
        <el-option label="已退款" value="refunded" />
      </el-select>

      <el-button type="primary" @click="loadBills" :loading="loading" style="margin-left: 10px;">
        <el-icon><Search /></el-icon>
        查询
      </el-button>

      <el-button type="success" @click="exportToExcel" :loading="exporting" style="margin-left: 10px;">
        <el-icon><Download /></el-icon>
        导出Excel
      </el-button>
    </div>

    <!-- 缴费表格 -->
    <div class="table-container">
      <el-table :data="billList" border v-loading="loading" style="width: 100%;">
        <el-table-column label="序号" width="60">
          <template #default="{ $index }">
            {{ (currentPage - 1) * pageSize + $index + 1 }}
          </template>
        </el-table-column>

        <el-table-column prop="id" label="订单ID" width="80" align="center" />
        <el-table-column prop="userName" label="用户名" width="150" align="center" />
        <el-table-column prop="amount" label="金额(元)" width="120" align="center" />
        <el-table-column prop="type" label="费用类型" width="120" align="center">
          <template #default="scope">
            <el-tag :type="getTypeTag(scope.row.type)">
              {{ getTypeName(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="支付状态" width="120" align="center">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payMethod" label="支付方式" width="120" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="180" align="center">
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180" align="center">
          <template #default="scope">
            {{ formatTime(scope.row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="editBill(scope.row)">编辑</el-button>
            <el-button link type="danger" @click="deleteBill(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next"
          :page-sizes="[10, 20, 30, 50]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" title="编辑缴费订单" width="50%">
      <el-form ref="editForm" :model="editFormData" label-width="120px">
        <el-form-item label="支付状态">
          <el-select v-model="editFormData.status" placeholder="请选择状态">
            <el-option label="待支付" value="pending" />
            <el-option label="已支付" value="paid" />
            <el-option label="已退款" value="refunded" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="editFormData.payMethod" placeholder="请选择支付方式">
            <el-option label="线下现金缴费" value="admin" />
            <el-option label="支付宝" value="alipay" />
            <el-option label="微信" value="wechat" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="editFormData.note" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Download } from '@element-plus/icons-vue'
import * as XLSX from 'xlsx'
import {
  getPropertyPaymentList,
  updatePropertyPayment,
  deletePropertyPayment,
  exportPropertyPaymentExcel
} from '@/api/admin'

// 数据与状态
const loading = ref(false)
const exporting = ref(false)
const billList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const filterType = ref('')
const filterStatus = ref('')
const dialogVisible = ref(false)
const editFormData = ref({})

// 加载缴费数据
const loadBills = async () => {
  loading.value = true
  try {
    const res = await getPropertyPaymentList({
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      type: filterType.value,
      status: filterStatus.value
    })
    if (res.code === 200) {
      billList.value = res.data.rows || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取缴费记录失败')
    }
  } catch (err) {
    console.error(err)
    ElMessage.error('请求失败')
  } finally {
    loading.value = false
  }
}

// 编辑订单
const editBill = (row) => {
  editFormData.value = { ...row }
  dialogVisible.value = true
}

// 提交编辑
const submitEdit = async () => {
  try {
    await updatePropertyPayment(editFormData.value)
    ElMessage.success('修改成功')
    dialogVisible.value = false
    loadBills()
  } catch (err) {
    console.error(err)
    ElMessage.error('修改失败')
  }
}

// 删除订单
const deleteBill = (row) => {
  ElMessageBox.confirm(`确认删除缴费订单 ${row.id} 吗？`, '删除确认', { type: 'warning' })
    .then(async () => {
      await deletePropertyPayment(row.id)
      ElMessage.success('删除成功')
      loadBills()
    })
    .catch(() => {})
}

// 导出 Excel
const exportToExcel = async () => {
  try {
    exporting.value = true
    const response = await exportPropertyPaymentExcel({
      type: filterType.value,
      status: filterStatus.value
    })
    
    // 创建下载链接
    const url = window.URL.createObjectURL(new Blob([response]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', '物业缴费账单.xlsx')
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (err) {
    console.error('导出失败:', err)
    ElMessage.error('导出失败')
  } finally {
    exporting.value = false
  }
}

// 工具函数
const handleSizeChange = (val) => { pageSize.value = val; loadBills() }
const handleCurrentChange = (val) => { currentPage.value = val; loadBills() }

const formatTime = (t) => t ? new Date(t).toLocaleString('zh-CN') : '-'
const getStatusName = (s) => ({ pending: '待支付', paid: '已支付', refunded: '已退款' }[s] || s)
const getStatusTag = (s) => ({ pending: 'warning', paid: 'success', refunded: 'info' }[s] || '')
const getTypeName = (t) => ({ property: '物业费', parking: '停车费', other: '其他' }[t] || t)
const getTypeTag = (t) => ({ property: 'primary', parking: 'success', other: 'info' }[t] || '')

onMounted(() => loadBills())
</script>

<style scoped>
.bill-management {
  padding: 20px;
  background-color: #fff;
}
.operation-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
.dialog-footer {
  text-align: right;
}
</style>