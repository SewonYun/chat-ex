package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.config.KafkaTopicConfig
import com.chattingexcercis.sewonyun.application.domain.Message
import com.chattingexcercis.sewonyun.application.service.MessageService
import com.chattingexcercis.sewonyun.application.service.UserUpdateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    @Autowired private val kafkaTemplate: KafkaTemplate<String, Message>,
    @Autowired private val messageService: MessageService,
    @Autowired private val userUpdateService: UserUpdateService
) {

    @MessageMapping("/chat/publish/{chatroomId}")
    fun handleChatMessagePub(message: Message): Message {
        kafkaTemplate.send(KafkaTopicConfig.CHAT_TOPIC, message.chatRoomId.toString(), message)
        userUpdateService.updateActive(message.userId)

        return messageService.saveMessage(message).getOrThrow()
    }

}