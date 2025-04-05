import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { CartService } from '../api/cart'

export const useCart = (page: number = 0) => {
	return useQuery({
		queryKey: ['cart', page],
		queryFn: () => CartService.getCart(page),
		placeholderData: previousData => previousData,
	})
}

export const useCartActions = () => {
	const queryClient = useQueryClient()

	const addToCart = useMutation({
		mutationFn: CartService.addItem,
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['cart'] })
			queryClient.invalidateQueries({ queryKey: ['cart-status'] })
			queryClient.invalidateQueries({ queryKey: ['products'] })
		},
	})

	const removeFromCart = useMutation({
		mutationFn: CartService.removeItem,
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['cart'] })
			queryClient.invalidateQueries({ queryKey: ['cart-status'] })
			queryClient.invalidateQueries({ queryKey: ['products'] })
		},
	})

	return { addToCart, removeFromCart }
}
