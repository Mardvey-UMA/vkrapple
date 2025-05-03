import {
	ProductAttributeDTO,
	ProductDocument,
	ProductResponse,
} from '../types/product'

export const mapDocToProduct = (doc: ProductDocument): ProductResponse => {
	const attributes: ProductAttributeDTO[] =
		doc.values?.map(v => ({
			attribute_id: v.id ?? 0,
			attribute_name: '',
			value: v.value ?? '',
		})) ?? []

	return {
		id: doc.id,
		article_number: doc.articleNumber,
		name: doc.name,
		price: doc.price ?? 0,
		rating: doc.rating ?? 0,
		number_of_orders: doc.numberOfOrders ?? 0,
		category_id: doc.categoryId ?? 0,
		category_name: doc.category ?? '',
		photos: (doc.photos ?? []) as string[],
		attributes,
		reviews: [],
	}
}
