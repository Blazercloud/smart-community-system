import request from '@/utils/request'

// 获取商家统计数据
export function getMerchantStats() {
  return request({
    url: '/merchant/stats',
    method: 'get'
  })
}

// 获取近期订单统计
export function getRecentOrderStats(days = 7) {
  return request({
    url: '/merchant/orders/stats',
    method: 'get',
    params: { days }
  })
}

// 获取商品销量排行
export function getProductRanking(limit = 5) {
  return request({
    url: '/merchant/products/ranking',
    method: 'get',
    params: { limit }
  })
}

// 获取收入趋势数据
export function getRevenueTrend(type = 'month') {
  return request({
    url: '/merchant/revenue/trend',
    method: 'get',
    params: { type }
  })
}