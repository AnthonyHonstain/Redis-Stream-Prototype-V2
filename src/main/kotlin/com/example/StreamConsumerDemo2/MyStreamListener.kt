package com.example.StreamConsumerDemo2

import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.stream.StreamListener
import org.springframework.stereotype.Component

@Component
class MyStreamListener(
    var redisTemplate: StringRedisTemplate
): StreamListener<String, MapRecord<String, String, String>> {

    override fun onMessage(message: MapRecord<String, String, String>) {
        println("id: ${message.id} stream: ${message.stream} value: ${message.value}")

        redisTemplate.opsForStream<String, String>().acknowledge("mygroup", message)
    }
}