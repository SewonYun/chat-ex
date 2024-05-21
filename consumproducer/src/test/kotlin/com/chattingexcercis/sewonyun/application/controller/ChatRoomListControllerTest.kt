package com.chattingexcercis.sewonyun.application.controller

import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.service.EnterChatRoomService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
class ChatRoomListControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc


    @MockBean
    private lateinit var enterChatRoomService: EnterChatRoomService

    @Test
    fun getChatRoomList() {
        // Given
        val roomList = listOf(ChatRoom(roomName = "test1"))
        val errorMessage = "Error occurred while fetching chat room list"

        // Mocking the service layer for success case
        `when`(enterChatRoomService.getList()).thenReturn(Result.success(roomList))

        // When & Then for success
        mockMvc.perform(
            MockMvcRequestBuilders.get("/chatroom/list")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].roomName").value("test1"))

        // Mocking the service layer for failure case
        `when`(enterChatRoomService.getList()).thenReturn(Result.failure(Throwable(errorMessage)))

        // When & Then for failure
        mockMvc.perform(
            MockMvcRequestBuilders.get("/chatroom/list")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isInternalServerError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage))
    }
}