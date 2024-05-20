package com.chattingexcercis.sewonyun.application.domain

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import java.util.BitSet


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(nullable = false, length = 50)
    private val roomName: String? = null

    @Column(nullable = false, length = 50)
    private val roomMakerId: Long? = null

    @ColumnDefault("0")
    @Column(nullable = false)
    private val isActiveRoom: Long = 0

    @ColumnDefault("0")
    @Column(nullable = false)
    private val userCount: Long = 0

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private val cratedAt: Timestamp? = null
}