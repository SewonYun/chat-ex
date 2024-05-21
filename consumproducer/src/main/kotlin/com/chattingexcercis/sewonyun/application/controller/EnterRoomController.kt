package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.service.EnterChatRoomService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/enter")
class EnterRoomController(
    @Autowired var enterChatRoomService: EnterChatRoomService
) {

    @PostMapping("/room")
    fun enterRoom(@RequestParam roomId: String, session: HttpSession): ResponseEntity<Any> {
        val userId = session.getAttribute("userId") as Long

        return enterChatRoomService.enter(chatRoomId = roomId.toLong(), userId = userId).fold(
            { chatRoomRelation ->
                ResponseEntity.ok(mapOf("success" to true, "data" to chatRoomRelation))
            },
            { errorMessage ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("success" to false, "message" to errorMessage.message))
            }
        )
    }

}