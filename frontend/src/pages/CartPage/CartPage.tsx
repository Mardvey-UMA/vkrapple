import { Button, Spin, Typography } from 'antd'
import { useState } from 'react'
import { Link } from 'react-router-dom'
import { CartItemComponent } from '../../components/CartItem/CartItemComponent'
import { useCart, useCartActions } from '../../hooks/useCart'
import styles from './CartPage.module.scss'

const { Title } = Typography

export const CartPage = () => {
	const [page, setPage] = useState(0)
	const { data, isLoading, isFetching } = useCart(page)
	const { removeFromCart, addToCart } = useCartActions()
	const [removedArticles, setRemovedArticles] = useState<number[]>([])

	const handleLoadMore = () => setPage(prev => prev + 1)
	const handleRemove = (article: number) => {
		setRemovedArticles(prev => [...prev, article])
		removeFromCart.mutate(article)
	}

	const handleQuantityChange = (article: number, delta: number) => {
		addToCart.mutate({ article_number: article, quantity: delta })
	}

	const totalAmount =
		data?.items.reduce((sum, item) => sum + item.price * item.quantity, 0) || 0

	const filteredItems =
		data?.items.filter(
			item => !removedArticles.includes(item.article_number)
		) || []

	return (
		<div className={styles.container}>
			<Title level={2} className={styles.title}>
				Корзина
			</Title>

			{totalAmount > 0 && (
				<div className={styles.summary}>
					<span>Итого: {totalAmount.toFixed(2)} ₽</span>
					<Link to='/checkout'>
						<Button type='primary'>Оформить заказ</Button>
					</Link>
				</div>
			)}

			{isLoading ? (
				<Spin size='large' className={styles.spinner} />
			) : (
				<>
					<div className={styles.list}>
						{filteredItems.map(item => (
							<CartItemComponent
								key={item.article_number}
								article={item.article_number}
								quantity={item.quantity}
								onRemove={handleRemove}
								onQuantityChange={handleQuantityChange}
							/>
						))}
					</div>

					{data && data?.current_page < data?.total_pages && (
						<div className={styles.loadMore}>
							<Button
								type='primary'
								loading={isFetching}
								onClick={handleLoadMore}
							>
								Показать еще
							</Button>
						</div>
					)}
				</>
			)}

			{!isLoading && filteredItems.length === 0 && (
				<div className={styles.empty}>Корзина пуста</div>
			)}
		</div>
	)
}
