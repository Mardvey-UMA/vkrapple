import { useInfiniteQuery } from '@tanstack/react-query'
import { Button, Spin } from 'antd'
import { useMemo } from 'react'
import { ProductService } from '../../api/product'
import { ProductCard } from '../../components/ProductCard/ProductCard'
import { useCartActions } from '../../hooks/useCart'
import { useProductStatus } from '../../hooks/useProductStatus'
import { useWishlistActions } from '../../hooks/useWishList'
import { ProductPageResponse, ProductResponse } from '../../types/product'
import styles from './ProductListContainer.module.scss'

export const ProductListContainer = () => {
	const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
		useInfiniteQuery<ProductPageResponse>({
			queryKey: ['products'],
			queryFn: ({ pageParam = 0 }) =>
				ProductService.getAll(pageParam as number),
			initialPageParam: 0,
			getNextPageParam: lastPage =>
				lastPage.current_page < lastPage.total_pages
					? lastPage.current_page + 1
					: undefined,
		})

	const { cartMap, wishlistSet } = useProductStatus()
	const { addToCart, removeFromCart } = useCartActions()
	const { addToWishlist, removeFromWishlist } = useWishlistActions()

	const enrichedProducts = useMemo(
		() =>
			data?.pages.flatMap(page =>
				page.products.map(product => ({
					...product,
					inCart: cartMap.get(product.article_number) || 0,
					inWishlist: wishlistSet.has(product.article_number),
				}))
			) || [],
		[data, cartMap, wishlistSet]
	)

	const handleCartAction = (
		product: ProductResponse,
		action: 'add' | 'remove'
	) => {
		if (action === 'add') {
			addToCart.mutate({ article_number: product.article_number, quantity: 1 })
		} else {
			if (product.inCart === 1) {
				removeFromCart.mutate(product.article_number)
			} else {
				addToCart.mutate({
					article_number: product.article_number,
					quantity: -1,
				})
			}
		}
	}

	return (
		<div className={styles.container}>
			<div className={styles.grid}>
				{enrichedProducts.map(product => (
					<ProductCard
						key={product.article_number}
						product={product}
						onCartAction={handleCartAction}
						onWishlistAction={() =>
							product.inWishlist
								? removeFromWishlist.mutate(product.article_number)
								: addToWishlist.mutate(product.article_number)
						}
						isInWishlist={product.inWishlist}
						cartQuantity={product.inCart}
					/>
				))}
			</div>

			{hasNextPage && (
				<div className={styles.loadMore}>
					<Button
						type='primary'
						loading={isFetchingNextPage}
						onClick={() => fetchNextPage()}
					>
						Показать еще
					</Button>
				</div>
			)}

			{isFetchingNextPage && <Spin className={styles.spinner} />}
		</div>
	)
}
