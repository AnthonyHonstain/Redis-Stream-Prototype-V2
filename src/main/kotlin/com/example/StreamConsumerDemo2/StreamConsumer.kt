package com.example.StreamConsumerDemo2

import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.stream.Consumer
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.ReadOffset
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.stream.StreamMessageListenerContainer
import org.springframework.data.redis.stream.Subscription
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.annotation.PreDestroy

@Component
class StreamConsumer(
    redisConnectionFactory: RedisConnectionFactory,
    streamListener: MyStreamListener,
) {

    final val POLL_TIMEOUT = 1000L

    final var container: StreamMessageListenerContainer<String, MapRecord<String, String, String>>
    final var subscription: Subscription

    init {
        val containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
            .pollTimeout(Duration.ofMillis(POLL_TIMEOUT))
            .build()
        container = StreamMessageListenerContainer.create(redisConnectionFactory, containerOptions)

        val consumer = Consumer.from("mygroup", "Alice")
        subscription = container.receive(
            consumer,
            StreamOffset.create("mystream", ReadOffset.lastConsumed()),
            streamListener
        )
        container.start()
    }

    @PreDestroy
    fun preDestroy() {
        println("PreDestroy subscription - subscription?.isActive: ${subscription.isActive}")

        // Timing how long it takes https://stackoverflow.com/questions/1770010/how-do-i-measure-time-elapsed-in-java
        val startTime = System.nanoTime()

        // Using container.stop() since it already calls subscription.cancel()
        container.stop()
        //subscription.cancel()

        while (subscription.isActive) {
            //println("wait... 10ms")
            Thread.sleep(10)
        }

        val completionTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)
        println("Time required for subscription.isActive==false : $completionTime ms")
        println("PreDestroy subscription - subscription?.isActive: ${subscription.isActive}")
    }
}