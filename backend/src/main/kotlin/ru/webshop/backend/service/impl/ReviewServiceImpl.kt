package ru.webshop.backend.service.impl

import org.springframework.stereotype.Service
import ru.webshop.backend.dto.ReviewDTO
import ru.webshop.backend.entity.Review
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.repository.ReviewRepository
import ru.webshop.backend.repository.UserRepository
import ru.webshop.backend.service.interfaces.ReviewService

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ReviewService {
    override fun createReview(reviewDTO: ReviewDTO, userId: Long, productArticle: Long) {

        val newReview = productRepository.findByArticleNumber(productArticle)?.let {
            userRepository.findByTelegramId(userId)?.let { it1 ->
                Review(
                    rating = reviewDTO.rating,
                    reviewText = reviewDTO.text,
                    product = it,
                    user = it1
                )
            }
        }
        if (newReview != null) {
            reviewRepository.save(newReview)
        }else{
            throw IllegalArgumentException("Review not found")
        }
    }
}