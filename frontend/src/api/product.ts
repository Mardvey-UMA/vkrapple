import { ProductPageResponse, ProductResponse } from '../types/product'
import api from '../utils/api'

export const ProductService = {
	getProduct: (article: number) =>
		api.get<ProductResponse>(`/products/${article}`).then(res => res.data),

	getAll: (page: number, size: number = 20) =>
		api
			.get<ProductPageResponse>(`/products?page=${page}&size=${size}`)
			.then(res => res.data),

	getByCategory: (categoryId: number, page: number, size: number = 20) =>
		api
			.get<ProductPageResponse>(
				`/products/category/${categoryId}?page=${page}&size=${size}`
			)
			.then(res => res.data),

	search: (
		categoryId: number,
		filters: Record<number, string[]>,
		page: number,
		size: number = 20
	) => {
		const params = new URLSearchParams({
			categoryId: categoryId.toString(),
			page: page.toString(),
			size: size.toString(),
		})

		Object.entries(filters).forEach(([attrId, values]) => {
			values.forEach(value => params.append(`attributes[${attrId}]`, value))
		})

		return api
			.get<ProductPageResponse>(`/products/search?${params}`)
			.then(res => res.data)
	},
}
