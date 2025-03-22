package ru.webshop.backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.dto.CategoryResponseDTO
import ru.webshop.backend.service.interfaces.CategoryService

@RestController
@RequestMapping("/api/category")
class CategoryController(
    private val categoryService: CategoryService
) {
    // Список всех категорий
    @GetMapping()
    fun getAllCategories(
    ) = categoryService.getCategories()

    // Атрибуты категории по id
    @GetMapping("/{categoryId}")
    fun getAttributesByCategoryId(@PathVariable categoryId: Long
    ) = categoryService.getCategoryAttributes(categoryId)

}