package com.rdebokx.pipelineshowcase.actors

import akka.actor.AbstractActor
import akka.actor.ActorPath
import akka.actor.ActorRef
import akka.actor.Props
import akka.event.Logging
import akka.event.LoggingAdapter
import com.rdebokx.pipelineshowcase.ActorHelper
import com.rdebokx.pipelineshowcase.Greeting
import org.springframework.kafka.core.KafkaTemplate

/**
 * GreetingActor for processing a Greeting. Upon receiving a Greeting, this actor will
 * - Delegate the logging to the logActor of this GreetingActor.
 * - Spin up a new WordActor and delegate the received Greeting to this WordActor.
 */
class GreeterActor(
        private val prefix: String,
        private val logActor: ActorRef,
        private val kafka: KafkaTemplate<String, String>): AbstractActor() {

    override fun createReceive(): Receive {
        return receiveBuilder().match(Greeting::class.java, { m ->
            logActor.tell(prefix + m.content, self)

            //Create new actor for submitting word to Kafka
            context.system.actorSelection(ActorHelper.getActorPath(context.system.name(), "${ActorHelper.WORD_ACTOR_PREFIX}*")).tell(m, self)
        }).build()
    }

}