package com.chattingexcercis.sewonyun.application.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // chat client will use this to connect to the server
        registry.addEndpoint("/ws-chat-pub")
            .setAllowedOrigins("http://localhost:3000")
            .withSockJS()

        registry.addEndpoint("/ws-chat-sub")
            .setAllowedOrigins("http://localhost:3000")
            .withSockJS()

        registry.addEndpoint("/ws-list-sub")
            .setAllowedOrigins("http://localhost:3000")
            .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableSimpleBroker("/topic/")
    }
}