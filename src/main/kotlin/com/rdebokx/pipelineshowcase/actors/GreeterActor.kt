package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import com.rdebokx.pipelineshowcase.ActorHelper
import com.rdebokx.pipelineshowcase.Greeting

class GreeterActor(
        private val prefix: String,
        private val logActor: ActorRef): AbstractActor() {

    //TODO: docs
    override fun createReceive(): Receive {
        return receiveBuilder().match(Greeting::class.java, { m ->
            logActor.tell(prefix + m, self)

            //Create new actor for submitting word to Kafka
            context.system.actorOf(Props.create(WordActor::class.java)).tell(m, self)
        }).build()
    }

}