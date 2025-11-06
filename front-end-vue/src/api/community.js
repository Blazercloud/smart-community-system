import request from '@/utils/request'


export const filePath = 'http://localhost:8989/file/upload'

// 获取公告列表
export function getNoticeList(params) {
  return request({
    url: '/community/noticelist',
    method: 'get',
    params
  })
}

// 车位管理相关API
export function getParkingSpaces(params) {
  return request({
    url: '/community/parking/info',
    method: 'get',
    params
  })
}

// 访客登记
export function registerVisitor(data) {
  return request({
    url: '/community/visitor/register',
    method: 'post',
    data
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

export function getRepairList(params) {
  return request({
    url: '/user/repair',
    method: 'get',
    params
  })
}

export function imgUpload(data) {
  return request({
    url: '/file/upload' ,
    method: 'post',
    data
  })
}

// 事项投诉
export function submitComplaint(data) {
  return request({
    url: '/community/complaint',
    method: 'post',
    data
  })
}

export function getComplaintList(params) {
  return request({
    url: '/community/complaint/list',
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
