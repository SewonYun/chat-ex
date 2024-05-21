package com.chattingexcercis.sewonyun

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.scheduling.annotation.EnableScheduling


@EnableKafka
@EnableScheduling
@SpringBootApplication
class SewonyunApplication

fun main(args: Array<String>) {
    runApplication<SewonyunApplication>(*args)
}
