import { Button, Spin, Typography } from 'antd'
import { useState } from 'react'
import { WishListItem } from '../../components/WishListItem/WishListItemComponent'
import { useWishlist } from '../../hooks/useWishList'
import styles from './WishlistPage.module.scss'

const { Title } = Typography

export const WishlistPage = () => {
	const [page, setPage] = useState(0)
	const { data, isLoading, isFetching } = useWishlist(page)
	const [removedArticles, setRemovedArticles] = useState<number[]>([])

	const handleLoadMore = () => setPage(prev => prev + 1)
	const handleRemove = (article: number) =>
		setRemovedArticles(prev => [...prev, article])

	const filteredItems =
		data?.items.filter(
			item => !removedArticles.includes(item.article_number)
		) || []

	return (
		<div className={styles.container}>
			<Title level={2} className={styles.title}>
				Список желаний
			</Title>

			{isLoading ? (
				<Spin size='large' className={styles.spinner} />
			) : (
				<>
					<div className={styles.list}>
						{filteredItems.map(item => (
							<WishListItem
								key={item.article_number}
								article={item.article_number}
								onRemove={handleRemove}
							/>
						))}
					</div>

					{data?.current_page < data?.total_pages && (
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
				<div className={styles.empty}>В списке желаний пока нет товаров</div>
			)}
		</div>
	)
}
