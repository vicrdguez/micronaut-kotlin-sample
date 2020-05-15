package com.blacksun.auth.web.controller

import com.blacksun.auth.service.AccountService
import com.blacksun.auth.utils.logger
import com.blacksun.auth.web.dto.AccountRequest
import com.blacksun.auth.web.dto.AccountResponse
import io.micronaut.http.annotation.Controller
import javax.inject.Inject
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule


@Controller("/auth")
class AccountController(
    @Inject val service: AccountService
)
{
    val logger = logger()

    @Post("/register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createAccount(@Body account: AccountRequest): HttpResponse<AccountResponse>?
    {
        logger.info("the received request is [{}]", account)
        val result = service.create(account)

        return HttpResponse.ok(AccountResponse(result.userName))
    }

}
