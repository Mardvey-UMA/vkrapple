package ru.webshop.backend.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.convert.Jsr310Converters
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions

@Configuration
class ElasticsearchConfig {

    @Bean
    fun elasticsearchCustomConversions(): ElasticsearchCustomConversions {
        return ElasticsearchCustomConversions(
            listOf(
                // Конвертер для LocalDateTime, если используется
                Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE,
                Jsr310Converters.DateToLocalDateTimeConverter.INSTANCE
            )
        )
    }

}