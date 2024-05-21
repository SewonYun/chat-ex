package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.service.UserJoinService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var userJoinService: UserJoinService

    @CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
    @PostMapping("/join")
    fun join(@RequestBody requestData: Map<String, String>, session: HttpSession): ResponseEntity<Any> {
        val nickName = requestData["nickName"] ?: return ResponseEntity.badRequest().body("닉네임을 제공해야 합니다.")

        val result = userJoinService.saveUser(nickName)
        return result.fold(
            { errorMessage ->
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("success" to false, "message" to errorMessage))
            },
            { savedUser ->
                session.setAttribute("userId", savedUser.id)
                ResponseEntity.ok(mapOf("success" to true, "message" to "가입에 성공했습니다."))
            }
        )
    }

    @GetMapping("/id")
    fun getId(session: HttpSession): ResponseEntity<Any> {
        return ResponseEntity.ok(mapOf("success" to true, "data" to session.getAttribute("userId")))
    }

}