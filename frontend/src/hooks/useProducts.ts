import { keepPreviousData, useQuery } from '@tanstack/react-query'
import { ProductService } from '../api/product'
import { ProductPageResponse, ProductResponse } from '../types/product'

export const useProducts = (page: number = 1, categoryId?: number) => {
	return useQuery<ProductPageResponse, Error>({
		queryKey: ['products', page, categoryId],
		queryFn: () =>
			categoryId
				? ProductService.getByCategory(categoryId, page)
				: ProductService.getAll(page),
		placeholderData: keepPreviousData,
		staleTime: 1000 * 60 * 5,
	})
}

export const useProductDetails = (article: number) => {
	return useQuery<ProductResponse, Error>({
		queryKey: ['product', article],
		queryFn: () => ProductService.getProduct(article),
		enabled: !!article,
	})
}
