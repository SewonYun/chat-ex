package com.chattingexcercis.sewonyun.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component


@Component
class TopicProducer @Autowired constructor(private val kafkaTemplate: KafkaTemplate<String, String>) {
    fun sendMessage(topic: String?, message: String) {
        kafkaTemplate.send(topic!!, message)
    }
}
