function checkInitDataFreshness() {
	const initData = localStorage.getItem('tg_init_data')
	if (!initData) return

	const params = new URLSearchParams(initData)
	const authDate = parseInt(params.get('auth_date') || '0')
	const now = Math.floor(Date.now() / 1000)

	if (now - authDate > 86400) {
		localStorage.removeItem('tg_init_data')
		window.location.reload()
	}
}

export default checkInitDataFreshness
