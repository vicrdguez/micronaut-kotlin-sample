package com.blacksun.auth

import com.blacksun.auth.web.dto.AccountRequest
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer

class UniqueConstraintSpec : BehaviorSpec({

    Given("A user trying to register")
    {
        val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
        val client: RxHttpClient = RxHttpClient.create(embeddedServer.url)
        When("The user use an existing email in db")
        {

            Then("The endpoint should return badResponse error and show the cause")
            {
                val request = HttpRequest.POST(
                        "/auth/register",
                        AccountRequest("blacksuntest", "blacksun@blacksun.com", "blacksun2143"))
                try
                {
                    client.toBlocking().exchange(request, Argument.OBJECT_ARGUMENT)
                }
                catch (e: HttpClientResponseException)
                {
                    e.status shouldBe HttpStatus.BAD_REQUEST
                }

            }
        }
        When("The user use an existing username in db")
        {
            Then("The endpoint should return badResponse error and show the cause")
            {
                val request = HttpRequest.POST(
                        "/auth/register",
                        AccountRequest("blacksuntest", "blacksun1@blacksun.com", "blacksun2143"))
                try
                {
                    client.toBlocking().exchange(request, Argument.OBJECT_ARGUMENT)
                }
                catch (e: HttpClientResponseException)
                {
                    e.status shouldBe HttpStatus.BAD_REQUEST
                }

            }
        }
    }
})
{
}