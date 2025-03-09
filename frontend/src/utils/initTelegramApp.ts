import { init, initData, viewport } from '@telegram-apps/sdk-react'

function initializeTelegramApp() {
	try {
		init()
		initData.restore()
		viewport.mount()
		viewport.expand()
	} catch (error) {
		console.error('Telegram init error:', error)
	}
}

export default initializeTelegramApp
