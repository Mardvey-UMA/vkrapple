// enum/union уже был в types/order.ts
export type OrderStatus =
	| 'CREATED'
	| 'WAITING_FOR_PAYMENT'
	| 'PAID'
	| 'PROCESSING'
	| 'IN_PROGRESS'
	| 'IN_WAY'
	| 'DELIVERED'
	| 'CANCELLED'
/* ───────────────────── SalesSummaryDTO ───────────────────── */

export interface SalesSummaryDTO {
	/** оформленных (НЕ отменённых) заказов */
	totalOrders: number
	/** сколько единиц товара продано в сумме */
	totalUnitsSold: number
	/** итоговая выручка (decimal на backend, число у нас) */
	totalSalesAmount: number
}

/* ───────────────────── ProductSalesDTO ──────────────────── */

export interface ProductSalesDTO {
	/** артикул */
	articleNumber: number
	/** название */
	productName: string
	/** сколько штук продано */
	totalUnitsSold: number
	/** выручка по данному товару */
	totalSalesAmount: number
}

/* ────────────────── OrderStatusAnalyticsDTO ─────────────── */

export interface OrderStatusAnalyticsDTO {
	/**
	 * карта статус ⇒ количество.
	 * пример: { IN_PROGRESS: 45, PAID: 32, CANCELLED: 8 }
	 */
	statusCounts: Record<OrderStatus, number>
}
