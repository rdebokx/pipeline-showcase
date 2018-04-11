package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.event.Logging
import akka.event.LoggingAdapter
import com.rdebokx.pipelineshowcase.Greeting
import com.rdebokx.pipelineshowcase.KafkaHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.*

class WordActor(private val kafka: KafkaTemplate<String, String>): AbstractActor() {

    private val log: LoggingAdapter = Logging.getLogger(context.system, this)

    private val randomizer = Random()

    private var processedMessage: Greeting? = null

    override fun createReceive(): Receive {
        return receiveBuilder().match(Greeting::class.java, { m ->
            log.debug("Akka Received message $m, dispatching 10 random permutations to Kafka for message ${m.id}")
            processedMessage = m
            randomizer.ints(10, 0, m.content.length).toArray()
                .forEachIndexed { i: Int, splitIndex: Int ->
                    kafka.send(
                        KafkaHelper.INPUT_WORDS_TOPIC,
                        m.id.toString() + "-" + i, m.content.substring(0, splitIndex) + "|" + m.content.substring(splitIndex)
                    )
                }
            //Terminate this actor
            context.stop(self)
        }).build()
    }

    override fun postStop() {
        log.info("Stopping WordActor after processing message $processedMessage")
        super.postStop()
    }
}