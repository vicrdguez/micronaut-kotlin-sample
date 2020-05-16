package com.blacksun.auth

import com.blacksun.auth.web.dto.AccountResponse
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.annotation.MicronautTest

@MicronautTest
class AuthorizationSpec : BehaviorSpec({

    Given("A REST client trying access the API")
    {

        val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
        val client: RxHttpClient = RxHttpClient.create(embeddedServer.url)

        When("The client tries to reach the get account endpoint without login")
        {
            val response: HttpResponse<AccountResponse> = client.toBlocking().exchange(HttpRequest.GET<Any>("/6"))
            Then("The server should retun 401 status")
            {
                response.status shouldBe HttpStatus.UNAUTHORIZED
            }
        }

        When("The client login")
        {
            val creds = UsernamePasswordCredentials("sherlock", "password")
            val request = HttpRequest.POST("/login", creds)

            val response: HttpResponse<BearerAccessRefreshToken> = client.toBlocking().exchange(
                request,
                BearerAccessRefreshToken::class.java
            )

            Then("The response should contain the JWT token")
            {
                response.status shouldBe HttpStatus.OK
                response.body()!!.accessToken shouldNotBe null
                (JWTParser.parse(response.body()!!.accessToken) is SignedJWT) shouldBe true
                response.body()!!.refreshToken shouldNotBe null
                (JWTParser.parse(response.body()!!.refreshToken) is SignedJWT) shouldBe true
            }

            And("The user tries to acces the get account endpoint")
            {
                val accessToken = response.body()!!.accessToken
                val requestWithAuthorization = HttpRequest.GET<Any>("/6")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                val response: HttpResponse<AccountResponse> =
                    client.toBlocking().exchange(requestWithAuthorization)

                Then("The user should can access")
                {
                    response.status shouldBe HttpStatus.OK
                    response.body()!!.userName shouldBe "black"
                }
            }
        }
    }
})
{

}
