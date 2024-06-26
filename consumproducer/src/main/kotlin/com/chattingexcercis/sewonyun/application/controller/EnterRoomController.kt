package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.service.EnterChatRoomService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/enter")
class EnterRoomController(
    @Autowired var enterChatRoomService: EnterChatRoomService
) {

    @PostMapping("/room")
    fun enterRoom(@RequestBody requestData: Map<String, String>, session: HttpSession): ResponseEntity<Any> {
        val roomId = requestData["roomId"] ?: return ResponseEntity.badRequest().body("잘못된 요청.")
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