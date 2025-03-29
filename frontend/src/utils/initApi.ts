import { retrieveRawInitData } from '@telegram-apps/sdk-react'
import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(config => {
	const initDataRaw = retrieveRawInitData()

	if (config.url === '/auth/authenticate') {
		if (initDataRaw) {
			config.headers['X-Telegram-Init-Data'] = initDataRaw
		}
		delete config.headers.Authorization
	} else {
		const accessToken = localStorage.getItem('accessToken')
		if (accessToken) {
			config.headers.Authorization = `Bearer ${accessToken}`
		}
		delete config.headers['X-Telegram-Init-Data']
	}

	return config
})

export default api
