package com.rdebokx.pipelineshowcase.kafka

import com.rdebokx.pipelineshowcase.KafkaHelper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class WordLoggerConsumer {

    /**
     * Consumer function for the words-logging topic. Upon receiving a record, this consumer will log the message
     * in this record.
     */
    @KafkaListener(topics = arrayOf(KafkaHelper.WORDS_LOGGING_TOPIC))
    fun receive(consumerRecord: ConsumerRecord<String?, String?>) {
        val value = consumerRecord.value() ?: "[noValue]"
        logger.info("Kafka Logging Word ===> $value")
    }

    companion object {
        /**
         * Logger used by this Consumer for logging received records.
         */
        val logger = LoggerFactory.getLogger(WordLoggerConsumer::class.java)
    }


}