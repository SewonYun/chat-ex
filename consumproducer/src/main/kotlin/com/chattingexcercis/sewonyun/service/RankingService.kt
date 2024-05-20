package com.chattingexcercis.sewonyun.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service

@Service
class RankingService {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Any>

    fun addToRanking(userId: String, score: Double) {
        val operations: ZSetOperations<String, Any> = redisTemplate.opsForZSet()
        operations.add("ranking", userId, score)
    }

    fun getRanking(): List<Pair<String, Double?>>? {
        val operations: ZSetOperations<String, Any> = redisTemplate.opsForZSet()
        val ranking = operations.reverseRangeWithScores("ranking", 0, -1)
        return ranking?.map { Pair(it.value as String, it.score) }
    }

}