package com.rdebokx.pipelineshowcase

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

class KafkaHelper {

    @Bean
    fun producerConfigs(): Map<String, Any> {
        return mapOf(
                //TODO: bootstrap servers config?
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to getServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        )
    }

    private fun getServers(): String {
        //TODO: load all server property files and concatenate to a list
        return "localhost:9092"
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }

    companion object {
        const val INPUT_WORDS_TOPIC = "input-words"
        const val WORDS_LOGGING_TOPIC = "words-logging"
    }
}