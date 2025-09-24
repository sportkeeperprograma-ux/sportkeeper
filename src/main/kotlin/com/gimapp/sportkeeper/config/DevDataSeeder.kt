package com.gimapp.sportkeeper.config


import com.gimapp.sportkeeper.domain.TimeSlot
import com.gimapp.sportkeeper.repo.TimeSlotRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime


@Configuration
class DevDataSeeder {
    @Bean
    fun seedSlots(timeSlotRepository: TimeSlotRepository) = CommandLineRunner {
        if (timeSlotRepository.count() == 0L) {
            val today = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0)
            val slots = (6..22).map { h ->
                val start = today.withHour(h)
                TimeSlot(startAt = start, endAt = start.plusHours(1), capacity = if (h in 18..21) 40 else 30)
            }
            timeSlotRepository.saveAll(slots)
        }
    }

    @Bean
    fun seedAdmin(userRepo: com.gimapp.sportkeeper.repo.UserRepository,
                  pe: org.springframework.security.crypto.password.PasswordEncoder) = CommandLineRunner {
        if (userRepo.findByEmail("admin@gimapp.local") == null) {
            userRepo.save(
                com.gimapp.sportkeeper.domain.User(
                    email = "admin@gimapp.local",
                    password = pe.encode("admin123"),
                    role = "ADMIN"
                )
            )
        }
    }

}