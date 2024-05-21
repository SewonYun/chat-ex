package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.service.UserJoinService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var userJoinService: UserJoinService

    @PostMapping("/join")
    fun join(@RequestParam nickName: String): ResponseEntity<Any> {

        val result = userJoinService.saveUser(nickName)
        return result.fold(
            { savedUser ->
                ResponseEntity.ok(mapOf("success" to true, "message" to "가입에 성공했습니다."))
            },
            { errorMessage ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("success" to false, "message" to errorMessage))
            }
        )
    }

}