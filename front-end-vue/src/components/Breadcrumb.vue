&lt;template>
  &lt;el-breadcrumb separator="/">
    &lt;el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="index" :to="item.path">
      {{ item.title }}
    &lt;/el-breadcrumb-item>
  &lt;/el-breadcrumb>
&lt;/template>

&lt;script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const breadcrumbs = ref([])

const getBreadcrumbs = () => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  
  breadcrumbs.value = [
    {
      path: '/',
      title: '首页'
    },
    ...matched.map(item => ({
      path: item.path,
      title: item.meta.title
    }))
  ]
}

watch(
  () => route.path,
  () => getBreadcrumbs(),
  {
    immediate: true
  }
)
&lt;/script>

&lt;style scoped>
.el-breadcrumb {
  line-height: 60px;
}