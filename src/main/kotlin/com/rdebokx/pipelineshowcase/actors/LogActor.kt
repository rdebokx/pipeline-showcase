package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.event.Logging
import akka.event.LoggingAdapter

/**
 * LogActor for logging a greeting.
 * Upon receiving a Greeting, this actor will log the message stored in the Greeting.
 */
class LogActor: AbstractActor() {

    /**
     * Logger used to log the message in the Greeting.
     */
    private val log: LoggingAdapter = Logging.getLogger(context.system, this)

    override fun createReceive(): Receive {
        return receiveBuilder().match(String::class.java, {m -> log.info(m)}).build()
    }
}