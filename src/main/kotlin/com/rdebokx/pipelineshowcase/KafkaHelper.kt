package com.rdebokx.pipelineshowcase

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Configuration
@PropertySource("file:config/zookeeper.properties")
class KafkaHelper {


    //This value is pulled from application.properties using autowiring
    @Value("\${clientPort}")
    var clientPort: Int = 0


    companion object {
        const val INPUT_WORDS_TOPIC = "input-words"
        const val WORDS_LOGGING_TOPIC = "words-logging"
    }
    //TODO
}