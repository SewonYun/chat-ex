package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.config.KafkaTopicConfig
import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.service.ChatRoomService
import com.chattingexcercis.sewonyun.application.service.EnterChatRoomService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chatroom")
class ChatRoomListController(
    @Autowired private val kafkaTemplateForChatRoom: KafkaTemplate<String, List<ChatRoom>>,
    @Autowired var enterChatRoomService: EnterChatRoomService,
    @Autowired var chatRoomService: ChatRoomService
) {

    @GetMapping("/list")
    fun getChatRoomList(): ResponseEntity<Any> {

        val result = enterChatRoomService.getList()

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

    @CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
    @PostMapping("/make")
    fun makeChatRoom(@RequestBody requestData: Map<String, String>, session: HttpSession): ResponseEntity<Any> {
        val roomName = requestData["roomName"] ?: return ResponseEntity.badRequest().body("방제목을 제공해야 합니다.")
        val userId = session.getAttribute("userId") as Long

        return chatRoomService.makeRoom(roomName = roomName, roomMakerId = userId).fold(
            { chatRoom ->

                enterChatRoomService.getList().onSuccess {
                    kafkaTemplateForChatRoom.send(KafkaTopicConfig.CHAT_LIST_TOPIC, "11", it)
                }
                ResponseEntity.ok(mapOf("success" to true, "data" to chatRoom))
            },
            { errorMessage ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("success" to false, "message" to errorMessage.message))
            }
        )

    }

}