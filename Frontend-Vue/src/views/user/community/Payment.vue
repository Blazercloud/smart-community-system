<template>
  <div class="payment-container">
    <div class="payment-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="payment-card">
            <template #header>
              <div class="card-header">
                <span>物业管理费</span>
                <el-tag type="danger" v-if="bills.property > 0">待缴费</el-tag>
                <el-tag type="success" v-else>已缴清</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="amount">¥{{ bills.property }}</div>
              <el-button type="primary" @click="handlePay('property')" :disabled="bills.property <= 0">
                立即缴费
              </el-button>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="payment-card">
            <template #header>
              <div class="card-header">
                <span>停车费</span>
                <el-tag type="danger" v-if="bills.parking > 0">待缴费</el-tag>
                <el-tag type="success" v-else>已缴清</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="amount">¥{{ bills.parking }}</div>
              <el-button type="primary" @click="handlePay('parking')" :disabled="bills.parking <= 0">
                立即缴费
              </el-button>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <el-card class="payment-card">
            <template #header>
              <div class="card-header">
                <span>其他费用</span>
                <el-tag type="danger" v-if="bills.other > 0">待缴费</el-tag>
                <el-tag type="success" v-else>已缴清</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="amount">¥{{ bills.other }}</div>
              <el-button type="primary" @click="handlePay('other')" :disabled="bills.other <= 0">
                立即缴费
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="payment-history">
      <h2>缴费记录</h2>
      <el-table :data="paymentHistory" style="width: 100%">
        <el-table-column prop="id" label="订单号" width="180" />
        <el-table-column prop="type" label="费用类型" width="120">
          <template #default="{ row }">
            {{ getPaymentTypeName(row.type) }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payTime" label="支付时间" width="180" />
        <el-table-column prop="payMethod" label="支付方式" width="120" />
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

    <el-dialog v-model="payDialogVisible" title="费用支付" width="30%">
      <el-form :model="payForm" label-width="80px">
        <el-form-item label="费用类型">
          {{ getPaymentTypeName(payForm.type) }}
        </el-form-item>
        <el-form-item label="应付金额">
          ¥{{ payForm.amount }}
        </el-form-item>
        <el-form-item label="支付方式">
          <el-radio-group v-model="payForm.method">
            <el-radio label="wallet">钱包支付</el-radio>
            <el-radio label="alipay">支付宝</el-radio>
            <el-radio label="wechat">微信支付</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="payDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmPay">
            确认支付
          </el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="缴费详情" width="40%">
      <div class="payment-detail" v-if="currentDetail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单号">
            {{ currentDetail.id }}
          </el-descriptions-item>
          <el-descriptions-item label="费用类型">
            {{ getPaymentTypeName(currentDetail.type) }}
          </el-descriptions-item>
          <el-descriptions-item label="支付金额">
            ¥{{ currentDetail.amount }}
          </el-descriptions-item>
          <el-descriptions-item label="支付状态">
            <el-tag :type="getStatusType(currentDetail.status)">
              {{ getStatusName(currentDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="支付时间">
            {{ currentDetail.payTime }}
          </el-descriptions-item>
          <el-descriptions-item label="支付方式">
            {{ currentDetail.payMethod }}
          </el-descriptions-item>
          <el-descriptions-item label="备注" v-if="currentDetail.note">
            {{ currentDetail.note }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPaymentBill, payBill, getPaymentHistory } from '@/api/user'

// 账单数据
const bills = reactive({
  property: 0,
  parking: 0,
  other: 0
})

// 支付表单
const payForm = reactive({
  type: '',
  amount: 0,
  method: 'wallet'
})

// 分页数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const paymentHistory = ref([])

// 对话框控制
const payDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentDetail = ref(null)

// 加载账单
const loadBills = async () => {
  try {
    const response = await getPaymentBill()
    Object.assign(bills, response.data)
  } catch (error) {
    console.error('获取账单失败:', error)
  }
}

// 加载缴费记录
const loadPaymentHistory = async () => {
  try {
    const response = await getPaymentHistory({
      page: currentPage.value,
      size: pageSize.value
    })
    paymentHistory.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    console.error('获取缴费记录失败:', error)
  }
}

// 处理支付
const handlePay = (type) => {
  payForm.type = type
  payForm.amount = bills[type]
  payForm.method = 'wallet'
  payDialogVisible.value = true
}

// 确认支付
const confirmPay = async () => {
  try {
    await ElMessageBox.confirm('确认支付该笔费用吗？', '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await payBill({
      type: payForm.type,
      amount: payForm.amount,
      method: payForm.method
    })

    ElMessage.success('支付成功')
    payDialogVisible.value = false
    
    // 刷新数据
    await loadBills()
    await loadPaymentHistory()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('支付失败:', error)
      ElMessage.error('支付失败，请重试')
    }
  }
}

// 查看详情
const viewDetail = (row) => {
  currentDetail.value = row
  detailDialogVisible.value = true
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  loadPaymentHistory()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadPaymentHistory()
}

// 工具函数
const getPaymentTypeName = (type) => {
  const types = {
    property: '物业管理费',
    parking: '停车费',
    other: '其他费用'
  }
  return types[type] || type
}

const getStatusName = (status) => {
  const statuses = {
    pending: '待支付',
    paid: '已支付',
    cancelled: '已取消',
    refunded: '已退款'
  }
  return statuses[status] || status
}

const getStatusType = (status) => {
  const types = {
    pending: 'warning',
    paid: 'success',
    cancelled: 'info',
    refunded: 'danger'
  }
  return types[status] || ''
}

onMounted(() => {
  loadBills()
  loadPaymentHistory()
})
</script>

<style scoped>
.payment-container {
  padding: 20px;
}

.payment-cards {
  margin-bottom: 30px;
}

.payment-card {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  text-align: center;
}

.amount {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
  margin: 20px 0;
}

.payment-history {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}

.payment-detail {
  padding: 20px;
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