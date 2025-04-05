import {
	ArrowLeftOutlined,
	HeartFilled,
	HeartOutlined,
	ShoppingCartOutlined,
} from '@ant-design/icons'
import { Button, Carousel, Rate, Spin, message } from 'antd'
import { useNavigate, useParams } from 'react-router-dom'
import { useCartActions } from '../../hooks/useCart'
import { useProductDetails } from '../../hooks/useProducts'
import { useWishlistActions } from '../../hooks/useWishList'
import styles from './ProductPage.module.scss'

export const ProductPage = () => {
	const { article } = useParams<{ article: string }>()
	const navigate = useNavigate()
	const { data: product, isLoading, error } = useProductDetails(Number(article))
	const { mutate: addToCart } = useCartActions().addToCart
	const { mutate: addToWishlist, mutate: removeFromWishlist } =
		useWishlistActions()

	const handleCartAction = () => {
		addToCart({ article_number: Number(article), quantity: 1 })
		message.success('Товар добавлен в корзину')
	}

	if (isLoading) return <Spin className={styles.spinner} />
	if (error)
		return <div className={styles.error}>Ошибка загрузки: {error.message}</div>
	if (!product) return null

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

			<Carousel className={styles.carousel}>
				{product.photos.map((photo, index) => (
					<div key={index}>
						<img src={photo} alt={`${product.name} ${index + 1}`} />
					</div>
				))}
			</Carousel>

			<div className={styles.content}>
				<h1 className={styles.title}>{product.name}</h1>

				<div className={styles.meta}>
					<Rate value={product.rating} disabled className={styles.rating} />
					<span className={styles.price}>{product.price} ₽</span>
				</div>

				<div className={styles.actions}>
					<Button
						type='primary'
						icon={<ShoppingCartOutlined />}
						onClick={handleCartAction}
						className={styles.cartButton}
					>
						Добавить в корзину
					</Button>

					<Button
						type={product.inWishlist ? 'primary' : 'default'}
						icon={product.inWishlist ? <HeartFilled /> : <HeartOutlined />}
						onClick={() =>
							product.inWishlist
								? removeFromWishlist(product.article_number)
								: addToWishlist(product.article_number)
						}
					/>
				</div>

				{product.attributes.length > 0 && (
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
					<h3>Отзывы ({product.reviews.length})</h3>
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
