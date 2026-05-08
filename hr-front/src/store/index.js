import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    roleCodes: JSON.parse(localStorage.getItem('roleCodes') || '[]'),
    sidebarCollapsed: false
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    SET_ROLE_CODES(state, roleCodes) {
      state.roleCodes = roleCodes
      localStorage.setItem('roleCodes', JSON.stringify(roleCodes))
    },
    SET_SIDEBAR_COLLAPSED(state, collapsed) {
      state.sidebarCollapsed = collapsed
    },
    LOGOUT(state) {
      state.token = ''
      state.userInfo = {}
      state.roleCodes = []
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('roleCodes')
    }
  },
  actions: {
    login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        commit('SET_TOKEN', userInfo.token)
        commit('SET_USER_INFO', userInfo)
        // 存储角色编码
        if (userInfo.roleCodes) {
          commit('SET_ROLE_CODES', userInfo.roleCodes)
        }
        resolve()
      })
    },
    logout({ commit }) {
      return new Promise((resolve) => {
        commit('LOGOUT')
        resolve()
      })
    }
  },
  getters: {
    isLoggedIn: state => !!state.token,
    userInfo: state => state.userInfo,
    roleCodes: state => state.roleCodes,
    // 判断是否是管理员（ADMIN 或 HR_MANAGER）
    isAdmin: state => {
      const codes = state.roleCodes || []
      return codes.includes('ADMIN') || codes.includes('HR_MANAGER')
    },
    // 判断是否是普通员工
    isEmployee: state => {
      const codes = state.roleCodes || []
      return codes.includes('EMPLOYEE') && !codes.includes('ADMIN') && !codes.includes('HR_MANAGER')
    }
  }
})
