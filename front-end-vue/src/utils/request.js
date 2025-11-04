import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8989/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    const token = userStore?.token || localStorage.getItem('token')

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data

    // ✅ 情况1：标准统一返回结构 { code, data, message }
    if (res && typeof res.code !== 'undefined') {
      if (res.code !== 200) {
        ElMessage.error(res.message || '请求失败')
        return Promise.reject(new Error(res.message || '请求失败'))
      }
      return res
    }

    // ✅ 情况2：后端直接返回数据（兼容 Spring 原生返回）
    return { data: res }
  },
  error => {
    console.error('请求错误:', error)

    const status = error.response?.status

    // ✅ 处理401未授权（JWT过期/无效）
    if (status === 401) {
      const userStore = useUserStore()
      userStore?.clearUserInfo()

      ElMessage.error('登录已过期，请重新登录')
      router.push('/login')
    }

    // ✅ 统一错误弹窗
    else if (status === 403) {
      ElMessage.warning('没有访问权限')
    } else if (status === 404) {
      ElMessage.error('请求的资源不存在')
    } else if (status >= 500) {
      ElMessage.error('服务器错误，请稍后重试')
    } else {
      ElMessage.error(error.message || '网络错误')
    }

    return Promise.reject(error)
  }
)

export default request
