package com.gimapp.sportkeeper.config


import com.gimapp.sportkeeper.domain.Role
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DevDataSeeder {

    @Bean
    fun seedAdmin(userRepo: com.gimapp.sportkeeper.repo.UserRepository,
                  pe: org.springframework.security.crypto.password.PasswordEncoder) = CommandLineRunner {
        if (userRepo.findByEmail("admin@gimapp.local") == null) {
            userRepo.save(
                com.gimapp.sportkeeper.domain.User(
                    email = "admin@gimapp.local",
                    password = pe.encode("admin123"),
                    role = Role.ADMIN
                )
            )
            userRepo.save(
                com.gimapp.sportkeeper.domain.User(
                    email = "demoMember@prueba.com",
                    password = pe.encode("member123"),
                    role = Role.MEMBER
                )
            )
            userRepo.save(
                com.gimapp.sportkeeper.domain.User(
                    email = "demoCoach@prueba.com",
                    password = pe.encode("coach123"),
                    role = Role.COACH
                )
            )
        }
    }

}