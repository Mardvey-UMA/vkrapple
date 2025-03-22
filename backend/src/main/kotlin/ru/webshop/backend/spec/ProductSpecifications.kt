package ru.webshop.backend.spec

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.domain.Specification
import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Product

class ProductSpecifications {
    companion object {

        fun byCategory(categoryId: Long): Specification<Product> {
            return Specification { root, _, cb ->
                cb.equal(root.get<Product>("category").get<Long>("id"), categoryId)
            }
        }
        // Тут ошибка или не тут
        fun byAttributes(attributes: Map<Long, String>): Specification<Product> {
            return Specification { root, query, cb ->
                val predicates = attributes.map { (attrId, value) ->
                    val subquery = query?.subquery(Long::class.java)
                    val valueRoot = subquery?.from(Value::class.java)

                    if (subquery != null) {
                        if (valueRoot != null) {
                            subquery.select(valueRoot.get<Product>("product").get("id"))
                        }
                    }

                    if (subquery != null) {
                        if (valueRoot != null) {
                            subquery.where(
                                cb.and(
                                    cb.equal(valueRoot.get<Attribute>("attribute").get<Long>("id"), attrId),
                                    cb.equal(valueRoot.get<String>("value"), value),
                                    cb.equal(valueRoot.get<Product>("product").get<Long>("id"), root.get<Long>("id"))
                                )
                            )
                        }
                    }

                    cb.exists(subquery)
                }
                if (predicates.isEmpty()) cb.conjunction() else cb.and(*predicates.toTypedArray())
            }
        }
    }
}