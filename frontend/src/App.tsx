import { useQuery } from '@tanstack/react-query'
import api from './utils/initApi'
import initializeTelegramApp from './utils/initTelegramApp'

export default function App() {
	initializeTelegramApp()

	const sendAuthRequest = async () => {
		const response = await api.post('/auth/authenticate')
		console.log('Auth Request Response:', response)
		return null
	}

	const sendTestRequest = async () => {
		const response = await api.get('/me')
		console.log('Test Request Response:', response)
		return null
	}

	const { isPending: isPending2 } = useQuery({
		queryKey: ['me', 1],
		queryFn: sendTestRequest,
		retry: 3,
		staleTime: Infinity,
	})

	const { isPending: isPending1 } = useQuery({
		queryKey: ['authenticate', 1],
		queryFn: sendAuthRequest,
		retry: 0,
		staleTime: Infinity,
	})

	const isLoading = isPending1 || isPending2
	return (
		<div>
			{isLoading ? (
				<div>Sending 1 requests...</div>
			) : (
				<div>All 1 requests completed successfully!</div>
			)}
		</div>
	)
}
