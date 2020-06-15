package com.blacksun.auth.web.client

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import javax.validation.constraints.NotBlank

@Client(id = "mail-api")
interface IMailClient
{
    @Post("/mail/validation/{email}")
    fun sendValidationEmail(@NotBlank email: String): HttpResponse<Any>

    @Post("/mail/password/reset/{email}")
    fun sendPasswordResetEmail(@NotBlank email: String): HttpResponse<Any>
}