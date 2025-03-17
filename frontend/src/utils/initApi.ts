import { retrieveRawInitData } from '@telegram-apps/sdk-react'
import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(config => {
	const initDataRaw = retrieveRawInitData()
	if (initDataRaw) {
		config.headers['X-Telegram-Init-Data'] = initDataRaw
	}

	const accessToken = localStorage.getItem('accessToken')
	if (accessToken) {
		config.headers.Authorization = `Bearer ${accessToken}`
	}

	return config
})

export default api
