package com.chattingexcercis.sewonyun.application.service

import arrow.core.flatMap
import com.chattingexcercis.sewonyun.application.config.KafkaTopicConfig
import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRoomService(
    @Autowired private var chatRoomRepository: ChatRoomRepository,
    @Autowired private var kafkaTemplate: KafkaTemplate<String, List<ChatRoom>>
) {

    fun produceListUpdate() {
        chatRoomRepository.findByIsActiveRoom(true).let {
            kafkaTemplate.send(KafkaTopicConfig.CHAT_LIST_TOPIC, "1", it.map {
                it.copy(recentMessage = it.messageList.lastOrNull())
            }.sortedByDescending { it.userCount })
        }
    }

    fun makeRoom(roomName: String, roomMakerId: Long): Result<ChatRoom> {
        return ChatRoom(
            roomName = roomName,
            roomMakerId = roomMakerId,
            isActiveRoom = true,
            userCount = 0
        ).let { chatRoom ->
            runCatching { chatRoomRepository.save(chatRoom) }
                .onSuccess {

                }
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
        }

    }

    @Transactional
    fun decreaseRoomUserCount(chatRoom: ChatRoom): Result<ChatRoom> {
        val userCount = { chatRoom.userCount - 1 }
        val isActiveRoom = { (userCount() > 0) }

        return runCatching {
            chatRoomRepository.save(
                chatRoom.copy(userCount = userCount().takeIf { it > 0 } ?: 0, isActiveRoom = isActiveRoom())
            )
        }
    }

}