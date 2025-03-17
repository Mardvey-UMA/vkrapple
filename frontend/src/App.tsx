import { useQuery } from '@tanstack/react-query'
import api from './utils/initApi'
import initializeTelegramApp from './utils/initTelegramApp'

export default function App() {
	initializeTelegramApp()
	const sendAuthRequest = async () => {
		await api.post('/auth/authenticate')
		return null
	}

	const sendTestRequest = async () => {
		await api.get('/me')
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
