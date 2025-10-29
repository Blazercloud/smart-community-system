import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMerchantStore = defineStore('merchant', () => {
  const merchantToken = ref(localStorage.getItem('merchantToken') || '')
  const merchantInfo = ref(JSON.parse(localStorage.getItem('merchantInfo') || 'null'))

  const setMerchantToken = (val) => {
    merchantToken.value = val
    localStorage.setItem('merchantToken', val)
  }

  const setMerchantInfo = (val) => {
    merchantInfo.value = val
    localStorage.setItem('merchantInfo', JSON.stringify(val))
  }

  const clearMerchantInfo = () => {
    merchantToken.value = ''
    merchantInfo.value = null
    localStorage.removeItem('merchantToken')
    localStorage.removeItem('merchantInfo')
  }

  return {
    merchantToken,
    merchantInfo,
    setMerchantToken,
    setMerchantInfo,
    clearMerchantInfo
  }
})