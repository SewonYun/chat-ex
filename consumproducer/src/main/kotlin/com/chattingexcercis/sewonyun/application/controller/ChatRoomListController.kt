package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.service.EnterChatRoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chatroom")
class ChatRoomListController {

    @Autowired
    lateinit var enterChatRoomService: EnterChatRoomService

    @GetMapping("/list")
    fun getChatRoomList(): ResponseEntity<Any> {

        val result = enterChatRoomService.getList()

        return result.fold(
            { errorMessage ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("success" to false, "message" to errorMessage))
            },
            { roomList ->
                ResponseEntity.ok(mapOf("success" to true, "data" to roomList))
            }
        )


    }

}