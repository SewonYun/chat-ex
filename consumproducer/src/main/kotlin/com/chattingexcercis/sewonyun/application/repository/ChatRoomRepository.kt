package com.chattingexcercis.sewonyun.application.repository

import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository


interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {

    fun findActiveChatRoomList(isActiveRoom: Long = 1): List<ChatRoom?>

}
