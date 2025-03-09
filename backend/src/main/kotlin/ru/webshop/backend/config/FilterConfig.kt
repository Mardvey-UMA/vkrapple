package ru.webshop.backend.config

import org.apache.catalina.filters.CorsFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import ru.webshop.backend.filters.TelegramSignatureFilter

@Configuration
class FilterConfig {

    @Bean
    fun filterRegistration() = FilterRegistrationBean<TelegramSignatureFilter>().apply {
        filter = TelegramSignatureFilter()
        order = Ordered.HIGHEST_PRECEDENCE + 1
        addUrlPatterns("/api/*")
    }

}