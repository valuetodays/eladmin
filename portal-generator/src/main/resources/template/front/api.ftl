import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/${changeClassName}/add',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/${changeClassName}/delete',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/${changeClassName}/edit',
    method: 'post',
    data
  })
}

export default { add, edit, del }
