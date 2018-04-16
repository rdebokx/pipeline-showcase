package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.event.Logging
import akka.event.LoggingAdapter
import com.rdebokx.pipelineshowcase.Greeting
import com.rdebokx.pipelineshowcase.KafkaHelper
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

/**
 * WordActor for processing the message in a Greeting and sending split up greetings to the Kafka input-words topic.
 * Upon receiving a Greeting, this actor will
 * - draw 10 random integers withing the bounds of the length of the Greeting message
 * - For each integer, insert a pipe ("|") at that particular index and send it to the input-words Kafka topic.
 */
class WordActor(private val kafka: KafkaTemplate<String, String>, private val randomizer: Random): AbstractActor() {

    /**
     * Logger used by this actor for monitoring purposes.
     */
    private val log: LoggingAdapter = Logging.getLogger(context.system, this)

    /**
     * Last processed Greeting of this actor before it stopped itself. Used for monitoring purposes.
     */
    private var processedMessage: Greeting? = null

    override fun createReceive(): Receive {
        return receiveBuilder().match(Greeting::class.java, { m ->
            log.debug("Akka Received message $m, dispatching 10 random permutations to Kafka for message ${m.id}")
            //TODO: remove:
            //randomizer.setSeed(6424424)
            processedMessage = m
            randomizer.ints(10, 0, m.content.length).toArray()
                .forEachIndexed { i: Int, splitIndex: Int ->
                    kafka.send(
                        KafkaHelper.INPUT_WORDS_TOPIC,
                        m.id.toString() + "-" + i, m.content.substring(0, splitIndex) + "|" + m.content.substring(splitIndex)
                    )
                }
        }).build()
    }

    override fun postStop() {
        log.info("Stopping WordActor after processing message $processedMessage")
        super.postStop()
    }
}