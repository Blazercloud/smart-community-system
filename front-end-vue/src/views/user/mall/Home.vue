<template>
  <div class="mall-home">
    <div class="search-bar">
      <el-input
        v-model="searchQuery"
        placeholder="搜索商品..."
        class="search-input"
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>

    <div class="promotion-section" v-if="promotionProducts.length">
      <h2>促销商品</h2>
      <el-row :gutter="20">
        <el-col :span="6" v-for="product in promotionProducts" :key="product.id">
          <el-card class="product-card" @click="goToDetail(product.id)">
            <img :src="product.image" class="product-image" />
            <div class="product-info">
              <h3>{{ product.name }}</h3>
              <div class="price">
                <span class="promotion-price">¥{{ product.promotionPrice }}</span>
                <span class="original-price">¥{{ product.originalPrice }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="all-products">
      <h2>全部商品</h2>
      <el-row :gutter="20">
        <el-col :span="6" v-for="product in products" :key="product.id">
          <el-card class="product-card" @click="goToDetail(product.id)">
            <img :src="product.image" class="product-image" />
            <div class="product-info">
              <h3>{{ product.name }}</h3>
              <div class="price">¥{{ product.price }}</div>
              <div class="store">{{ product.storeName }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[12, 24, 36, 48]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { getProductList } from '@/api/mall'

const router = useRouter()
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const products = ref([])
const promotionProducts = ref([])

const loadProducts = async () => {
  try {
    const response = await getProductList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value
    })
    products.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    console.error('获取商品列表失败:', error)
  }
}

const loadPromotionProducts = async () => {
  try {
    const response = await getProductList({
      page: 1,
      size: 4,
      isPromotion: true
    })
    promotionProducts.value = response.data.records
  } catch (error) {
    console.error('获取促销商品失败:', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadProducts()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadProducts()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadProducts()
}

const goToDetail = (id) => {
  router.push(`/mall/product/${id}`)
}

onMounted(() => {
  loadProducts()
  loadPromotionProducts()
})
</script>

<style scoped>
.mall-home {
  padding: 20px;
}

.search-bar {
  margin-bottom: 30px;
}

.search-input {
  width: 500px;
  margin: 0 auto;
  display: block;
}

.promotion-section {
  margin-bottom: 40px;
}

.product-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 10px;
}

.product-info h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  margin-top: 10px;
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.promotion-price {
  color: #f56c6c;
  margin-right: 10px;
}

.original-price {
  color: #999;
  text-decoration: line-through;
  font-size: 14px;
}

.store {
  margin-top: 5px;
  font-size: 12px;
  color: #666;
}

.pagination {
  margin-top: 30px;
  text-align: center;
}
</style>