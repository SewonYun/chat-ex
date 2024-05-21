package com.chattingexcercis.sewonyun.application.domain

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp

@Entity
data class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 50)
    val roomName: String? = null,

    @Column(nullable = false, length = 50)
    val roomMakerId: Long? = null,

    @ColumnDefault("false")
    @Column(nullable = false)
    val isActiveRoom: Boolean = true,

    @ColumnDefault("0")
    @Column(nullable = false)
    val userCount: Long = 0,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val cratedAt: Timestamp? = null
)