import { ProductPageResponse, ProductResponse } from '../types/product'
import api from '../utils/api'

const buildSortParams = (sortOption?: string) => {
	if (!sortOption) return undefined
	const sortMapping: Record<string, string> = {
		price_asc: 'price,asc',
		price_desc: 'price,desc',
		rating: 'rating,desc',
		orders: 'numberOfOrders,desc',
	}
	return sortMapping[sortOption] || undefined
}

export const ProductService = {
	getProduct: (article: number) =>
		api.get<ProductResponse>(`/products/${article}`).then(res => res.data),

	getAll: (page: number, size: number = 20, sort?: string) =>
		api
			.get<ProductPageResponse>('/products', {
				params: {
					page,
					size,
					sort: buildSortParams(sort),
				},
			})
			.then(res => res.data),

	getByCategory: (
		categoryId: number,
		page: number,
		size: number = 20,
		sort?: string
	) =>
		api
			.get<ProductPageResponse>(`/products/category/${categoryId}`, {
				params: {
					page,
					size,
					sort: buildSortParams(sort),
				},
			})
			.then(res => res.data),

	search: (
		searchQuery: string,
		page: number,
		size: number = 20,
		sort?: string
	) =>
		api
			.get<ProductPageResponse>('/products/search', {
				params: {
					query: searchQuery,
					page,
					size,
					sort: buildSortParams(sort),
				},
			})
			.then(res => res.data),

	searchWithFilters: (
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
		})

		const sortParam = buildSortParams(sort)
		if (sortParam) {
			params.append('sort', sortParam)
		}

		Object.entries(filters).forEach(([attrId, values]) => {
			values.forEach(value => params.append(`attributes[${attrId}]`, value))
		})

		return api
			.get<ProductPageResponse>(`/products/search?${params}`)
			.then(res => res.data)
	},
}
