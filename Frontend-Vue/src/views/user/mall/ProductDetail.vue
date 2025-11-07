<template>
  <div class="product-detail">
    <el-row :gutter="20">
      <el-col :span="10">
        <el-image :src="product.image" fit="contain" style="width:100%;height:400px;" />
      </el-col>
      <el-col :span="14">
        <h2>{{ product.name }}</h2>
        <div class="price">¥{{ product.price }}</div>
        <div class="stock">库存：{{ product.stock }}</div>
        <div class="store">门店：{{ product.storeName }}</div>

        <el-input-number v-model="quantity" :min="1" :max="product.stock" />
        <el-button type="primary" @click="handleAddToCart">加入购物车</el-button>
        <el-button type="success" @click="handleBuyNow">立即购买</el-button>

        <div class="description" v-html="product.description" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail, addToCart, createOrder } from '@/api/mall'

const route = useRoute()
const router = useRouter()
const product = ref({})
const quantity = ref(1)

const load = async () => {
  try {
    const id = route.params.id
    const res = await getProductDetail(id)
    product.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const handleAddToCart = async () => {
  try {
    await addToCart({ productId: product.value.id, quantity: quantity.value })
    ElMessage.success('已加入购物车')
  } catch (err) {
    ElMessage.error('加入购物车失败')
  }
}

const handleBuyNow = async () => {
  try {
    const order = await createOrder({ items: [{ productId: product.value.id, quantity: quantity.value }] })
    ElMessage.success('订单创建成功')
    router.push('/user/mall/orders')
  } catch (err) {
    ElMessage.error('购买失败')
  }
}

onMounted(load)
</script>

<style scoped>
.product-detail { padding: 20px; background: #fff; }
.price { font-size: 24px; color: #f56c6c; margin: 10px 0; }
.stock, .store { color: #909399; margin-bottom: 8px }
.description { margin-top: 20px; color: #606266 }
</style>