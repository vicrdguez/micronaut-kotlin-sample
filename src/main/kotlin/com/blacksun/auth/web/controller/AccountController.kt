package com.blacksun.auth.web.controller

import com.blacksun.auth.service.AuthenticatorService
import com.blacksun.auth.utils.logger
import com.blacksun.auth.web.dto.AccountRequest
import com.blacksun.auth.web.dto.AccountResponse
import io.micronaut.http.annotation.Controller
import javax.inject.Inject
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.hibernate.exception.ConstraintViolationException
import java.lang.Exception
import javax.persistence.PersistenceException

//main controller
@Controller("/auth")
@Secured(SecurityRule.IS_AUTHENTICATED)
class AccountController(
        @Inject val service: AuthenticatorService
)
{
    val logger = logger()

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createAccount(@Body account: AccountRequest): HttpResponse<Any>?
    {
        logger.info("the received request is [{}]", account)

        return try
        {
            val result = service.create(account)
            HttpResponse.created(AccountResponse(result.userName))
        } catch (e: PersistenceException)
        {
            val innerException = e.cause as ConstraintViolationException
            return when (innerException.constraintName)
            {
                "account_email_key" -> HttpResponse.badRequest("The email already exists")
                "account_username_key" -> HttpResponse.badRequest("The username already exists")
                else -> HttpResponse.unprocessableEntity()
            }
        } catch (e: Exception)
        {
            HttpResponse.serverError(e.message)
        }
    }

    @Get("/{id}")
    fun getAccountName(id: Long): HttpResponse<AccountResponse>?
    {
        return service.read(id).map { t -> HttpResponse.ok(AccountResponse(t.userName)) }
                .orElse(HttpResponse.noContent())
    }
}

