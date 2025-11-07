<template>
  <div class="order-management">
    <div class="page-header">
      <h2>订单管理</h2>
    </div>

    <div class="filter-container">
      <el-form :inline="true" :model="filters">
        <el-form-item label="订单号">
          <el-input v-model="filters.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="filters.status" placeholder="请选择">
            <el-option label="全部" value="" />
            <el-option label="待付款" value="pending" />
            <el-option label="待发货" value="paid" />
            <el-option label="已发货" value="shipped" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item label="下单时间">
          <el-date-picker
            v-model="filters.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="orders" style="width: 100%; margin-top: 20px">
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column prop="total" label="订单金额">
        <template #default="{ row }">
          ¥{{ row.total.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="订单状态">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
          <el-button-group>
            <el-button type="primary" size="small" @click="handleDetail(row)">
              详情
            </el-button>
            <el-button
              v-if="row.status === 'paid'"
              type="success"
              size="small"
              @click="handleShip(row)"
            >
              发货
            </el-button>
            <el-button
              v-if="['pending', 'paid'].includes(row.status)"
              type="danger"
              size="small"
              @click="handleCancel(row)"
            >
              取消
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="订单详情"
      width="800px"
    >
      <template v-if="currentOrder">
        <div class="order-info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="下单时间">{{ currentOrder.createTime }}</el-descriptions-item>
            <el-descriptions-item label="订单状态">
              <el-tag :type="getStatusType(currentOrder.status)">
                {{ getStatusText(currentOrder.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="订单金额">¥{{ currentOrder.total.toFixed(2) }}</el-descriptions-item>
          </el-descriptions>

          <h4>商品信息</h4>
          <el-table :data="currentOrder.items" border style="width: 100%">
            <el-table-column prop="name" label="商品名称" />
            <el-table-column prop="price" label="单价">
              <template #default="{ row }">¥{{ row.price.toFixed(2) }}</template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="100" />
            <el-table-column label="小计">
              <template #default="{ row }">
                ¥{{ (row.price * row.quantity).toFixed(2) }}
              </template>
            </el-table-column>
          </el-table>

          <h4>收货信息</h4>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="收货人">{{ currentOrder.address?.name }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentOrder.address?.phone }}</el-descriptions-item>
            <el-descriptions-item label="收货地址">{{ currentOrder.address?.fullAddress }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </template>
    </el-dialog>

    <!-- 发货对话框 -->
    <el-dialog
      v-model="shipDialogVisible"
      title="订单发货"
      width="500px"
    >
      <el-form :model="shipForm" label-width="100px">
        <el-form-item label="快递公司" required>
          <el-select v-model="shipForm.company" placeholder="请选择快递公司">
            <el-option label="顺丰速运" value="SF" />
            <el-option label="中通快递" value="ZTO" />
            <el-option label="圆通速递" value="YTO" />
          </el-select>
        </el-form-item>
        <el-form-item label="快递单号" required>
          <el-input v-model="shipForm.trackingNo" placeholder="请输入快递单号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="shipDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmShip">
            确认发货
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const filters = ref({
  orderNo: '',
  status: '',
  dateRange: []
})

// 示例订单数据
const orders = ref([
  {
    id: 1,
    orderNo: 'DD20251029001',
    createTime: '2025-10-29 10:00:00',
    total: 199.99,
    status: 'paid',
    items: [
      {
        name: '示例商品1',
        price: 99.99,
        quantity: 2
      }
    ],
    address: {
      name: '张三',
      phone: '13800138000',
      fullAddress: '北京市朝阳区XX街道XX小区'
    }
  }
])

const detailVisible = ref(false)
const currentOrder = ref(null)
const shipDialogVisible = ref(false)
const shipForm = ref({
  company: '',
  trackingNo: ''
})

const getStatusType = (status) => {
  const types = {
    pending: 'warning',
    paid: 'primary',
    shipped: 'success',
    completed: 'info',
    cancelled: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    pending: '待付款',
    paid: '待发货',
    shipped: '已发货',
    completed: '已完成',
    cancelled: '已取消'
  }
  return texts[status] || status
}

const handleSearch = () => {
  // TODO: 调用搜索API
  console.log('搜索条件：', filters.value)
}

const resetFilters = () => {
  filters.value = {
    orderNo: '',
    status: '',
    dateRange: []
  }
}

const handleDetail = (row) => {
  currentOrder.value = row
  detailVisible.value = true
}

const handleShip = (row) => {
  currentOrder.value = row
  shipForm.value = {
    company: '',
    trackingNo: ''
  }
  shipDialogVisible.value = true
}

const confirmShip = () => {
  if (!shipForm.value.company || !shipForm.value.trackingNo) {
    ElMessage.warning('请填写完整的发货信息')
    return
  }
  // TODO: 调用发货API
  shipDialogVisible.value = false
  ElMessage.success('发货成功')
}

const handleCancel = (row) => {
  ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // TODO: 调用取消订单API
    ElMessage.success('订单已取消')
  })
}
</script>

<style scoped>
.order-management {
  padding: 20px;
  background: #fff;
  border-radius: 4px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
  color: #1f2f3d;
}

.filter-container {
  margin: 20px 0;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.order-info {
  h4 {
    margin: 20px 0 12px;
    color: #1f2f3d;
  }
}
</style>