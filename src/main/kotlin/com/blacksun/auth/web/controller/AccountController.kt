package com.blacksun.auth.web.controller

import com.blacksun.auth.entity.Account
import com.blacksun.auth.service.AccountService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Inject
import io.micronaut.http.HttpResponse
import io.micronaut.security.annotation.Secured
import java.security.Principal

@Controller("/auth")
class AccountController
{
    @Inject
    private lateinit var service: AccountService

    @Get("/create")
    fun createAccount(account: Account): HttpResponse<Account>?
    {
        return HttpResponse.ok(service.create(account));
    }

}