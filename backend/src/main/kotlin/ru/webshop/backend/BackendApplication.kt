package ru.webshop.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import ru.webshop.backend.entity.Role
import ru.webshop.backend.repository.RoleRepository
import ru.webshop.backend.service.interfaces.AttributeService
import ru.webshop.backend.service.interfaces.CategoryService

@SpringBootApplication
@EnableJpaAuditing
class BackendApplication {
    var logger: Logger = LoggerFactory.getLogger(this::class.java)
    @Bean
    fun runner(
        roleRepository: RoleRepository,
        categoryService: CategoryService,
        attributeService: AttributeService
    ): CommandLineRunner {
        return CommandLineRunner {
            // Создание ролей
            listOf(
                Role(roleName = "USER", description = "Usual user"),
                Role(roleName = "ADMIN", description = "Admin can add products")
            ).forEach { role ->
                if (!roleRepository.existsByRoleName(role.roleName)) {
                    roleRepository.save(role)
                }
            }
            // Создание категорий и атрибутов
            val categoriesToCreate = listOf(
                "Тетрадь" to listOf("Производитель", "Количество листов", "Коллекция", "Разметка", "Кол-во в упаковке"),
                "Ежедневник" to listOf("Производитель", "Цвет", "Коллекция", " Количество листов ", "Кол-во в упаковке"),
                "Ластик" to listOf("Производитель", "Тип", "Коллекция", "Цвет", "Кол-во в упаковке"),
                "Ланъярд" to listOf("Производитель", "Цвет", "Коллекция", "Материал", "Кол-во в упаковке"),
                "Блокнот" to listOf("Производитель", "Формат", "Коллекция", "Количество листов", "Кол-во в упаковке"),
                "Ручка" to listOf("Производитель", "Цвет чернил", "Цвет ручки", "Коллекция", "Толщина стержня", "Кол-во в упаковке"),
                "Значок" to listOf("Производитель", "Цвет", "Коллекция", "Вид замка", "Кол-во в упаковке"),
                "Набор" to listOf("Производитель", "Цвет", "Коллекция", "Кол-во в упаковке")
            )

            categoriesToCreate.forEach { (categoryName, attributes) ->
                val category = try {
                    categoryService.createCategory(categoryName)
                } catch (e: RuntimeException) {
                    categoryService.getByCategoryName(categoryName)
                }

                attributes.forEach { attributeName ->
                    try {
                        attributeService.createAttributeToCategory(category.id, attributeName)
                    } catch (_: RuntimeException) {
                    }
                }
            }
        }
    }
}
fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
