import { AddToCartRequest, CartPageResponse } from '../types/cart'
import api from '../utils/api'

export const CartService = {
	addItem: (data: AddToCartRequest) =>
		api.post('/cart/add', data).then(res => res.data),
	removeItem: (article: number) =>
		api.delete(`/cart/remove?article=${article}`).then(res => res.data),
	getCart: (page: number = 1, size: number = 20) =>
		api
			.get<CartPageResponse>(`/cart/list?page=${page}&size=${size}`)
			.then(res => res.data),
}
