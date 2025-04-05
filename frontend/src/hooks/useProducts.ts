import { keepPreviousData, useQuery } from '@tanstack/react-query'
import { ProductService } from '../api/product'
import { ProductPageResponse, ProductResponse } from '../types/product'
import { useProductStatus } from './useProductStatus'

export const useProducts = (page: number = 0, categoryId?: number) => {
	const { cartMap, wishlistSet, isLoading: statusLoading } = useProductStatus()

	const productsQuery = useQuery<ProductPageResponse, Error>({
		queryKey: ['products', page, categoryId],
		queryFn: () =>
			categoryId
				? ProductService.getByCategory(categoryId, page)
				: ProductService.getAll(page),
		placeholderData: keepPreviousData,
		staleTime: 1000 * 60 * 5,
		enabled: !statusLoading,
	})

	const enrichedProducts =
		productsQuery.data?.products?.map(product => ({
			...product,
			inCart: cartMap.get(product.article_number) || 0,
			inWishlist: wishlistSet.has(product.article_number),
		})) || []

	return {
		...productsQuery,
		data: productsQuery.data
			? {
					...productsQuery.data,
					products: enrichedProducts,
			  }
			: undefined,
	}
}

export const useProductDetails = (article: number) => {
	return useQuery<ProductResponse, Error>({
		queryKey: ['product', article],
		queryFn: () => ProductService.getProduct(article),
		enabled: !!article,
	})
}
