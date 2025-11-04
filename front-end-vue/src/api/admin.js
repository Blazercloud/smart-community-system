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


// 删除公告
export const deleteNotice = (id) => { 
    return request({
        url: `/admin/property/notices/${id}`,
        method: 'delete', 
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

export function updateParkingSpace(data) {
  return request({
    url: '/community/parking',
    method: 'put',
    data
  })
}

export function addParkingSpace(data) {
  return request({
    url: '/community/parking',
    method: 'post',
    data
  })
}

export function deleteParkingSpace(id) {
  return request({
    url: `/community/parking/${id}`,
    method: 'delete'
  })
}

//管理员获取申请信息
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