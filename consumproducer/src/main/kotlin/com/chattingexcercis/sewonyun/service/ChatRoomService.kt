package com.chattingexcercis.sewonyun.service

import arrow.core.flatMap
import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRoomService(
    private var chatRoomRepository: ChatRoomRepository,
) {

    fun makeRoom(roomName: String, roomMakerId: Long): Result<ChatRoom> {
        return ChatRoom(
            roomName = roomName,
            roomMakerId = roomMakerId,
            isActiveRoom = true,
            userCount = 0
        ).let { chatRoom ->
            runCatching { chatRoomRepository.save(chatRoom) }
                .onFailure { return Result.failure(it) }
        }.flatMap { savedRoom ->
            Result.success(savedRoom)
        }
    }

    @Transactional
    fun increaseRoomUserCount(chatRoom: ChatRoom): Result<ChatRoom> {

        return runCatching {
            chatRoomRepository.save(
                chatRoom.copy(userCount = chatRoom.userCount + 1)
            )
        }.onFailure { return Result.failure(it) }

    }

    @Transactional
    fun decreaseRoomUserCount(chatRoom: ChatRoom): Result<ChatRoom> {
        val userCount = { chatRoom.userCount - 1 }
        val isActiveRoom = { (userCount() > 0) }

        return runCatching {
            chatRoomRepository.save(
                chatRoom.copy(userCount = userCount().takeIf { it > 0 } ?: 0, isActiveRoom = isActiveRoom())
            )
        }.onFailure { return Result.failure(it) }
    }

}