import {
	ArrowLeftOutlined,
	HeartFilled,
	HeartOutlined,
	ShoppingCartOutlined,
} from '@ant-design/icons'
import { Button, Carousel, Rate, Space, Spin, message } from 'antd'
import { useNavigate, useParams } from 'react-router-dom'
import { useCartActions } from '../../hooks/useCart'
import { useProductDetails } from '../../hooks/useProducts'
import { useProductStatus } from '../../hooks/useProductStatus'
import { useWishlistActions } from '../../hooks/useWishList'
import styles from './ProductPage.module.scss'

export const ProductPage = () => {
	const { article } = useParams<{ article: string }>()
	const navigate = useNavigate()
	const { data: product, isLoading, error } = useProductDetails(Number(article))
	const { cartMap, wishlistSet } = useProductStatus()
	const { addToCart, removeFromCart } = useCartActions()
	const { addToWishlist, removeFromWishlist } = useWishlistActions()
	if (!product) return <div>Товар не найден</div>
	const enrichedProduct = product
		? {
				...product,
				inCart: cartMap.get(product.article_number) || 0,
				inWishlist: wishlistSet.has(product.article_number),
		  }
		: null

	const handleCartAction = (action: 'add' | 'remove') => {
		if (!enrichedProduct) return

		if (action === 'add') {
			addToCart.mutate({
				article_number: enrichedProduct.article_number,
				quantity: 1,
			})
			message.success('Товар добавлен в корзину')
		} else {
			if (enrichedProduct.inCart === 1) {
				removeFromCart.mutate(enrichedProduct.article_number)
			} else {
				addToCart.mutate({
					article_number: enrichedProduct.article_number,
					quantity: -1,
				})
			}
			message.success('Количество товара изменено')
		}
	}

	if (isLoading) return <Spin className={styles.spinner} />
	if (error)
		return <div className={styles.error}>Ошибка загрузки: {error.message}</div>
	if (!enrichedProduct) return null

	return (
		<div className={styles.container}>
			<Button
				type='link'
				icon={<ArrowLeftOutlined />}
				onClick={() => navigate(-1)}
				className={styles.backButton}
			>
				Назад
			</Button>

			<Carousel arrows autoplay className={styles.carousel}>
				{enrichedProduct.photos.map((photo, index) => (
					<div key={index}>
						<img src={photo} alt={`${enrichedProduct.name} ${index}`} />
					</div>
				))}
			</Carousel>

			<div className={styles.content}>
				<h1 className={styles.title}>{enrichedProduct.name}</h1>

				<div className={styles.meta}>
					<Rate
						value={enrichedProduct.rating}
						disabled
						className={styles.rating}
					/>
					<span className={styles.price}>{enrichedProduct.price} ₽</span>
				</div>

				<div className={styles.actions}>
					{enrichedProduct.inCart > 0 ? (
						<Space className={styles.quantityControl}>
							<Button
								type='primary'
								danger
								onClick={() => handleCartAction('remove')}
							>
								-
							</Button>
							<span className={styles.quantity}>{enrichedProduct.inCart}</span>
							<Button type='primary' onClick={() => handleCartAction('add')}>
								+
							</Button>
						</Space>
					) : (
						<Button
							type='primary'
							icon={<ShoppingCartOutlined />}
							onClick={() => handleCartAction('add')}
							className={styles.cartButton}
						>
							Добавить в корзину
						</Button>
					)}

					<Button
						type={enrichedProduct.inWishlist ? 'primary' : 'default'}
						icon={
							enrichedProduct.inWishlist ? <HeartFilled /> : <HeartOutlined />
						}
						onClick={() =>
							enrichedProduct.inWishlist
								? removeFromWishlist.mutate(enrichedProduct.article_number)
								: addToWishlist.mutate(enrichedProduct.article_number)
						}
					/>
				</div>

				{product.attributes?.length > 0 && (
					<div className={styles.attributes}>
						<h3>Характеристики:</h3>
						{product.attributes.map(attr => (
							<div key={attr.attribute_id} className={styles.attribute}>
								<span>{attr.attribute_name}:</span>
								<span>{attr.value}</span>
							</div>
						))}
					</div>
				)}

				<div className={styles.reviews}>
					<h3>Отзывы ({product.reviews?.length})</h3>
					{product.reviews.map(review => (
						<div key={review.id} className={styles.review}>
							<Rate value={review.rating} disabled />
							<p>{review.text}</p>
							{review.photos?.map((photo, i) => (
								<img key={i} src={photo} alt={`Фото отзыва ${i + 1}`} />
							))}
						</div>
					))}
				</div>
			</div>
		</div>
	)
}
