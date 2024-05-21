package com.chattingexcercis.sewonyun.application.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin


@Configuration
class KafkaTopicConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String? = null

    companion object {
        const val CHAT_GROUP_ID = 0
        const val CHAT_TOPIC = "chat_topic"
        const val CHAT_LIST_TOPIC = "chat_list_topic"
    }

    @Bean
    fun admin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }

    @Bean
    fun chatRoomTopic(): NewTopic {
        return TopicBuilder.name(CHAT_TOPIC)
            .partitions(1)
            .replicas(1)
            .compact()
            .build()
    }

    @Bean
    fun chatListTopic(): NewTopic {
        return TopicBuilder.name(CHAT_LIST_TOPIC)
            .partitions(1)
            .replicas(1)
            .compact()
            .build()
    }

}