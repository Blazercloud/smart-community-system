<template>
  <div class="admin-index">
    <el-card>
      <template #header>
        <div class="card-header">
          <h3>管理控制台</h3>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="总用户数" :value="1234" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="总订单数" :value="5678" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="今日收入" :value="12345" prefix="¥" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="在线用户" :value="89" />
        </el-col>
      </el-row>
      
      <el-divider />
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>待处理事项</span>
            </template>
            <el-empty description="暂无待处理事项" />
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <template #header>
              <span>系统公告</span>
            </template>
            <el-empty description="暂无系统公告" />
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
</script>

<style scoped>
.card-header h3 {
  margin: 0;
}

.admin-index {
  padding: 20px;
}
</style>

