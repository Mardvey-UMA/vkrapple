import { ReactQueryDevtools } from '@tanstack/react-query-devtools'
import { useEffect } from 'react'
import { useAuth } from './hooks/useAuth'
import { useInitialData } from './hooks/useInitialData'
import initializeTelegramApp from './utils/initTelegram'

export default function App() {
	useEffect(() => {
		initializeTelegramApp()
	}, [])

	const { authData, isAuthLoading, authError } = useAuth()
	const queries = useInitialData()

	useEffect(() => {
		if (authData?.isAuthenticated) {
			const [products, orders, wishlist, cart] = queries
			console.log('Initial data loaded:', {
				products: products.data,
				orders: orders.data,
				wishlist: wishlist.data,
				cart: cart.data,
			})
		}
	}, [queries, authData])

	if (isAuthLoading) {
		return <div>Authenticating with Telegram...</div>
	}

	if (authError) {
		return (
			<div>
				Authentication failed: {authError.message}
				<button onClick={() => window.location.reload()}>Retry</button>
			</div>
		)
	}

	return (
		<div className='app'>
			<header>My Store</header>
			<ReactQueryDevtools initialIsOpen={false} />
		</div>
	)
}
