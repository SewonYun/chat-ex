package com.chattingexcercis.sewonyun.application.repository

import com.chattingexcercis.sewonyun.application.domain.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.sql.Timestamp

interface MessageRepository : JpaRepository<Message, Long> {

    fun findAllByChatRoomId(chatRoomId: Long): List<Message>

    @Query("SELECT count(m) as cnt FROM Message m WHERE m.chatRoomId = :chatRoomId AND m.cratedAt > :halfMin")
    fun countingMessage(chatRoomId: Long, halfMin: Timestamp): Int

}