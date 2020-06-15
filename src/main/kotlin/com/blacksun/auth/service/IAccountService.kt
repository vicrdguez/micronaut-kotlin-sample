package com.blacksun.auth.service

import com.blacksun.auth.entity.Account
import com.blacksun.auth.enum.HashAlgorithm
import com.blacksun.auth.utils.PasswordEncoder
import com.blacksun.auth.web.dto.AccountRequest
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UserDetails
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.*

interface IAccountService
{

    fun register(account: AccountRequest): Account

    fun read(id: Long): Optional<Account>

    fun update(account: Account): Account

    fun delete(id: Long)

    fun sendPasswordResetEmail(email: String): Boolean

    fun updatePassword(id: Long, password: String)

    fun updatePassword(id: Long, oldPassword: String, newPassword:String) : Boolean
}