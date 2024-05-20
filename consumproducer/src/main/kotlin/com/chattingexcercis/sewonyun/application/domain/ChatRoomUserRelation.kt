package com.chattingexcercis.sewonyun.application.domain

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
class ChatRoomUserRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(nullable = false, length = 50)
    private val chatRoomId: Long? = null

    @Column(nullable = false, length = 50)
    private val userId: Long? = null

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private val cratedAt: Timestamp? = null

    @UpdateTimestamp
    private val updatedAt: Timestamp? = null

    @Column
    private val leftAt: Timestamp? = null
}