package com.rdebokx.pipelineshowcase

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(ActorHelper::class)
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}