package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.domain.ChatRoomUserRelation
import com.chattingexcercis.sewonyun.application.service.EnterChatRoomService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
class EnterRoomControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var context: WebApplicationContext

    @MockkBean
    private lateinit var enterChatRoomService: EnterChatRoomService

    lateinit var session: MockHttpSession
    val userId = 1L
    val roomId = "1"

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        session = MockHttpSession()
        session.setAttribute("userId", userId)
    }

    @Test
    fun enterRoomTest() {
        // Given
        val chatRoomUserRelation = ChatRoomUserRelation(chatRoomId = roomId.toLong(), userId = userId)
        val errorMessage = "Error occurred while entering the room"
        // Mocking the service layer for success case
        every { enterChatRoomService.enter(roomId.toLong(), userId) } returns Result.success(chatRoomUserRelation)

        // When & Then for success
        mockMvc.perform(
            MockMvcRequestBuilders.post("/enter/room")
                .param("roomId", roomId)
                .session(session) // Add the session here
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.chatRoomId").value(roomId.toLong()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(userId))

        // Mocking the service layer for failure case
        every { enterChatRoomService.enter(roomId.toLong(), userId) } returns Result.failure(Throwable(errorMessage))

        // When & Then for failure
        mockMvc.perform(
            MockMvcRequestBuilders.post("/enter/room")
                .param("roomId", roomId)
                .session(session) // Add the session here
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage))
    }

}