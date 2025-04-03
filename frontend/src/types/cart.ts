export interface AddToCartRequest {
	articleNumber: number
	quantity: number
}

export interface CartItem {
	id: number
	articleNumber: number
	productName: string
	price: number
	quantity: number
	addDate: string
}

export interface CartPageResponse {
	items: CartItem[]
	currentPage: number
	totalPages: number
	totalItems: number
}
