package ru.kharevich.activityservice.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service;
import ru.kharevich.activityservice.dto.response.ActivityResponse

@Service
class ActivityMessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, ActivityResponse>
) {
    @Value("\${spring.kafka.topic.activity}")
    private lateinit var topic: String

    fun sendMessage(message: ActivityResponse) {
        val message: Message<ActivityResponse> = MessageBuilder
            .withPayload(message)
            .setHeader(KafkaHeaders.TOPIC, topic)
            .build()

        kafkaTemplate.send(message)
    }
}
