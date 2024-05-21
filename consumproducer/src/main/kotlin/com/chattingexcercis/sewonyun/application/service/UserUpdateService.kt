package com.chattingexcercis.sewonyun.application.service

import arrow.core.flatMap
import com.chattingexcercis.sewonyun.application.domain.User
import com.chattingexcercis.sewonyun.application.repository.UserRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class UserUpdateService(private var userRepository: UserRepository) {

    fun saveUser(user: User): Result<User> {
        return runCatching { userRepository.save(user) }
    }

    fun updateActive(userId: Long): Result<User> {
        return runCatching { userRepository.findById(userId).get() }
            .map { user -> user.copy(lastActiveAt = Timestamp(System.currentTimeMillis())) }
            .flatMap { user -> saveUser(user) }
    }


}