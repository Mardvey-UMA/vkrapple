package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "photo_review")
data class PhotoReview (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "index_number", unique = true, nullable = false)
    var indexNumber: Int = 0,

    // Связи

    @ManyToOne
    @JoinColumn(name = "photo_id")
    val photo: Photo,

    @ManyToOne
    @JoinColumn(name = "review_id")
    val review: Review,
)
