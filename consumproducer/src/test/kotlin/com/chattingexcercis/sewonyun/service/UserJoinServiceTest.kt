package com.chattingexcercis.sewonyun.service

import arrow.core.Either
import arrow.core.right
import com.chattingexcercis.sewonyun.application.domain.User
import com.chattingexcercis.sewonyun.application.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class UserJoinServiceTest {

    private val userRepository: UserRepository = mockk()
    private val userJoinService = UserJoinService(userRepository)

    @Test
    fun `saveUser success`() {
        // given
        val nickName = "testUser"
        every { userRepository.save(any()) } returns User(id = 1, nickName = nickName)

        // when
        val result = userJoinService.saveUser(nickName)

        // then
        assertEquals(User(id = 1, nickName = nickName).right(), result)
    }

    @Test
    fun `saveUser failure`() {
        // given
        val nickName = "testUser"
        every { userRepository.save(any()) } throws RuntimeException()

        // when
        val result = userJoinService.saveUser(nickName)

        // then
        assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("success" to false, "message" to "가입에 실패했습니다.")), result)
    }
}