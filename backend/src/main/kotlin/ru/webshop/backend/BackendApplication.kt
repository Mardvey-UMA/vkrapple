package ru.webshop.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import ru.webshop.backend.repository.RoleRepository

@SpringBootApplication
@EnableJpaAuditing
class BackendApplication
//{
//    @Bean
//    fun runner(roleRepository: RoleRepository): CommandLineRunner {
//        return CommandLineRunner {
//            if (roleRepository.findByName("USER") == null) {
//                roleRepository.save(Role(name = "USER"))
//            }
//        }
//    }
//}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}
