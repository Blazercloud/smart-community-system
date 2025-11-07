<template>
  <div class="orders-page">
    <h2>我的订单</h2>
    <el-table :data="orders" style="width: 100%">
      <el-table-column prop="orderNo" label="订单号" width="220" />
      <el-table-column prop="amount" label="金额" width="120">
        <template #default="{ row }">¥{{ row.amount }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button v-if="row.status==='pending'" type="primary" @click="pay(row)">去支付</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderList, payOrder } from '@/api/mall'

const orders = ref([])

const load = async () => {
  try {
    const res = await getOrderList({ page:1, size: 20 })
    orders.value = res.data.records || []
  } catch (err) {
    console.error(err)
  }
}

const pay = async (row) => {
  try {
    await payOrder({ orderId: row.id })
    ElMessage.success('支付成功')
    load()
  } catch (err) {
    ElMessage.error('支付失败')
  }
}

onMounted(load)
</script>

<style scoped>
.orders-page { padding: 20px; background: #fff }
</style>