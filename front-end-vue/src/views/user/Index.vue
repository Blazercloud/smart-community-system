<template>
  <div class="user-index">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>欢迎使用智慧社区用户端</h3>
          <p>查看您的社区信息与服务</p>
        </div>
      </template>
      
      <el-row :gutter="20" class="stats-row">
        <el-col :span="6">
          <el-statistic title="我的物业账单" :value="unpaidBills" :precision="0" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="车位状态" :value="parkingStatus" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="社区公告" :value="noticesCount" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="待处理报修" :value="pendingRepairs" />
        </el-col>
      </el-row>
      
      <el-divider />

      <h4>最新社区公告</h4>
      <el-timeline>
        <el-timeline-item
          v-for="notice in latestNotices"
          :key="notice.id"
          :timestamp="notice.date"
          placement="top"
        >
          <el-card>
            <h4>{{ notice.title }}</h4>
            <p>{{ notice.content }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>

      <el-divider />

      <h4>便民服务</h4>
      <el-row :gutter="20" class="service-row">
        <el-col :span="6">
          <el-card @click="goTo('/user/community/payment')" class="service-card">
            <h4>物业缴费</h4>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card @click="goTo('/user/community/repair')" class="service-card">
            <h4>报事维修</h4>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card @click="goTo('/user/community/services')" class="service-card">
            <h4>社区服务</h4>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card @click="goTo('/user/community/notices')" class="service-card">
            <h4>查看公告</h4>
          </el-card>
        </el-col>
      </el-row>

    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 模拟数据
const unpaidBills = ref(2)
const parkingStatus = ref('已绑定')
const noticesCount = ref(5)
const pendingRepairs = ref(1)

const latestNotices = ref([
  { id: 1, date: '2025-10-29', title: '社区活动', content: '本周末小区组织邻里活动' },
  { id: 2, date: '2025-10-28', title: '安全提示', content: '请注意夜间门禁安全' },
  { id: 3, date: '2025-10-27', title: '停车通知', content: '车位维护时间提前至周五' }
])

const goTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.card-header h3 {
  margin: 0;
}

.card-header p {
  margin: 4px 0 0;
  color: #888;
}

.user-index {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.service-row {
  margin-top: 10px;
}

.service-card {
  cursor: pointer;
  text-align: center;
  padding: 20px 0;
  transition: all 0.3s;
}

.service-card:hover {
  background-color: #f0f2f5;
  transform: translateY(-2px);
}
</style>
