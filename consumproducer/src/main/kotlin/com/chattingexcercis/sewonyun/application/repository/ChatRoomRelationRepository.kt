package com.chattingexcercis.sewonyun.application.repository

import com.chattingexcercis.sewonyun.application.domain.ChatRoomUserRelation
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRelationRepository : JpaRepository<ChatRoomUserRelation, Long> {
}
