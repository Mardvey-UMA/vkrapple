import {
	keepPreviousData,
	useMutation,
	useQuery,
	useQueryClient,
} from '@tanstack/react-query'
import { OrderService } from '../api'
import { OrderDTO, OrderPageResponse } from '../types/order'

export const useOrders = (page: number = 1) => {
	return useQuery<OrderPageResponse, Error>({
		queryKey: ['orders', page],
		queryFn: () => OrderService.getOrders(page),
		enabled: !!localStorage.getItem('accessToken'),
		placeholderData: keepPreviousData,
	})
}

export const useOrderActions = () => {
	const queryClient = useQueryClient()

	const createOrder = useMutation({
		mutationFn: OrderService.create,
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['orders', 'cart'] })
		},
	})

	const cancelOrder = useMutation<OrderDTO, Error, number>({
		mutationFn: orderId => OrderService.cancel(orderId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['orders'] })
		},
	})

	return { createOrder, cancelOrder }
}
