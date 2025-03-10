import { useQuery } from '@tanstack/react-query'
import api from './utils/initApi'
import initializeTelegramApp from './utils/initTelegramApp'

export default function App() {
	initializeTelegramApp()
	const sendMeRequest = async () => {
		await api.get('/me')
		return null
	}

	const { isPending: isPending1 } = useQuery({
		queryKey: ['me', 1],
		queryFn: sendMeRequest,
		retry: 0,
		staleTime: Infinity,
	})
	const isLoading = isPending1
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
