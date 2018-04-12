package com.rdebokx.pipelineshowcase.kafka

import com.rdebokx.pipelineshowcase.KafkaHelper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class RandomWordProcessor {

    /**
     * The KafkaTemplate initiated by Spring, to be used to send messages to the Kafka topics.
     */
    @Autowired
    lateinit var kafka: KafkaTemplate<String, String>

    /**
     * Consumer function for the input-words topic. For each received record, this Processor will submit the
     * received message and its inverse to the words-logging topic.
     */
    @KafkaListener(topics = arrayOf(KafkaHelper.INPUT_WORDS_TOPIC))
    fun receive(consumerRecord: ConsumerRecord<String?, String?>) {
        val key = consumerRecord.key() ?: "[noKey]"
        val value = consumerRecord.value() ?: "[noValue]"
        logger.info("Kafka Processor Received message in ${KafkaHelper.INPUT_WORDS_TOPIC}: $key->$value")
        //Put both the original and the reversed word in the logging queue
        kafka.send(KafkaHelper.WORDS_LOGGING_TOPIC, value)
        kafka.send(KafkaHelper.WORDS_LOGGING_TOPIC, value.reversed())
    }

    companion object {
        /**
         * Logger used by this Processor for monitoring purposes.
         */
        val logger = LoggerFactory.getLogger(RandomWordProcessor::class.java)
    }


}