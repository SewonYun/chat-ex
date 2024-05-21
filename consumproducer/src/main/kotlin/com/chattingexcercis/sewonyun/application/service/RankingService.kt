package com.chattingexcercis.sewonyun.application.service

import com.chattingexcercis.sewonyun.application.config.KafkaTopicConfig
import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.repository.ChatRoomRepository
import com.chattingexcercis.sewonyun.application.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class RankingService(
    @Autowired private val kafkaTemplate: KafkaTemplate<String, List<ChatRoom>>,
    @Autowired private val messageRepository: MessageRepository,
    @Autowired private val chatRoomRepository: ChatRoomRepository

) {

    fun countUser() {
        chatRoomRepository.findByIsActiveRoom(true).map {
            val halfMin: Timestamp = Timestamp.valueOf(LocalDateTime.now().minusMinutes(30))
            val userChanged = it.copy(userCount = messageRepository.countingMessage(it.id!!, halfMin).toLong())
            chatRoomRepository.save(userChanged)
            kafkaTemplate.send(KafkaTopicConfig.CHAT_LIST_TOPIC, "1", chatRoomRepository.findByIsActiveRoom(true))
        }

    }
}