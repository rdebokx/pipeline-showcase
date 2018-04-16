package com.rdebokx.pipelineshowcase.actors

import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.PatternsCS
import akka.testkit.TestKit
import com.nhaarman.mockito_kotlin.*
import com.rdebokx.pipelineshowcase.Greeting
import com.rdebokx.pipelineshowcase.KafkaHelper
import org.junit.Test
import org.springframework.kafka.core.KafkaTemplate
import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest

class WordActorTest: TestKit(system) {

    @Test
    fun `should queue 10 random words to kafka`() {
        val kafkaMock = mock<KafkaTemplate<String, String>>()
        val randomSeed = 6424424L
        val randomizer = Random(randomSeed)
        val wordActor = system.actorOf(Props.create(WordActor::class.java, kafkaMock, randomizer))
        this.watch(wordActor)

        val testGreeting = Greeting(42, "Helloooo, test")
        within(FiniteDuration(3, TimeUnit.SECONDS), {
            PatternsCS.ask(wordActor, testGreeting, 3000).toCompletableFuture()
            verify(kafkaMock, times(10)).send(eq(KafkaHelper.INPUT_WORDS_TOPIC), any(), any())

            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 0, "Helloooo, |test")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 1, "Hel|loooo, test")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 2, "Helloooo, |test")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 3, "Helloooo, tes|t")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 4, "|Helloooo, test")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 5, "Helloooo, |test")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 6, "Hel|loooo, test")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 7, "Helloo|oo, test")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 8, "Helloooo, t|est")
            verify(kafkaMock, times(1)).send(KafkaHelper.INPUT_WORDS_TOPIC, testGreeting.id.toString() + "-" + 9, "Hell|oooo, test")

            expectNoMsg()
        })
    }

    companion object {
        val system = ActorSystem.create("WordActorTestSytem")

        @AfterTest
        fun shutDown() {
            TestKit.shutdownActorSystem(system, Duration.create("3 seconds"), false)
        }
    }
}