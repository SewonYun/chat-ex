package com.chattingexcercis.sewonyun.application.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp


@Entity
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false, length = 50)
    val userId: Long?,

    @Column(nullable = false, length = 50)
    val chatRoomId: Long,

    @Column(nullable = false, length = 500)
    val message: String,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val cratedAt: Timestamp? = null,
)