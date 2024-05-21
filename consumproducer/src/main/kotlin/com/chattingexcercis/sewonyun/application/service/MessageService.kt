package com.chattingexcercis.sewonyun.application.service

import com.chattingexcercis.sewonyun.application.domain.Message
import com.chattingexcercis.sewonyun.application.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService(@Autowired private val messageRepository: MessageRepository) {

    fun saveMessage(message: Message): Result<Message> {
        return runCatching { messageRepository.save(message) }
    }

}