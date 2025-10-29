import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // If backend uses a wrapper like { code: 200, data: ... }, treat non-200 as error.
    // If there is no `code` field (some APIs return raw data), accept it as success.
    if (typeof res.code !== 'undefined') {
      if (res.code !== 200) {
        ElMessage.error(res.message || '请求失败')
        return Promise.reject(new Error(res.message || '请求失败'))
      }
      return res
    }

  // No `code` field — assume success. Normalize the return value so callers
  // that expect `res.data.*` continue to work: return an object with a
  // `data` property containing the raw response.
  return { data: res }
  },
  error => {
    console.error('请求错误:', error)
    
    // 处理401未授权
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      // 跳转到登录页
      window.location.href = '/login'
    }
    
    ElMessage.error(error.message || '请求失败')
    return Promise.reject(error)
  }
)

export default request

