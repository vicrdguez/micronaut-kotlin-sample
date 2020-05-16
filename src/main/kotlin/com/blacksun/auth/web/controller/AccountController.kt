package com.blacksun.auth.web.controller

import com.blacksun.auth.service.AccountService
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
import io.reactivex.Flowable
import java.lang.Exception

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/auth")
class AccountController(
    @Inject val service: AuthenticatorService
)
{
    val logger = logger()

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createAccount(@Body account: AccountRequest): HttpResponse<AccountResponse>?
    {
        logger.info("the received request is [{}]", account)

        try
        {
            val result = service.create(account)
            return HttpResponse.created(AccountResponse(result.userName))
        }
        catch (e: Exception)
        {
            return HttpResponse.unprocessableEntity()
        }
    }

    @Get("/{id}")
    fun getAccountName(id: Long): HttpResponse<AccountResponse>?
    {
        return service.read(id).map { t -> HttpResponse.ok(AccountResponse(t.userName)) }
            .orElse(HttpResponse.noContent())
    }
}

