<template>
  <div class="notice-center">
    <div class="notice-header">
      <h2>社区公告</h2>
    </div>

    <el-card v-for="notice in notices" :key="notice.id" class="notice-item">
      <div class="notice-title">
        <h3>{{ notice.title }}</h3>
        <span class="notice-time">{{ formatDate(notice.createTime) }}</span>
      </div>
      <div class="notice-content" v-html="notice.content"></div>
      <div class="notice-footer">
        <span class="notice-publisher">发布人: {{ notice.publisher }}</span>
      </div>
    </el-card>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNoticeList } from '@/api/community'
import { formatDate } from '@/utils/date'

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const notices = ref([])

const loadNotices = async () => {
  try {
    const response = await getNoticeList({
      page: currentPage.value,
      size: pageSize.value
    })
    notices.value = response.data.records
    total.value = response.data.total
  } catch (error) {
    console.error('获取公告列表失败:', error)
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadNotices()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadNotices()
}

onMounted(() => {
  loadNotices()
})
</script>

<style scoped>
.notice-center {
  padding: 20px;
}

.notice-header {
  margin-bottom: 20px;
}

.notice-item {
  margin-bottom: 20px;
}

.notice-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.notice-title h3 {
  margin: 0;
  color: #303133;
}

.notice-time {
  color: #909399;
  font-size: 14px;
}

.notice-content {
  padding: 15px 0;
  color: #606266;
  line-height: 1.6;
}

.notice-footer {
  color: #909399;
  font-size: 14px;
  padding-top: 10px;
  border-top: 1px solid #eee;
}

.notice-publisher {
  color: #409EFF;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>