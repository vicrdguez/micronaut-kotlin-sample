package com.blacksun.auth

import com.blacksun.auth.web.dto.AccountResponse
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.BehaviorSpec

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.annotation.MicronautTest

const val PASS: String = "51uctSQVgFS75g3GcWe4QBby0hQ54DnX9iDMcCFCcQw+SWJHClRkjxT9TkZ3yuJxTOIXK/3lfZGKzxHsE4pCxw=="
const val USER: String = "blacksuntest"

@MicronautTest
class AuthorizationSpec : BehaviorSpec({

    Given("A REST client trying access the API")
    {

        val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
        val client: RxHttpClient = RxHttpClient.create(embeddedServer.url)

        When("The client tries to reach the get account endpoint without login")
        {
            var responseStatus: HttpStatus? = null
            try
            {
                client.toBlocking().exchange(HttpRequest.GET<Any>("/auth/4"), Argument.OBJECT_ARGUMENT)
            }
            catch (e: HttpClientResponseException)
            {
                responseStatus = e.status
            }

            Then("The server should retun 401 status")
            {
                responseStatus shouldBe HttpStatus.UNAUTHORIZED
            }
        }

        When("The client login")
        {
            val creds = UsernamePasswordCredentials(USER, PASS)
            val request = HttpRequest.POST("/login", creds)

            val bearerResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking().exchange(
                    request,
                    BearerAccessRefreshToken::class.java
            )

            Then("The response should contain the JWT token")
            {
                bearerResponse.status shouldBe HttpStatus.OK
                bearerResponse.body()!!.accessToken shouldNotBe null
                (JWTParser.parse(bearerResponse.body()!!.accessToken) is SignedJWT) shouldBe true
                bearerResponse.body()!!.refreshToken shouldNotBe null
                (JWTParser.parse(bearerResponse.body()!!.refreshToken) is SignedJWT) shouldBe true
            }

            And("The user tries to acces the get account endpoint")
            {
                val accessToken = bearerResponse.body()!!.accessToken
                val requestWithAuthorization = HttpRequest.GET<Any>("/auth/4")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                val accountResponse: HttpResponse<AccountResponse> = client
                        .toBlocking()
                        .exchange(requestWithAuthorization, AccountResponse::class.java)

                Then("The user should can access")
                {
                    accountResponse.status shouldBe HttpStatus.OK
                    accountResponse.body()!!.userName shouldBe "blacksuntest"
                }
            }
        }
    }
})

