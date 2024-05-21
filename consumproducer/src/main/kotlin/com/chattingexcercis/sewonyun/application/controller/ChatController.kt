package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.config.KafkaTopicConfig
import com.chattingexcercis.sewonyun.application.domain.Message
import com.chattingexcercis.sewonyun.application.service.ChatRoomService
import com.chattingexcercis.sewonyun.application.service.MessageService
import com.chattingexcercis.sewonyun.application.service.RankingService
import com.chattingexcercis.sewonyun.application.service.UserUpdateService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chatting")
class ChatController(
    @Autowired private val kafkaTemplate: KafkaTemplate<String, Message>,
    @Autowired private val messageService: MessageService,
    @Autowired private val chatRoomService: ChatRoomService,
    @Autowired private val userUpdateService: UserUpdateService,
    @Autowired private val rankingService: RankingService
) {

    @Scheduled(fixedRate = 60000)
    fun runEveryMinute() {
        rankingService.countUser()
    }


    @Transactional
    @MessageMapping("/chat/publish/{chatroomId}")
    fun handleChatMessagePub(message: Message): Message {
        val savedMessage = messageService.saveMessage(message).getOrThrow()
        kafkaTemplate.send(KafkaTopicConfig.CHAT_TOPIC, message.chatRoomId.toString(), savedMessage)
        userUpdateService.updateActive(savedMessage.userId!!)
        chatRoomService.produceListUpdate()

        return savedMessage
    }

    @GetMapping("/list")
    fun getChatList(@RequestParam params: Map<String, String>): ResponseEntity<Any> {

        val chatRoomId = params["chatRoomId"] ?: "0"
        val result = messageService.getList(chatRoomId.toLong())

        return result.fold(
            { roomList ->
                ResponseEntity.ok(mapOf("success" to true, "data" to roomList))
            },
            { errorMessage ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("success" to false, "message" to errorMessage.message))
            }
        )
    }

}