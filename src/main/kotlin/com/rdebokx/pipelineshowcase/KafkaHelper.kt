package com.rdebokx.pipelineshowcase

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

class KafkaHelper {

    /**
     * Bean function for constructing a config bean, eventually required for the KafkaTemplate bean.
     */
    @Bean
    fun producerConfigs(): Map<String, Any> {
        return mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to getServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        )
    }

    private fun getServers(): String {
        //TODO: load all server property files and concatenate to a list
        return "localhost:9092"
    }

    /**
     * Bean function for creating a ProducerFactory using the configuration bean. Will be used for
     * constructing the KafkaTemplate bean
     */
    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    /**
     * Bean function for constructing a KafkaTemplate bean, which can be used to interact with Kafka.
     */
    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }

    companion object {
        /**
         * Name of the input-words topic
         */
        const val INPUT_WORDS_TOPIC = "input-words"

        /**
         * Name of the words-logging topic
         */
        const val WORDS_LOGGING_TOPIC = "words-logging"
    }
}