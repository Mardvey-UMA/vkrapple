export interface WishListItem {
	id: number
	articleNumber: number
	productName: string
	price: number
	addDate: string
}

export interface WishListPageResponse {
	items: WishListItem[]
	currentPage: number
	totalPages: number
	totalItems: number
}
