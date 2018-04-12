package com.rdebokx.pipelineshowcase.actors

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestActors
import akka.testkit.TestKit
import com.nhaarman.mockito_kotlin.mock
import com.rdebokx.pipelineshowcase.Greeting
import org.springframework.kafka.core.KafkaTemplate
import kotlin.test.*
import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit


class GreeterActorTest: TestKit(system) {




    @Test
    fun `Greeting forwarded to LogActor and WordActor`(){
        val logActor = system.actorOf(TestActors.echoActorProps())
        val prefix = "Test prefix "
        val kafkaMock = mock<KafkaTemplate<String, String>>()
        val greetingActor = system.actorOf(Props.create(GreeterActor::class.java, prefix, logActor, kafkaMock))

        val testGreeting = Greeting(42, "A test message!")

        greetingActor.tell(testGreeting, ActorRef.noSender())

        //TODO: debug
        expectMsg(FiniteDuration(3, TimeUnit.SECONDS), prefix + testGreeting.content)




        fail("TODO")

    }

    companion object {

        val system = ActorSystem.create()

        @AfterTest
        fun shutDown() {
            TestKit.shutdownActorSystem(system, Duration.create("3 seconds"), false)
        }


    }
}