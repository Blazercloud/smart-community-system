<template>
  <div class="admin-index">
    <el-row class="dashboard-column" type="flex" justify="center">
      <!-- 待处理事项，横向三单元 -->
      <el-col :span="24" class="module-col">
        <el-card shadow="hover" class="section-card">
          <template #header>
            <span>待处理事项</span>
          </template>
          <el-row class="todo-row" :gutter="20">
            <el-col :span="8">
              <el-card class="todo-card" shadow="hover" @click="goTo('/admin/property/visitors')">
                <div class="todo-content">
                  <div class="todo-title">访客申请-未到访</div>
                  <div class="todo-count">{{ overview.pendingVisitors }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="todo-card" shadow="hover" @click="goTo('/admin/property/complaints')">
                <div class="todo-content">
                  <div class="todo-title">业主投诉-待处理</div>
                  <div class="todo-count">{{ overview.pendingComplaints }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="todo-card" shadow="hover" @click="goTo('/admin/property/repairs')">
                <div class="todo-content">
                  <div class="todo-title">物业报修-未维修</div>
                  <div class="todo-count">{{ overview.pendingRepairs }}</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <!-- 系统公告 -->
      <el-col :span="24" class="module-col">
        <el-card shadow="hover" class="section-card notice-card">
          <template #header>
            <span>系统公告</span>
          </template>
          <ul class="notice-list">
            <li v-for="(notice, index) in noticeList" :key="index">
              <el-tag type="info" size="small">{{ notice.title }}</el-tag>
              <span class="notice-title">{{ notice.content }}</span>
            </li>
          </ul>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardOverview, getLatestNotices } from '@/api/admin.js'
import { useRouter } from 'vue-router'

const router = useRouter()

const overview = ref({
  pendingRepairs: 0,
  pendingComplaints: 0,
  pendingVisitors: 0
})

const noticeList = ref([])

const fetchData = async () => {
  try {
    const overviewRes = await getDashboardOverview()
    overview.value = overviewRes.data

    const noticeRes = await getLatestNotices()
    noticeList.value = noticeRes.data

  } catch (err) {
    console.error('获取首页数据失败', err)
  }
}

onMounted(() => {
  fetchData()
})

const goTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.admin-index {
  padding: 10px;
  box-sizing: border-box;
  height: 100%;
}

.dashboard-column {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.module-col {
  /* 待处理事项和公告只包裹内容高度，不撑满整个页面 */
}

.section-card {
  display: flex;
  flex-direction: column;
  padding: 10px;
}

/* 待处理事项横向三单元 */
.todo-row {
  display: flex;
  gap: 20px;
  flex-wrap: nowrap;
  width: 100%;
}

.todo-card {
  height: 120px; /* 固定高度，三个单元一样高 */
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.todo-card:hover {
  transform: translateY(-5px);
}

.todo-content {
  text-align: center;
}
.todo-title {
  font-size: 16px;
  margin-bottom: 5px;
}
.todo-count {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

/* 系统公告 */
.notice-card {
  padding: 10px;
}

.notice-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.notice-list li {
  margin-bottom: 8px;
}

.notice-title {
  margin-left: 6px;
  line-height: 1.5;
}
</style>
