package com.gimapp.sportkeeper.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "user_activity_role",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "activity_id", "role_type"])]
)
class UserActivityRole(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id")
    val activity: Activity,

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    val roleType: RoleType
)
