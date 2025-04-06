import { ProductPageResponse, ProductResponse } from '../types/product'
import api from '../utils/api'

const buildSortParams = (sortOption: string) => {
	const sortMapping: Record<string, string> = {
		price_asc: 'price,asc',
		price_desc: 'price,desc',
		rating: 'rating,desc',
		orders: 'number_of_orders,desc',
	}
	return sortMapping[sortOption] || ''
}

export const ProductService = {
	getProduct: (article: number) =>
		api.get<ProductResponse>(`/products/${article}`).then(res => res.data),

	getAll: (page: number, size: number = 20, sort?: string) =>
		api
			.get<ProductPageResponse>(
				`/products?page=${page}&size=${size}${
					sort ? `&sort=${buildSortParams(sort)}` : ''
				}`
			)
			.then(res => res.data),

	getByCategory: (
		categoryId: number,
		page: number,
		size: number = 20,
		sort?: string
	) =>
		api
			.get<ProductPageResponse>(
				`/products/category/${categoryId}?page=${page}&size=${size}${
					sort ? `&sort=${buildSortParams(sort)}` : ''
				}`
			)
			.then(res => res.data),

	search: (
		categoryId: number,
		filters: Record<number, string[]>,
		page: number,
		size: number = 20,
		sort?: string
	) => {
		const params = new URLSearchParams({
			categoryId: categoryId.toString(),
			page: page.toString(),
			size: size.toString(),
			...(sort && { sort: buildSortParams(sort) }),
		})

		Object.entries(filters).forEach(([attrId, values]) => {
			values.forEach(value => params.append(`attributes[${attrId}]`, value))
		})

		return api
			.get<ProductPageResponse>(`/products/search?${params}`)
			.then(res => res.data)
	},
}
