package com.chattingexcercis.sewonyun.application.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp


@Entity
data class ChatRoomUserRelation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 50)
    val chatRoomId: Long? = null,

    @Column(nullable = false, length = 50)
    val userId: Long? = null,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val cratedAt: Timestamp? = null,

    @UpdateTimestamp
    val updatedAt: Timestamp? = null,

    @Column
    val leftAt: Timestamp? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "userId")
    val user: User? = null
)