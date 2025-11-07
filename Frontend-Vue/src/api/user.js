import request from '../utils/request'

/**
 * 用户登录
 */
export const login = (data) => {
  return request.post('/user/login', data)
}

/**
 * 用户注册
 */
export const register = (data) => {
  return request.post('/user/register', data)
}

/**
 * 获取用户信息
 */
export const getUserInfo = () => {
  return request.get('/user/info')
}

// 用户车位相关API
export function getUserParkingSpaces(params) {
  return request({
    url: '/community/parking/info',
    method: 'get',
    params
  })
}

// 车位申请
export function createParkSpaceApplication(data) {
  return request({
    url: '/user/park/application',
    method: 'post',
    data
  })
}

// 用户车位申请查询
export function getParkSpaceApplication(params) {
  return request({
    url: '/user/park/application',
    method: 'get',
    params
  })
}

// 报事报修
export function submitRepair(data) {
  return request({
    url: '/user/repair',
    method: 'post',
    data
  })
}

export function getUserRepairList(params) {
  return request({
    url: '/user/repair',
    method: 'get',
    params
  })
}

// 社区公告相关API
export function getNoticeList(params) {
  return request({
    url: '/community/noticelist',
    method: 'get',
    params
  })
}

// 生活缴费
export function getPaymentBill(params) {
  return request({
    url: '/community/payment/bill',
    method: 'get',
    params
  })
}

export function payBill(data) {
  return request({
    url: '/community/payment/pay',
    method: 'post',
    data
  })
}

export function getPaymentHistory(params) {
  return request({
    url: '/community/payment/history',
    method: 'get',
    params
  })
}

