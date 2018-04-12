package com.rdebokx.pipelineshowcase

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@Import(ActorHelper::class)
@EnableKafka
class Application

/**
 * Start the Application.
 */
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}