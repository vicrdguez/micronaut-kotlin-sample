package com.blacksun.auth.web.controller

import com.blacksun.auth.service.AccountService
import com.blacksun.auth.utils.logger
import com.blacksun.auth.web.dto.AccountRequest
import com.blacksun.auth.web.dto.AccountResponse
import io.micronaut.http.HttpRequest
import javax.inject.Inject
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
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
        @Inject private val service: AccountService
)
{
    val logger = logger()

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createAccount(@Body account: AccountRequest): HttpResponse<AccountResponse>
    {
        logger.info("the received request is [{}]", account)
        val result = service.create(account)

        return HttpResponse.created(AccountResponse(result.id, result.userName))
    }

    @Get("/{id}")
    fun getAccountName(id: Long): HttpResponse<AccountResponse>?
    {
        return service
                .read(id)
                .map { account -> HttpResponse.ok(AccountResponse(account.id, account.userName)) }
                .orElse(HttpResponse.noContent())
    }

    @Delete("/delete/{id}")
    fun deleteAccount(id: Long): HttpResponse<String>
    {
        service.delete(id)
        return HttpResponse.ok("Account deleted successfully")
    }

    @Post("/reset/password/email")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createResetRequest(email: String): HttpResponse<Boolean>?
    {
        return HttpResponse.ok(service.sendPasswordResetEmail(email))
    }

    @Post("/reset/password")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun resetPassword(id: Long, password: String): HttpResponse<Unit>?
    {
        return HttpResponse.ok(service.updatePassword(id, password))
    }

    @Error
    fun onRegisterError(request: HttpRequest<AccountRequest>, exception: ConstraintViolationException): HttpResponse<String>
    {
        return when (exception.constraintName)
        {
            "account_email_key" -> HttpResponse.badRequest("The email already exists [${request.body.get().email}]")
            "account_username_key" -> HttpResponse.badRequest("The username already exists [${request.body.get().userName}]")
            else -> HttpResponse.unprocessableEntity()
        }
    }
}

