package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.service.ChatRoomService
import com.chattingexcercis.sewonyun.service.EnterChatRoomService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chatroom")
class ChatRoomListController(
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

    @PostMapping("/make")
    fun makeChatRoom(@RequestParam roomName: String, session: HttpSession): ResponseEntity<Any> {
        val userId = session.getAttribute("userId") as Long

        return chatRoomService.makeRoom(roomName = roomName, roomMakerId = userId).fold(
            { chatRoom ->
                ResponseEntity.ok(mapOf("success" to true, "data" to chatRoom))
            },
            { errorMessage ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("success" to false, "message" to errorMessage.message))
            }
        )

    }

}