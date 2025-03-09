import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(config => {
	const initData = localStorage.getItem('tg_init_data')
	if (initData) {
		config.headers['X-Telegram-Init'] = initData
	}
	return config
})

export default api
