import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

  const isLoggedIn = computed(() => !!token.value)

  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const login = (tokenValue, user) => {
    setToken(tokenValue)
    setUserInfo(user)
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  const updateUserInfo = (info) => {
    userInfo.value = { ...userInfo.value, ...info }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  return { token, userInfo, isLoggedIn, setToken, setUserInfo, login, logout, updateUserInfo }
})