import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

  // 设置token
  const setToken = (val) => {
    token.value = val
    localStorage.setItem('token', val)
  }

  // 设置用户信息
  const setUserInfo = (val) => {
    userInfo.value = val
    localStorage.setItem('userInfo', JSON.stringify(val))
  }

  // 清除用户信息
  const clearUserInfo = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    clearUserInfo
  }
})

