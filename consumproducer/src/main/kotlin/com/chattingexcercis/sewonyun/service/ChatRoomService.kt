package com.chattingexcercis.sewonyun.service

import arrow.core.flatMap
import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRepository
import com.chattingexcercis.sewonyun.application.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRoomService(
    private var userRepository: UserRepository,
    private var chatRoomRepository: ChatRoomRepository,
    private var enterChatRoomService: EnterChatRoomService
) {

    fun makeRoom(roomName: String, roomMakerId: Long): Result<ChatRoom?> {
        return ChatRoom(
            roomName = roomName,
            roomMakerId = roomMakerId,
            isActiveRoom = 1,
            userCount = 0
        ).let { chatRoom ->
            runCatching { chatRoomRepository.save(chatRoom) }
                .onFailure { return Result.failure(it) }
        }.flatMap { savedRoom ->
            runCatching { userRepository.findById(roomMakerId) }
                .onSuccess { enterChatRoomService.enter(chatRoom = savedRoom, user = it.get()) }
                .onFailure { return Result.failure(it) }

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
        val isActiveRoom = { if ((userCount() > 0)) 1L else 0 }

        return runCatching {
            chatRoomRepository.save(
                chatRoom.copy(userCount = userCount().takeIf { it > 0 } ?: 0, isActiveRoom = isActiveRoom())
            )
        }.onFailure { return Result.failure(it) }
    }

}