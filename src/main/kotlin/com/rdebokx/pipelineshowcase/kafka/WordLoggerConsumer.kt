package com.rdebokx.pipelineshowcase.kafka

import com.rdebokx.pipelineshowcase.KafkaHelper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class WordLoggerConsumer {

    @KafkaListener(topics = arrayOf(KafkaHelper.WORDS_LOGGING_TOPIC))
    fun receive(consumerRecord: ConsumerRecord<String?, String?>) {
        val value = consumerRecord.value() ?: "[noValue]"
        logger.info("Logging Word ===> $value")
    }

    companion object {
        val logger = LoggerFactory.getLogger(WordLoggerConsumer::class.java)
    }


}