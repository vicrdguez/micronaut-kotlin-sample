package com.blacksun.auth

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import io.kotlintest.specs.FeatureSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest

@Suppress("BlockingMethodInNonBlockingContext")
@MicronautTest
class PasswordResetSpec : FeatureSpec({

    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val client: RxHttpClient = RxHttpClient.create(embeddedServer.url)
    val mapper = ObjectMapper()

    feature("reset Password endpoint")
    {
        scenario("Should find the user because the email exists in db")
        {
            val body = "{\"email\": \"blacksun@blacksun.com\"}"

            val json = mapper.readTree(body)

            val request = HttpRequest.POST("/auth/reset/password/email", json)
            val response = client.toBlocking().exchange(request, JsonNode::class.java)

            response.status shouldBe HttpStatus.OK
            response.body()?.booleanValue() shouldBe true
        }

        scenario("Should not find the user because the email not exists in db")
        {
            val body = "{\"email\": \"blacksun2@blacksun.com\"}"
            val json = mapper.readTree(body)

            val request = HttpRequest.POST("/auth/reset/password/email", json)
            val response = client.toBlocking().exchange(request, JsonNode::class.java)

            response.status shouldBe HttpStatus.OK
            response.body()?.booleanValue() shouldBe false
        }

        scenario("Updates the password with success")
        {
            val body = "{\"id\": 4, \"password\": \"hello\"}"
            val json = ObjectMapper().readTree(body)

            val request = HttpRequest.POST("/auth/reset/password", json)
            val response = client.toBlocking().exchange(request, JsonNode::class.java)

            response.status shouldBe HttpStatus.OK
        }

    }
})
