package com.blacksun.auth

import com.blacksun.auth.web.dto.AccountRequest
import com.blacksun.auth.web.dto.AccountResponse
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.BehaviorSpec

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.*
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.annotation.MicronautTest

const val PASS: String = "blacksun2143"
const val USER: String = "blacksuntest"
const val EMAIL: String = "blacksun@blacksun.com"

@Suppress("NAME_SHADOWING")
@MicronautTest
class AuthorizationSpec : BehaviorSpec({

    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val client: RxHttpClient = RxHttpClient.create(embeddedServer.url)
    var accountId: Long? = null
    var accessToken: String? = null
    Given("A user trying to register with a non existing email and username")
    {
        When("The user request the register")
        {
            val request = HttpRequest.POST("/auth/register", AccountRequest(USER, EMAIL, PASS))
            val response = client.toBlocking().exchange(request, AccountResponse::class.java)
            accountId = response.body()?.id

            Then("The register should succeed")
            {
                response.status shouldBe HttpStatus.CREATED
                response.body()?.userName shouldBe USER
            }
        }

    }
    Given("A REST client trying access the API")
    {

        When("The client tries to reach the get account endpoint without login")
        {
            var responseStatus: HttpStatus? = null
            try
            {
                client.toBlocking().exchange(HttpRequest.GET<Any>("/auth/$accountId"), Argument.OBJECT_ARGUMENT)
            } catch (e: HttpClientResponseException)
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

            And("The user tries to access the get account endpoint")
            {
                accessToken = bearerResponse.body()!!.accessToken
                val requestWithAuthorization = HttpRequest.GET<Any>("/auth/$accountId")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                val accountResponse: HttpResponse<AccountResponse> = client
                        .toBlocking()
                        .exchange(requestWithAuthorization, AccountResponse::class.java)

                Then("The user should can access")
                {
                    accountResponse.status shouldBe HttpStatus.OK
                    accountResponse.body()!!.userName shouldBe USER
                }
            }

            And("The user wants to delete his account")
            {
                val request: MutableHttpRequest<Any> = HttpRequest.DELETE<Any>("/auth/delete/$accountId")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                val response = client.toBlocking().exchange(request, Argument.STRING)
                Then("His data should be delted")
                {
                    response.status shouldBe HttpStatus.OK
                }
            }
        }

    }
})