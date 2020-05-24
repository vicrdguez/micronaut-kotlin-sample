package com.blacksun.auth.web.controller

import com.blacksun.auth.service.AccountService
import com.blacksun.auth.utils.logger
import com.blacksun.auth.web.dto.AccountRequest
import com.blacksun.auth.web.dto.AccountResponse
import io.micronaut.http.annotation.Controller
import javax.inject.Inject
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.hibernate.exception.ConstraintViolationException
import java.lang.Exception
import javax.persistence.PersistenceException

/**
 * Account controller
 *
 * @author vicrdguez
 */
@Controller("/auth")
@Secured(SecurityRule.IS_AUTHENTICATED)
class AccountController(
        @Inject val service: AccountService
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

    @Post("/reset/password/email")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createResetRequest(email: String): HttpResponse<Boolean>?
    {
        return HttpResponse.ok(service.sendPasswordResetEmail(email))
    }

    @Post("/reset/password")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun resetPassword(id: Long, password :String): HttpResponse<Unit>?
    {
        return HttpResponse.ok(service.updatePassword(id, password))
    }
}

