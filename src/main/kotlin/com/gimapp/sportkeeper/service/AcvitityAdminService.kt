package com.gimapp.sportkeeper.service

import com.gimapp.sportkeeper.domain.Activity
import com.gimapp.sportkeeper.repo.ActivityRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ActivityAdminService(
    private val repo: ActivityRepository
) {
    private fun normalizeCode(raw: String): String =
        raw.trim().uppercase().replace(Regex("\\s+"), "_")

    @Transactional
    fun create(code: String, name: String, active: Boolean): UUID {
        val c = normalizeCode(code)
        require(c.isNotBlank()) { "code vacío" }
        require(name.isNotBlank()) { "name vacío" }

        if (repo.existsByCode(c)) {
            throw DataIntegrityViolationException("Activity code '$c' already exists")
        }
        return repo.save(Activity(code = c, name = name.trim(), active = active)).id
    }

    @Transactional
    fun update(id: UUID, code: String, name: String, active: Boolean) {
        val a = repo.findById(id).orElseThrow { NoSuchElementException("Activity not found") }
        val c = normalizeCode(code)
        require(c.isNotBlank()) { "code vacío" }
        require(name.isNotBlank()) { "name vacío" }

        // Si cambia el code, verifica unicidad
        if (a.code != c && repo.existsByCode(c)) {
            throw DataIntegrityViolationException("Activity code '$c' already exists")
        }
        repo.save(Activity(id = a.id, code = c, name = name.trim(), active = active))
    }
}
