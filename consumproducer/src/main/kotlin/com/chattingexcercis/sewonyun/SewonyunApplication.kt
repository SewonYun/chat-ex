package com.chattingexcercis.sewonyun

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka


@EnableKafka
@SpringBootApplication
class SewonyunApplication

fun main(args: Array<String>) {
    runApplication<SewonyunApplication>(*args)
}
