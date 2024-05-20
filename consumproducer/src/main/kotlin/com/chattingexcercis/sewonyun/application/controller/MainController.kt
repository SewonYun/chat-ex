package com.chattingexcercis.sewonyun.application.controller

import com.google.gson.Gson
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/main")
class MainController {

    private val gson = Gson()

}