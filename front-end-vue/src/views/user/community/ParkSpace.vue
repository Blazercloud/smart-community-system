<template>
  <div class="parking-list">
    <div class="page-header">
      <h2>车位列表</h2>
      <el-button 
        type="primary" 
        @click="showMyParking = !showMyParking; fetchParkingList()"
      >
        {{ showMyParking ? '查看全部车位' : '查看我的车位' }}
      </el-button>
    </div>

    <!-- 车位筛选 -->
    <el-input 
      v-model="searchNumber" 
      placeholder="输入车位编号搜索" 
      style="width: 300px; margin: 10px 0"
      clearable
      @input="handleSearch"
    />

    <!-- 车位列表表格 -->
    <el-table 
      :data="parkingList" 
      border 
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="spaceNumber" label="车位编号" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag 
            :type="scope.row.status === '空闲' ? 'success' : 'warning'"
          >
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="车主" width="100" />
      <el-table-column prop="carNumber" label="车牌号" width="120" />
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button 
            size="small" 
            type="success" 
            @click="handleApply(scope.row)"
            :disabled="scope.row.status !== '空闲'"
          >
            申请此车位
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getParkingInfo } from '@/api/community' // 导入车位查询接口
import { useUserStore } from '@/stores/user.js'
import { useRouter } from 'vue-router'

const router = useRouter()
// 状态定义
const parkingList = ref([]) // 车位列表数据
const searchNumber = ref('') // 搜索关键词
const showMyParking = ref(false) // 是否显示我的车位（布尔值）
const userStore = useUserStore() // 获取当前登录用户信息

// 页面加载时查询车位
onMounted(async () => {
  await fetchParkingList() // 异步函数需加await
})

/**
 * 查询车位列表（核心修改）
 * - 显示我的车位：传递当前用户ID给后端，精准查询用户名下车位
 * - 显示全部车位：不传递ID，后端返回全部车位
 */
const fetchParkingList = async () => {
  try {
    let queryId = undefined // 初始化为undefined（不传参给后端）
    
    // 关键修改1：showMyParking是布尔值，直接判断true/false（不是字符串'false'）
    if (showMyParking.value) {
      // 关键修改2：从userStore获取用户ID（需确保登录后userInfo有值）
      queryId = userStore.userInfo?.id // 可选链避免userInfo为null时报错
    }

    // 调用后端接口：queryId为undefined时，后端查全部；有值时查该用户的车位
    const res = await getParkingInfo(queryId) 
    if (res.code === 200) {
      parkingList.value = res.data.rows // 赋值后端返回的车位列表
    }
  } catch (err) {
    console.error('查询车位失败', err)
  }
}

/**
 * 筛选逻辑（核心修改）
 * - 结合"我的车位"和"编号搜索"双重筛选
 * - 筛选结果直接更新parkingList，确保页面渲染
 */
const handleSearch = async () => {
  // 先获取原始数据（全部/我的车位）
  await fetchParkingList()
  let filteredList = [...parkingList.value]

  // 1. 按车位编号模糊筛选（优先级：搜索框输入 > 我的车位）
  if (searchNumber.value.trim()) {
    filteredList = filteredList.filter(item => 
      item.spaceNumber.toLowerCase().includes(searchNumber.value.trim().toLowerCase())
    )
  }

  // 2. 按我的车位筛选（如果开启了"查看我的车位"）
  if (showMyParking.value) {
    filteredList = filteredList.filter(item => 
      item.ownerId === userStore.userInfo?.id
    )
  }

  // 关键：更新列表数据（之前只return没赋值，页面不刷新）
  parkingList.value = filteredList
}

// 申请车位（跳转到申请页面并携带车位编号）
const handleApply = (row) => {
  // 修正路由路径：匹配你的路由配置（用户端二级路由）
  router.push({
    path: '/user/community/parking/apply', 
    query: { spaceNumber: row.spaceNumber }
  })
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>