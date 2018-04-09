package com.rdebokx.pipelineshowcase

import akka.actor.ActorRef
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class GreetingController {

    @Autowired
    private lateinit var context: ApplicationContext

    val actorSystem by lazy { context.getBean(ActorHelper::class.java).actorSystem() }

    val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {

        val greeting = Greeting(counter.incrementAndGet(), "Helloooo, $name")

        logger.info("Sending greeting to GreetingActor in " + actorSystem.name())
        val greeterActor = actorSystem.actorSelection(ActorHelper.getActorPath(ActorHelper.GREETER_ACTOR))
        greeterActor.tell(greeting, ActorRef.noSender())

        return greeting
    }


}

private val logger = LoggerFactory.getLogger(GreetingController::class.java)