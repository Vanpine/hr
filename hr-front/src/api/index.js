import request from '@/utils/request'

// 员工管理 API
export function getEmployeeList(params) {
  return request({
    url: '/employee/page',
    method: 'get',
    params
  })
}

export function getEmployeeById(id) {
  return request({
    url: `/employee/${id}`,
    method: 'get'
  })
}

export function addEmployee(data) {
  return request({
    url: '/employee',
    method: 'post',
    data
  })
}

export function updateEmployee(data) {
  return request({
    url: '/employee',
    method: 'put',
    data
  })
}

export function deleteEmployee(id) {
  return request({
    url: `/employee/${id}`,
    method: 'delete'
  })
}

export function batchDeleteEmployee(ids) {
  return request({
    url: '/employee',
    method: 'delete',
    data: ids
  })
}

// 部门管理 API
export function getDepartmentList(params) {
  return request({
    url: '/department/page',
    method: 'get',
    params
  })
}

export function getDepartmentTree() {
  return request({
    url: '/department/tree',
    method: 'get'
  })
}

export function getAllDepartments() {
  return request({
    url: '/department/list',
    method: 'get'
  })
}

export function addDepartment(data) {
  return request({
    url: '/department',
    method: 'post',
    data
  })
}

export function updateDepartment(data) {
  return request({
    url: '/department',
    method: 'put',
    data
  })
}

export function deleteDepartment(id) {
  return request({
    url: `/department/${id}`,
    method: 'delete'
  })
}

// 用户管理 API
export function getUserList() {
  return request({
    url: '/user/list',
    method: 'get'
  })
}

export function getUserById(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function addUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: `/user/${id}/status?status=${status}`,
    method: 'put'
  })
}

// 角色管理 API
export function getRoleList() {
  return request({
    url: '/role/list',
    method: 'get'
  })
}

export function getRoleById(id) {
  return request({
    url: `/role/${id}`,
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: '/role',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/role',
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

// 权限管理 API
export function getPermissionTree() {
  return request({
    url: '/permission/tree',
    method: 'get'
  })
}

export function getPermissionList() {
  return request({
    url: '/permission/list',
    method: 'get'
  })
}

export function addPermission(data) {
  return request({
    url: '/permission',
    method: 'post',
    data
  })
}

export function updatePermission(data) {
  return request({
    url: '/permission',
    method: 'put',
    data
  })
}

export function deletePermission(id) {
  return request({
    url: `/permission/${id}`,
    method: 'delete'
  })
}

// 登录 API
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 用户角色管理 API
export function getUserRoleCodes(userId) {
  return request({
    url: `/user/${userId}/roles`,
    method: 'get'
  })
}

export function getUserMenuTree(userId) {
  return request({
    url: `/permission/menu/${userId}`,
    method: 'get'
  })
}
