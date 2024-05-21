package com.chattingexcercis.sewonyun.application.config

import com.chattingexcercis.sewonyun.application.domain.ChatRoom
import com.chattingexcercis.sewonyun.application.domain.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer


@EnableKafka
@Configuration
class ListenerConfig {

    @Value(value = "\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapAddress: String

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Message> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, Message> =
            ConcurrentKafkaListenerContainerFactory<String, Message>()
        factory.setConsumerFactory(consumerFactory())
        return factory
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Message> {
        return DefaultKafkaConsumerFactory(
            consumerConfigurations(), StringDeserializer(), JsonDeserializer(
                Message::class.java
            )
        )
    }

    @Bean
    fun consumerConfigurations(): Map<String, Any> {
        val configurations: MutableMap<String, Any> = HashMap()
        configurations[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configurations[ConsumerConfig.GROUP_ID_CONFIG] = KafkaTopicConfig.CHAT_GROUP_ID
        configurations[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] =
            StringDeserializer::class.java
        configurations[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] =
            JsonDeserializer::class.java
        configurations[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        return configurations
    }


    @Bean
    fun kafkaListenerContainerFactoryForChatRoom(): ConcurrentKafkaListenerContainerFactory<String, List<ChatRoom>> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, List<ChatRoom>> =
            ConcurrentKafkaListenerContainerFactory<String, List<ChatRoom>>()
        factory.setConsumerFactory(consumerFactoryForChatRoom())
        return factory
    }

    @Bean
    fun consumerFactoryForChatRoom(): ConsumerFactory<String, List<ChatRoom>> {
        return DefaultKafkaConsumerFactory(consumerConfigurations())
    }

}