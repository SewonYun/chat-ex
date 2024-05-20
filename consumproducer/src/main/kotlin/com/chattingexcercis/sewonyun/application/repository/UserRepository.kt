package com.chattingexcercis.sewonyun.application.repository

import com.chattingexcercis.sewonyun.application.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}