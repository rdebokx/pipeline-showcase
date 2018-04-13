package com.rdebokx.pipelineshowcase.actors

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestActors
import akka.testkit.TestKit
import akka.testkit.TestProbe
import com.nhaarman.mockito_kotlin.*
import com.rdebokx.pipelineshowcase.ActorHelper
import com.rdebokx.pipelineshowcase.Greeting
import org.mockito.ArgumentMatchers
import org.springframework.kafka.core.KafkaTemplate
import kotlin.test.*
import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit


class GreeterActorTest: TestKit(system) {

    @Test
    fun `Greeting forwarded to LogActor and WordActor`(){
        //Return mock wordActor when created by GreetingActor
        val kafkaMock = mock<KafkaTemplate<String, String>>()

        //Create greetingActor with stubbed LogActor and kafkaMock.
        val logActor = TestProbe(system)
        val prefix = "Test prefix "
        val greetingActor = system.actorOf(Props.create(GreeterActor::class.java, prefix, logActor.ref(), kafkaMock))

        val wordActor = TestProbe(system)
        system.actorOf(TestActors.forwardActorProps(wordActor.ref()), ActorHelper.WORD_ACTOR_PREFIX + "1")

        val testGreeting = Greeting(42, "A test message!")
        greetingActor.tell(testGreeting, ActorRef.noSender())

        //Test that logActor was called
        logActor.expectMsg(FiniteDuration(3, TimeUnit.SECONDS), prefix + testGreeting.content)
        //Test that wordActor was created and called
        wordActor.expectMsg(FiniteDuration(3, TimeUnit.SECONDS), testGreeting)
    }

    companion object {

        val system = ActorSystem.create("GreetingActorTestSystem")

        @AfterTest
        fun shutDown() {
            TestKit.shutdownActorSystem(system, Duration.create("3 seconds"), false)
        }
    }
}