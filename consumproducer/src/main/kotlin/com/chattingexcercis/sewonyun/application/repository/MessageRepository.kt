package com.chattingexcercis.sewonyun.application.repository

import com.chattingexcercis.sewonyun.application.domain.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long> {
}