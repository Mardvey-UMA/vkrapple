import { retrieveRawInitData } from '@telegram-apps/sdk-react'
import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(config => {
	const initDataRaw = retrieveRawInitData()
	if (initDataRaw) {
		config.headers.Authorization = `tma ${initDataRaw}`
	}

	return config
})

export default api
