package com.chattingexcercis.sewonyun

import com.chattingexcercis.sewonyun.service.TopicConsumer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka


@EnableKafka
@SpringBootApplication
class SewonyunApplication

fun main(args: Array<String>) {
    runApplication<SewonyunApplication>(*args)
}

@Bean
fun kafkaConsumer(): TopicConsumer {
    return TopicConsumer()
}
