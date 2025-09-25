package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.repo.ActivityRepository
import com.gimapp.sportkeeper.web.dto.ActivityResponse
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/activities")
class ActivityPublicController(
    private val repo: ActivityRepository
) {
    @GetMapping
    @Transactional(readOnly = true)
    fun list(): List<ActivityResponse> =
        repo.findAll()
            .sortedBy { it.name.lowercase() }
            .map { ActivityResponse(it.id, it.code, it.name, it.active) }
}
