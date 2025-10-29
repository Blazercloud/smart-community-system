import request from '@/utils/request'

// 商家登录
export function merchantLogin(data) {
	return request({
		url: '/merchant/login',
		method: 'post',
		data
	})
}

// 商家注册
export function register(data) {
    return request({
        url: '/merchant/register',
        method: 'post',
        data
    })
}

// 其他与店铺/商家相关的接口可以放在这里
