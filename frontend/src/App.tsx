import { useQuery } from '@tanstack/react-query'
import checkInitDataFreshness from './utils/checkInitDataFreshness'
import api from './utils/initApi'
import initializeTelegramApp from './utils/initTelegramApp'

export default function App() {
	initializeTelegramApp()
	checkInitDataFreshness()
	// Функция для отправки запроса
	const sendMeRequest = async () => {
		await api.get('/me')
		return null // Возвращаем null так как данные не нужны
	}

	// Первый запрос
	const { isPending: isPending1 } = useQuery({
		queryKey: ['me', 1],
		queryFn: sendMeRequest,
		retry: 0,
		staleTime: Infinity,
	})

	// // Второй запрос
	// const { isPending: isPending2 } = useQuery({
	// 	queryKey: ['me', 2],
	// 	queryFn: sendMeRequest,
	// 	retry: 0,
	// 	staleTime: Infinity,
	// })

	// // Третий запрос
	// const { isPending: isPending3 } = useQuery({
	// 	queryKey: ['me', 3],
	// 	queryFn: sendMeRequest,
	// 	retry: 0,
	// 	staleTime: Infinity,
	// })

	// Общий статус загрузки
	//const isLoading = isPending1 || isPending2 || isPending3
	const isLoading = isPending1
	return (
		<div>
			{isLoading ? (
				<div>Sending 3 requests...</div>
			) : (
				<div>All 3 requests completed successfully!</div>
			)}
		</div>
	)
}
