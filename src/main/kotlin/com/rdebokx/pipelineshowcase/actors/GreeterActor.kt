package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.actor.ActorRef
import com.rdebokx.pipelineshowcase.Greeting

class GreeterActor(
        private val prefix: String,
        private val logActor: ActorRef): AbstractActor() {

    //TODO: docs
    override fun createReceive(): Receive {
        return receiveBuilder().match(Greeting::class.java, { m -> logActor.tell(prefix + m, self)}).build()
    }

}