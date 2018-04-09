package com.rdebokx.pipelineshowcase

import akka.actor.ActorSystem
import akka.actor.Props
import com.rdebokx.pipelineshowcase.actors.GreeterActor
import com.rdebokx.pipelineshowcase.actors.LogActor
import com.rdebokx.pipelineshowcase.actors.WordActor
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("com.rdebokx.pipelineshowcase")
open class ActorHelper {

    companion object {
        const val ACTOR_SYSTEM = "PipelineActorSystem"
        const val GREETER_ACTOR = "greeterActor"
        const val WORD_ACTOR = "wordActor"

        fun getActorPath(actorName: String) = "akka://$ACTOR_SYSTEM/user/$actorName"
    }

    //This value is pulled from application.properties using autowiring
    @Value("\${akka.remote.netty.tcp.port}")
    var akkaPort: Int = 0

    //This value is pulled from applicaiton.properties using autowiring
    @Value("\${akka.remote.netty.tcp.hostname}")
    var akkaHostName: String = ""

    @Bean
    open fun actorSystem(): ActorSystem {
        logger.info("Starting Actor System at $akkaHostName:$akkaPort")
        val defaultApplication: Config = ConfigFactory.defaultApplication()
                .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(akkaHostName))
                .withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(akkaPort))
        val system = ActorSystem.create(ACTOR_SYSTEM, defaultApplication)

        val logActor = system.actorOf(Props.create(LogActor::class.java))
        system.actorOf(Props.create(GreeterActor::class.java, "GreeterLogger1 logs: ", logActor), GREETER_ACTOR)
        return system
    }

}

private val logger = LoggerFactory.getLogger(ActorHelper::class.java)
