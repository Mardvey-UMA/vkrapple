import { init, initData, viewport } from '@telegram-apps/sdk-react'

function initializeTelegramApp() {
	try {
		init()
		initData.restore()
		viewport.mount()
		viewport.expand()

		if (initData.raw()) {
			const data = initData.raw() || ' '
			localStorage.setItem('tg_init_data', data)
			console.log('InitData saved:')
		}
	} catch (error) {
		console.error('Telegram init error:', error)
		localStorage.removeItem('tg_init_data')
	}
}

export default initializeTelegramApp
