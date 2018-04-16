package com.rdebokx.pipelineshowcase

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.TypedActor
import com.rdebokx.pipelineshowcase.actors.GreeterActor
import com.rdebokx.pipelineshowcase.actors.LogActor
import com.rdebokx.pipelineshowcase.actors.WordActor
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

@Configuration
@ComponentScan("com.rdebokx.pipelineshowcase")
class ActorHelper {

    companion object {

        /**
         * Name of the ActorSystem that will be initialized.
         */
        const val ACTOR_SYSTEM = "PipelineActorSystem"

        /**
         * Name of the GreetingActor that will be initialized.
         */
        const val GREETER_ACTOR = "greeterActor"

        /**
         * The prefix of the WordActors that will be initiated.
         */
        const val WORD_ACTOR_PREFIX = "wordActor-"

        /**
         * Helper function for constructing the absolute akka path to an actor of the given name in the Actor System that was set up by this helper.
         * @param actorName The name of the actor for which the akka path is requested.
         */
        fun getActorPath(actorName: String) = getActorPath(ACTOR_SYSTEM, actorName)

        /**
         * Helper function for constructing the absolute akka path to an actor of the given name in the given actor system.
         * @param actorSystem The name of the actor system in which an actor is requested.
         * @param actorName The name of the actor for which the akk path is requested. May contain wildcards.
         */
        fun getActorPath(actorSystem: String, actorName: String) = "akka://$actorSystem/user/$actorName"

        /**
         * Logger used by this helper.
         */
        private val logger = LoggerFactory.getLogger(ActorHelper::class.java)
    }

    /**
     * The port on which akka will be running. Pulled from application.properties.
     */
    @Value("\${akka.remote.netty.tcp.port}")
    var akkaPort: Int = 0

    /**
     * The hostname on which akka will be running. Pulled from application.properties.
     */
    @Value("\${akka.remote.netty.tcp.hostname}")
    var akkaHostName: String = ""

    /**
     * The KafkaTemplate instantiated by Spring that the actors will be created in.
     */
    @Autowired
    lateinit var kafka: KafkaTemplate<String, String>

    /**
     * Bean function for creating an Actor System for the application, including a GreetingActor and a LogActor.
     * Actor system will be running on the hostname and port configured in application.properties.
     */
    @Bean
    fun actorSystem(): ActorSystem {
        logger.info("Starting Actor System at $akkaHostName:$akkaPort")
        val defaultApplication: Config = ConfigFactory.defaultApplication()
                .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(akkaHostName))
                .withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(akkaPort))
        val system = ActorSystem.create(ACTOR_SYSTEM, defaultApplication)

        val logActor = system.actorOf(Props.create(LogActor::class.java))
        system.actorOf(Props.create(GreeterActor::class.java, "GreeterLogger1 logs: ", logActor, kafka), GREETER_ACTOR)
        for(i in 0 .. 5){
            system.actorOf(Props.create(WordActor::class.java, kafka, Random()), WORD_ACTOR_PREFIX + i)
        }
        return system
    }

}
