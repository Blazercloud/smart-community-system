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
// export function createNotice(data) {
//   return request({
//     url:'/admin/property/notices',
//     method:'post',
//     data
//   }) 
// }
// export function getNotices(params) {
//   return request({
//     url:'/admin/property/notices',
//     method:'get',
//     params
//   }) 
// }
export function updateNotice(data) {
  return request({
    url:'/admin/property/notices',
    method:'put',
    data
  }) 
}
// export function deleteNotice(id) {
//   return request({
//     url: `/admin/notice/${id}`,
//     method: 'delete'
//   })
// }
// export function deleteNotice(data){
//   return request({
//     url:'/admin/property/notices/Delete',
//     method:'delete',
//   })
// }
// export function getVisitors(params) {
//   return request({
//     url:'/admin/property/visitors',
//     method:'get',
//     params
//   }) 
// }
// export function createVisitor(data) {
//   return request({
//     url:'/admin/property/visitors',
//     method:'post',
//     data
//   }) 
// }
// export function updateVisitor(id, data) {
//   return request({
//     url:`/admin/property/visitors/${id}`,
//     method:'put',
//     data
//   }) 
// }
// export function deleteVisitor(id) {
//   return request({
//     url:`/admin/property/visitors/${id}`,
//     method:'delete'
//   }) 
// }
// export function getComplaints(params) {
//   return request({
//     url:'/admin/property/complaints',
//     method:'get',
//     params
//   }) 
// }
// export function updateComplaint(id, data) {
//   return request({
//     url:`/admin/property/complaints/${id}`,
//     method:'put',
//     data
//   }) 
// }
// export function deleteComplaint(id) {
//   return request({
//     url:`/admin/property/complaints/${id}`,
//     method:'delete'
//   }) 
// }
// export function getRepairs(params) {
//   return request({
//     url:'/admin/property/repairs',
//     method:'get',
//     params
//   }) 
// }
// export function updateRepair(id, data) {
//   return request({
//     url:`/admin/property/repairs/${id}`,
//     method:'put',
//     data
//   }) 
// } 
// export function deleteRepair(id) {
//   return request({
//     url:`/admin/property/repairs/${id}`,
//     method:'delete'
//   }) 
// }
