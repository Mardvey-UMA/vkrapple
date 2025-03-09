import { initData } from '@telegram-apps/sdk-react'
import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(config => {
	initData.restore()
	const rawData = initData.raw()
	if (rawData) {
		config.headers['X-Telegram-Init'] = rawData
		console.log(rawData)
	}
	return config
})

export default api
