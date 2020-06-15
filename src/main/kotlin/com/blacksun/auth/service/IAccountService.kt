package com.blacksun.auth.service

import com.blacksun.auth.entity.Account
import com.blacksun.auth.web.dto.AccountRequest
import java.util.*

interface IAccountService
{

    fun register(account: AccountRequest): Account

    fun read(id: Long): Optional<Account>

    fun update(account: Account): Account

    fun delete(id: Long)

    fun sendPasswordResetEmail(email: String): Boolean

    fun sendValidationEmail(email:String): Boolean

    fun updatePassword(id: Long, password:String)

    fun updatePassword(id: Long, oldPassword: String, newPassword:String) : Boolean
}