package com.gimapp.sportkeeper.web

import com.gimapp.sportkeeper.service.ActivityAdminService
import com.gimapp.sportkeeper.web.dto.ActivityUpsertRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/admin/activities")
class ActivityAdminController(
    private val svc: ActivityAdminService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@Valid @RequestBody body: ActivityUpsertRequest): Map<String, UUID> =
        mapOf("id" to svc.create(body.code, body.name, body.active))

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun update(@PathVariable id: UUID, @Valid @RequestBody body: ActivityUpsertRequest): Map<String, String> {
        svc.update(id, body.code, body.name, body.active)
        return mapOf("status" to "OK")
    }
}
