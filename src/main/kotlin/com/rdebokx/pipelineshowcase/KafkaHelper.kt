package com.rdebokx.pipelineshowcase

import org.slf4j.LoggerFactory

class KafkaHelper {

    companion object {
        /**
         * Name of the input-words topic
         */
        const val INPUT_WORDS_TOPIC = "input-words"

        /**
         * Name of the words-logging topic
         */
        const val WORDS_LOGGING_TOPIC = "words-logging"

        val logger = LoggerFactory.getLogger(KafkaHelper::class.java)
    }
}