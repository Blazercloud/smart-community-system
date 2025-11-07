<template>
  <div class="merchant-dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>今日订单</span>
            </div>
          </template>
          <div class="stat-value">{{ stats.todayOrders || 0 }}</div>
          <div class="stat-label">
            <span>待处理：{{ stats.pendingOrders || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>商品数量</span>
            </div>
          </template>
          <div class="stat-value">{{ stats.totalProducts || 0 }}</div>
          <div class="stat-label">
            <span>在售：{{ stats.activeProducts || 0 }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>本月收入</span>
            </div>
          </template>
          <div class="stat-value">¥{{ formatPrice(stats.monthlyRevenue) }}</div>
          <div class="stat-label">
            <el-tag :type="stats.monthlyGrowth >= 0 ? 'success' : 'danger'" size="small">
              {{ stats.monthlyGrowth >= 0 ? '↑' : '↓' }} {{ Math.abs(stats.monthlyGrowth || 0) }}%
            </el-tag>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <template #header>
            <div class="card-header">
              <span>总销售额</span>
            </div>
          </template>
          <div class="stat-value">¥{{ formatPrice(stats.totalRevenue) }}</div>
          <div class="stat-label">累计收入</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <!-- 订单趋势图 -->
      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>订单趋势</span>
              <el-radio-group v-model="orderTrendType" size="small" @change="refreshOrderTrend">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="orderTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 商品销量排行 -->
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>商品销量排行</span>
              <el-button type="text" @click="router.push('/merchant/products')">查看全部</el-button>
            </div>
          </template>
          <div class="ranking-list">
            <div v-for="(item, index) in productRanking" :key="item.id" class="ranking-item">
              <span class="ranking-index" :class="'top-' + (index + 1)">{{ index + 1 }}</span>
              <span class="ranking-name">{{ item.name }}</span>
              <span class="ranking-value">{{ item.sales }}件</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 收入趋势 -->
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>收入趋势</span>
              <el-radio-group v-model="revenueTrendType" size="small" @change="refreshRevenueTrend">
                <el-radio-button label="week">周</el-radio-button>
                <el-radio-button label="month">月</el-radio-button>
                <el-radio-button label="year">年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="revenueTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { getMerchantStats, getRecentOrderStats, getProductRanking, getRevenueTrend } from '../../api/merchant'
import * as echarts from 'echarts'

const router = useRouter()
const stats = ref({})
const orderTrendType = ref('week')
const revenueTrendType = ref('month')
const productRanking = ref([])

// 图表实例
let orderTrendChartInstance = null
let revenueTrendChartInstance = null
const orderTrendChart = ref(null)
const revenueTrendChart = ref(null)

// 格式化价格
const formatPrice = (price) => {
  return (price || 0).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

// 初始化图表
const initCharts = () => {
  // 订单趋势图
  orderTrendChartInstance = echarts.init(orderTrendChart.value)
  orderTrendChartInstance.setOption({
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: []
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      name: '订单数',
      type: 'line',
      smooth: true,
      data: [],
      itemStyle: {
        color: '#2d8cf0'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
          offset: 0,
          color: 'rgba(45,140,240,0.3)'
        }, {
          offset: 1,
          color: 'rgba(45,140,240,0.1)'
        }])
      }
    }]
  })

  // 收入趋势图
  revenueTrendChartInstance = echarts.init(revenueTrendChart.value)
  revenueTrendChartInstance.setOption({
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: []
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [{
      name: '收入',
      type: 'line',
      smooth: true,
      data: [],
      itemStyle: {
        color: '#19be6b'
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
          offset: 0,
          color: 'rgba(25,190,107,0.3)'
        }, {
          offset: 1,
          color: 'rgba(25,190,107,0.1)'
        }])
      }
    }]
  })
}

// 刷新数据
const refreshData = async () => {
  try {
    // 获取基础统计数据
    const statsRes = await getMerchantStats()
    stats.value = statsRes.data

    // 获取商品排行
    const rankingRes = await getProductRanking()
    productRanking.value = rankingRes.data

    // 刷新图表数据
    refreshOrderTrend()
    refreshRevenueTrend()
  } catch (error) {
    console.error('Failed to fetch dashboard data:', error)
  }
}

// 刷新订单趋势
const refreshOrderTrend = async () => {
  try {
    const days = orderTrendType.value === 'week' ? 7 : 30
    const res = await getRecentOrderStats(days)
    const { dates, counts } = res.data

    orderTrendChartInstance.setOption({
      xAxis: {
        data: dates
      },
      series: [{
        data: counts
      }]
    })
  } catch (error) {
    console.error('Failed to fetch order trend:', error)
  }
}

// 刷新收入趋势
const refreshRevenueTrend = async () => {
  try {
    const res = await getRevenueTrend(revenueTrendType.value)
    const { dates, values } = res.data

    revenueTrendChartInstance.setOption({
      xAxis: {
        data: dates
      },
      series: [{
        data: values
      }]
    })
  } catch (error) {
    console.error('Failed to fetch revenue trend:', error)
  }
}

// 监听窗口大小变化
const handleResize = () => {
  orderTrendChartInstance?.resize()
  revenueTrendChartInstance?.resize()
}

onMounted(() => {
  initCharts()
  refreshData()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  orderTrendChartInstance?.dispose()
  revenueTrendChartInstance?.dispose()
})
</script>

<style scoped>
.merchant-dashboard {
  padding: 12px 0;
}

.stat-card {
  height: 100%;
  .stat-value {
    font-size: 28px;
    font-weight: 600;
    color: #2d8cf0;
    margin: 12px 0;
  }
  .stat-label {
    color: #666;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

.chart-card {
  .chart-container {
    height: 300px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ranking-list {
  .ranking-item {
    display: flex;
    align-items: center;
    padding: 8px 0;
    font-size: 14px;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }
  }

  .ranking-index {
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
    border-radius: 12px;
    margin-right: 12px;
    font-size: 12px;
    font-weight: bold;
    color: #fff;
    background: #a0a0a0;

    &.top-1 { background: #f5a623 }
    &.top-2 { background: #b8c4ce }
    &.top-3 { background: #bf9e6b }
  }

  .ranking-name {
    flex: 1;
    margin-right: 12px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .ranking-value {
    color: #666;
  }
}
</style>
</style>