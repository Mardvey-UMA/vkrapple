import {
	HeartOutlined,
	HomeOutlined,
	ShoppingCartOutlined,
	UserOutlined,
} from '@ant-design/icons'
import { Link } from 'react-router-dom'
import styles from './Navbar.module.scss'

type NavItem = {
	path: string
	icon: React.ReactNode
	label: string
}

const navItems: NavItem[] = [
	{ path: '/', icon: <HomeOutlined />, label: 'Главная' },
	{ path: '/wishlist', icon: <HeartOutlined />, label: 'Избранное' },
	{ path: '/profile', icon: <UserOutlined />, label: 'Профиль' },
	{ path: '/cart', icon: <ShoppingCartOutlined />, label: 'Корзина' },
]

export const Navbar = ({ activePath }: { activePath: string }) => {
	return (
		<nav className={styles.navbar}>
			{navItems.map(item => (
				<Link
					key={item.path}
					to={item.path}
					className={`${styles.navItem} ${
						activePath === item.path ? styles.active : ''
					}`}
					aria-label={item.label}
				>
					{item.icon}
				</Link>
			))}
		</nav>
	)
}
