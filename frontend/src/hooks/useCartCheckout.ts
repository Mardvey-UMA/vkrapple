import { useQueryClient } from '@tanstack/react-query'
import { CartPageResponse } from '../types/cart'

export const useCartCheckout = () => {
	const queryClient = useQueryClient()

	const getCartSnapshot = () => {
		return queryClient.getQueryData<CartPageResponse>(['cart'])
	}

	return { getCartSnapshot }
}
