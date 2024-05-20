package com.chattingexcercis.sewonyun.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.chattingexcercis.sewonyun.application.domain.User
import com.chattingexcercis.sewonyun.application.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserJoinService(private var userRepository: UserRepository) {

    fun saveUser(nickName: String): Either<String, User> {
        val user = User(nickName = nickName)
        return try {
            userRepository.save(user).right()
        } catch (ex: Exception) {
            "가입에 실패했습니다.".left()
        }
    }

}