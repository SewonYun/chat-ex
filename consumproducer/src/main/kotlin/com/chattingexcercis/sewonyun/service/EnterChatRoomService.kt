package com.chattingexcercis.sewonyun.service

import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.domain.ChatRoomUserRelation
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRelationRepository
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class EnterChatRoomService(
    @Autowired private var chatRoomUserRelationRepository: ChatRoomRelationRepository,
    @Autowired private var chatRoomRepository: ChatRoomRepository,
    @Autowired private var chatRoomService: ChatRoomService
) {

    fun enter(chatRoomId: Long, userId: Long): Result<ChatRoomUserRelation> {

        val relation = ChatRoomUserRelation(
            chatRoomId = chatRoomId,
            userId = userId
        )

        return runCatching { chatRoomUserRelationRepository.save(relation) }
            .onSuccess { chatRoomUserRelation ->
                chatRoomRepository.findById(chatRoomUserRelation.chatRoomId!!).let { chatRoom ->
                    chatRoom.map { chatRoomService.increaseRoomUserCount(it) }
                }
            }
    }

    fun leave(userId: Long, chatRoomId: Long): Result<ChatRoomUserRelation> {
        val chatRoomUserRelation = chatRoomUserRelationRepository.findByUserIdAndChatRoomId(userId, chatRoomId)
        return runCatching {
            chatRoomUserRelationRepository.save(
                chatRoomUserRelation.copy(leftAt = Timestamp(System.currentTimeMillis()))
            )
        }
        .onSuccess { chatRoomUserRelation ->
            chatRoomRepository.findById(chatRoomUserRelation.chatRoomId!!).let { chatRoom ->
                chatRoom.map { chatRoomService.decreaseRoomUserCount(it) }
            }
        }
    }

    fun getList(): Result<List<ChatRoom?>> {
        return runCatching { chatRoomRepository.findByIsActiveRoom(true).toList() }
    }
}