<template>
  <div class="wallet-page">
    <h2>钱包管理</h2>
    <el-card>
      <div class="balance">余额：¥{{ balance.toFixed(2) }}</div>
      <el-input-number v-model="rechargeAmount" :min="1" />
      <el-button type="primary" @click="recharge">充值</el-button>
    </el-card>

    <div style="margin-top:20px">
      <h3>账单明细（示例）</h3>
      <el-table :data="records" style="width:100%">
        <el-table-column prop="id" label="流水号" width="160" />
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column prop="amount" label="金额" width="120"> </el-table-column>
        <el-table-column prop="time" label="时间" width="180" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const balance = ref(100.0)
const rechargeAmount = ref(10)
const records = ref([
  { id: 'T2025001', type: '充值', amount: '+100.00', time: '2025-10-01 10:00' }
])

const recharge = () => {
  balance.value += rechargeAmount.value
  records.value.unshift({ id: `T${Date.now()}`, type: '充值', amount: `+${rechargeAmount.value}.00`, time: new Date().toLocaleString() })
  ElMessage.success('充值成功（示例）')
}
</script>

<style scoped>
.wallet-page { padding: 20px; background: #fff }
.balance { font-size:20px; margin-bottom:10px }
</style>