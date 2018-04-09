package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.event.Logging
import akka.event.LoggingAdapter

class LogActor: AbstractActor() {

    private val log: LoggingAdapter = Logging.getLogger(context.system, this)

    override fun createReceive(): Receive {
        return receiveBuilder().match(String::class.java, {m -> log.info(m)}).build()
    }
}