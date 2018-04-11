package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import akka.event.Logging
import akka.event.LoggingAdapter
import com.rdebokx.pipelineshowcase.Greeting
import org.springframework.kafka.core.KafkaTemplate

class GreeterActor(
        private val prefix: String,
        private val logActor: ActorRef,
        private val kafka: KafkaTemplate<String, String>): AbstractActor() {

    //TODO: docs
    override fun createReceive(): Receive {
        return receiveBuilder().match(Greeting::class.java, { m ->
            logActor.tell(prefix + m, self)

            //Create new actor for submitting word to Kafka
            context.system.actorOf(Props.create(WordActor::class.java, kafka)).tell(m, self)
        }).build()
    }

}