import request from '@/utils/request'

export const userApi = {
  login: (data) => request.post('/user/login', data),
  register: (data) => request.post('/user/register', data),
  getUserInfo: () => request.get('/user/info'),
  updateUser: (data) => request.put('/user/update', data)
}

export const videoApi = {
  getList: (params) => request.get('/video/list', { params }),
  getDetail: (id) => request.get(`/video/detail/${id}`),
  getHot: (limit = 10) => request.get('/video/hot', { params: { limit } }),
  getByCategory: (categoryId, limit = 10) => request.get(`/video/category/${categoryId}`, { params: { limit } }),
  upload: (data) => request.post('/video/upload', data)
}

export const categoryApi = {
  getList: () => request.get('/category/list')
}

export const danmakuApi = {
  getList: (videoId) => request.get(`/danmaku/list/${videoId}`),
  add: (data) => request.post('/danmaku/add', data)
}

export const commentApi = {
  getList: (videoId) => request.get(`/comment/list/${videoId}`),
  add: (data) => request.post('/comment/add', data)
}

export const actionApi = {
  like: (videoId) => request.post(`/action/like/${videoId}`),
  collect: (videoId) => request.post(`/action/collect/${videoId}`)
}