// src/pages/ProfilePage/ProfilePage.tsx
import { Button, List, Typography } from 'antd'
import { useNavigate } from 'react-router-dom'
import { UserProfileCard } from '../../components/UserProfile/UserProfileCard'
import { useTelegramUser } from '../../hooks/useTelegramAuth'
import styles from './ProfilePage.module.scss'

const { Title } = Typography

const menuItems = [
	{ label: 'Мои заказы', path: '/orders' },
	{ label: 'Корзина', path: '/cart' },
	{ label: 'Список желаний', path: '/wishlist' },
]

export const ProfilePage = () => {
	const navigate = useNavigate()
	const user = useTelegramUser()

	if (!user) {
		return <div className={styles.loading}>Загрузка данных пользователя...</div>
	}

	return (
		<div className={styles.container}>
			<Title level={2} className={styles.title}>
				Профиль
			</Title>

			<UserProfileCard user={user} />

			<List
				dataSource={menuItems}
				renderItem={item => (
					<List.Item className={styles.listItem}>
						<Button
							type='text'
							block
							onClick={() => navigate(item.path)}
							className={styles.menuButton}
						>
							{item.label}
						</Button>
					</List.Item>
				)}
			/>

			<div className={styles.logoutContainer}>
				<Button
					type='primary'
					danger
					block
					onClick={() => navigate('/logout')}
					className={styles.logoutButton}
				>
					Выйти
				</Button>
			</div>
		</div>
	)
}
