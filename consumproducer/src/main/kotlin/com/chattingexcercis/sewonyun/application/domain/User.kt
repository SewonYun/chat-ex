package com.chattingexcercis.sewonyun.application.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val nickName: String? = null,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val cratedAt: Timestamp? = null,

    @UpdateTimestamp
    val lastActiveAt: Timestamp? = null
)