package com.chattingexcercis.sewonyun.application.service

import com.chattingexcercis.sewonyun.application.config.KafkaTopicConfig
import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.domain.ChatRoomUserRelation
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRelationRepository
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class EnterChatRoomService(
    @Autowired private var chatRoomUserRelationRepository: ChatRoomRelationRepository,
    @Autowired private var chatRoomRepository: ChatRoomRepository,
    @Autowired private var chatRoomService: ChatRoomService,
    @Autowired private var kafkaTemplate: KafkaTemplate<String, List<ChatRoom>>
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
                    chatRoomRepository.findByIsActiveRoom(true).let {
                        kafkaTemplate.send(
                            KafkaTopicConfig.CHAT_LIST_TOPIC,
                            "1",
                            it.map {
                                it.copy(recentMessage = it.messageList.lastOrNull())
                            }.sortedByDescending { it.userCount }
                        )
                    }
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
                    chatRoomRepository.findByIsActiveRoom(true).let {
                        kafkaTemplate.send(KafkaTopicConfig.CHAT_LIST_TOPIC, "1", it)
                    }
                }
            }
    }

    fun getList(): Result<List<ChatRoom>> {
        return runCatching { chatRoomRepository.findByIsActiveRoom(true).toList() }
    }
}