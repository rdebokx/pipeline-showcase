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

    /**
     * Application context, used to access the Actor System.
     */
    @Autowired
    private lateinit var context: ApplicationContext

    /**
     * The ActorSystem bean that was initialized by Spring.
     */
    val actorSystem by lazy { context.getBean(ActorHelper::class.java).actorSystem() }

    /**
     * Counter used to give each Greeting message an id.
     */
    val counter = AtomicLong()

    /**
     * greeting handler. Upon loading this page, a new Greeting will be constructed with the given name
     * and will be sent to the GreeterActor in the ActorSystem to be further logged and processed using Kafka.
     * The result of this function is that the constructed message will be logged and split up in 10 random split versions of the message,
     * this is then sent to Kafka that will log these messages and the inverse of them.
     *
     * @return The Greeting that was created.
     */
    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {

        val greeting = Greeting(counter.incrementAndGet(), "Helloooo, $name")

        logger.info("Sending greeting to GreetingActor in " + actorSystem.name())
        val greeterActor = actorSystem.actorSelection(ActorHelper.getActorPath(ActorHelper.GREETER_ACTOR))
        greeterActor.tell(greeting, ActorRef.noSender())

        return greeting
    }

    companion object {
        /**
         * Logger used by this controller.
         */
        private val logger = LoggerFactory.getLogger(GreetingController::class.java)
    }
}