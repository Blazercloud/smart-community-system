<template>
  <div class="cart-page">
    <h2>购物车</h2>
    <el-table :data="items" style="width: 100%">
      <el-table-column prop="productName" label="商品" />
      <el-table-column label="单价" width="120">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column label="数量" width="160">
        <template #default="{ row }">
          <el-input-number v-model="row.quantity" :min="1" :max="row.stock" @change="updateQuantity(row)" />
        </template>
      </el-table-column>
      <el-table-column label="小计" width="120">
        <template #default="{ row }">¥{{ (row.price * row.quantity).toFixed(2) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="danger" link @click="remove(row)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="cart-footer">
      <div class="total">合计：¥{{ total.toFixed(2) }}</div>
      <el-button type="primary" @click="checkout" :disabled="items.length===0">去结算</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getCartList, removeFromCart, createOrder } from '@/api/mall'

const items = ref([])

const load = async () => {
  try {
    const res = await getCartList()
    items.value = res.data || []
  } catch (err) {
    console.error(err)
  }
}

const updateQuantity = (row) => {
  // local update only; backend sync can be added
}

const remove = async (row) => {
  try {
    await removeFromCart({ cartItemId: row.id })
    ElMessage.success('移除成功')
    load()
  } catch (err) {
    ElMessage.error('移除失败')
  }
}

const total = computed(() => items.value.reduce((s, r) => s + r.price * r.quantity, 0))

const checkout = async () => {
  try {
    const order = await createOrder({ items: items.value.map(i => ({ productId: i.productId, quantity: i.quantity })) })
    ElMessage.success('订单已创建')
    // navigate to orders page
    window.location.href = '/user/mall/orders'
  } catch (err) {
    ElMessage.error('结算失败')
  }
}

onMounted(load)
</script>

<style scoped>
.cart-page { padding: 20px; background: #fff }
.cart-footer { display:flex; justify-content:space-between; align-items:center; margin-top:20px }
.total { font-size:18px; font-weight:600 }
</style>