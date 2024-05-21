package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.config.KafkaTopicConfig
import com.chattingexcercis.sewonyun.application.domain.Message
import com.chattingexcercis.sewonyun.application.service.MessageService
import com.chattingexcercis.sewonyun.application.service.UserUpdateService
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    @Autowired private val kafkaTemplate: KafkaTemplate<String, Message>,
    @Autowired private val messageService: MessageService,
    @Autowired private val userUpdateService: UserUpdateService
) {

    @PostMapping("/chat/send")
    fun sendMessage(@RequestBody message: Message): Message {
        kafkaTemplate.send(KafkaTopicConfig.CHAT_TOPIC, message.chatRoomId.toString(), message)
        userUpdateService.updateActive(message.userId)
//        sendRecentRoomList()
        return messageService.saveMessage(message).getOrThrow()
    }

}