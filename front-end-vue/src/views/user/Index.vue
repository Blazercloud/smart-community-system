<template>
  <div class="user-index">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>欢迎使用东软智慧社区平台</h3>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="用户总数" :value="1234" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="今日访问" :value="567" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="商品总数" :value="89" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="订单数" :value="345" />
        </el-col>
      </el-row>
      
      <el-divider />
      
      <el-timeline>
        <el-timeline-item timestamp="2024-10-27" placement="top">
          <el-card>
            <h4>系统上线</h4>
            <p>东软智慧社区平台正式上线</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="2024-10-26" placement="top">
          <el-card>
            <h4>功能更新</h4>
            <p>新增社区服务模块</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="2024-10-25" placement="top">
          <el-card>
            <h4>功能更新</h4>
            <p>商城模块上线</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.card-header h3 {
  margin: 0;
}

.user-index {
  padding: 20px;
}
</style>

