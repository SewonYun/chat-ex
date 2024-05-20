package com.chattingexcercis.sewonyun.application.controller

import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @PostMapping("/login")
    fun login(session: HttpSession): String {
        return "User logged in and session attributes set."
    }

    @GetMapping("/info")
    fun getUserInfo(session: HttpSession): String {
        return "User"
    }

}