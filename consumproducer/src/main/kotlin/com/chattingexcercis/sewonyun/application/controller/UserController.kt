package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.domain.User
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController {

    @PostMapping("/join")
    fun join(@RequestParam nickName: String): String {
        User(nickName = nickName)
//        userRepository.save(user)
        return "redirect:/"
    }

    @GetMapping("/info")
    fun getUserInfo(session: HttpSession): String {
        return "User"
    }

}