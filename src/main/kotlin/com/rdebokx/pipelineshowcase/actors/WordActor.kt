package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.event.Logging
import akka.event.LoggingAdapter
import com.rdebokx.pipelineshowcase.KafkaHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

class WordActor: AbstractActor() {

    private val log: LoggingAdapter = Logging.getLogger(context.system, this)

    private val randomizer = Random()

    private var processedMessage: String? = null

    @Autowired
    lateinit var kafka: KafkaTemplate<String, String>

    override fun createReceive(): Receive {
        return receiveBuilder().match(String::class.java, {m ->
            log.debug("Akka Received message $m, dispatching 10 random permutations to Kafka")
            randomizer.ints(10, m.length)
                .forEach { splitIndex ->
                    kafka.send(
                        KafkaHelper.INPUT_WORDS_TOPIC,
                        m.substring(0, splitIndex) + "|", m.substring(splitIndex)
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