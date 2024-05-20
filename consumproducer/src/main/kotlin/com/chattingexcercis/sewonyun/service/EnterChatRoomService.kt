package com.chattingexcercis.sewonyun.service

import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.domain.ChatRoomUserRelation
import com.chattingexcercis.sewonyun.application.domain.User
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRelationRepository
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRepository
import org.springframework.stereotype.Service

@Service
class EnterChatRoomService(
    private var chatRoomUserRelationRepository: ChatRoomRelationRepository,
    private var chatRoomRepository: ChatRoomRepository
) {

    fun enter(chatRoom: ChatRoom, user: User): Result<ChatRoomUserRelation> {

        val relation = ChatRoomUserRelation(
            chatRoomId = chatRoom.id,
            userId = user.id
        )

        return runCatching { chatRoomUserRelationRepository.save(relation) }
    }

    fun getList(): Result<List<ChatRoom?>> {
        return runCatching { chatRoomRepository.findActiveChatRoomList().toList() }
    }

}