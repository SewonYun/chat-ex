package com.chattingexcercis.sewonyun.application.component

import com.chattingexcercis.sewonyun.application.config.KafkaTopicConfig
import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.domain.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component


@Component
class MessageConsumer {
    @Autowired
    var template: SimpMessagingTemplate? = null

    @KafkaListener(topics = [KafkaTopicConfig.CHAT_TOPIC], groupId = KafkaTopicConfig.CHAT_GROUP_ID.toString())
    fun listen(message: Message) {
        template?.convertAndSend("/topic/chat/${message.chatRoomId}", message)
    }


    @KafkaListener(topics = [KafkaTopicConfig.CHAT_LIST_TOPIC], groupId = KafkaTopicConfig.CHAT_GROUP_ID.toString())
    fun listListen(chatRoomList: List<ChatRoom>) {
        template?.convertAndSend("/topic/list", chatRoomList)
    }
}
