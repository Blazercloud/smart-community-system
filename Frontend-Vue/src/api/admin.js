import request from '../utils/request'

/**
 * 管理员登录
 */
export const adminLogin = (data) => {
  return request.post('/admin/login', data)
}

/**
 * 获取管理员信息
 */
export const getAdminInfo = () => {
  return request.get('/admin/info')
}

// 公告管理相关API
export function createNotice(data) {
  return request({
    url:'/admin/property/notices',
    method:'post',
    data
  }) 
}

export function updateNotice(data) {
  return request({
    url:'/admin/property/notices',
    method:'put',
    data
  }) 
}

export const deleteNotice = (id) => { 
    return request({
        url: `/admin/property/notices/${id}`,
        method: 'delete', 
    })
}

// 车位管理相关API
export function getParkingSpaces(params) {
  return request({
    url: '/admin/parking/space/info',
    method: 'get',
    params
  })
}

export function updateParkingSpace(data) {
  return request({
    url: '/admin/parking',
    method: 'put',
    data
  })
}

export function addParkingSpace(data) {
  return request({
    url: '/admin/parking',
    method: 'post',
    data
  })
}

export function deleteParkingSpace(id) {
  return request({
    url: `/admin/parking/${id}`,
    method: 'delete'
  })
}

// 车位申请管理相关API
export function getParkingApplications(params) {
  return request({
    url: '/admin/parking/application',
    method: 'get',
    params
  })
}

export function updateParkingApplicationStatus(id, data) {
  return request({
    url: `/admin/parking/application/${id}`,
    method: 'put',
    data
  })
}

// 报修管理相关API
export function getRepairList(params) {
  return request({
    url: '/admin/repair',
    method: 'get',
    params
  })
}

export function updateRepair(data){
  return request({
    url: '/admin/repair',
    method: 'put',
    data
  })
}

export function imgUpload(data) {
  return request({
    url: '/file/upload' ,
    method: 'post',
    data
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


// 投诉建议管理相关API
export function getComplaintList(params) {
  return request({
    url: '/api/admin/complaints',
    method: 'get',
    params
  })
}

export function updateComplaint(data) {
  return request({
    url: '/api/admin/complaints',
    method: 'put',
    data
  })
}
// 物业缴费管理相关API
export function getPropertyPaymentList(params) {
  return request({
    url: '/admin/property/payments',
    method: 'get',
    params
  })
}

export function addPropertyPayment(data) {
  return request({
    url: '/admin/property/payments',
    method: 'post',
    data
  })
}

export function updatePropertyPayment(data) {
  return request({
    url: '/admin/property/payments',
    method: 'put',
    data
  })
}

export function deletePropertyPayment(id) {
  return request({
    url: `/admin/property/payments/${id}`,
    method: 'delete'
  })
}

export function exportPropertyPaymentExcel(params) {
  return request({
    url: '/admin/property/payments/export',
    method: 'get',
    params,
    responseType: 'blob' // 用于Excel文件下载
  })
}
//访客管理相关API
export function getVisitorList(params) {
  return request({
    url: '/admin/property/visitor/list',
    method: 'get',
    params
  })
}

export function addVisitor(data) {
  return request({
    url: '/admin/property/visitor/add',
    method: 'post',
    data
  })
}

export function deleteVisitor(id) {
  return request({
    url: `/admin/property/visitor/delete/${id}`,
    method: 'delete'
  })
}

// 首页汇总信息
export function getDashboardOverview() {
  return request({
    url: '/admin/dashboard/overview',
    method: 'get'
  })
}

// 最新公告
export function getLatestNotices() {
  return request({
    url: '/admin/dashboard/latest-notices',
    method: 'get'
  })
}
