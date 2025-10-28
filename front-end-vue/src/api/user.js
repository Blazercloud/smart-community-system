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

