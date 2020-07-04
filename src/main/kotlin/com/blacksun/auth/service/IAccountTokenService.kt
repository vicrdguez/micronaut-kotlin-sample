package com.blacksun.auth.service

import com.blacksun.auth.entity.AccountToken
import java.util.*

interface IAccountTokenService
{
    fun save(accountId: Long, token: String)
    fun read(id: Long): Optional<AccountToken>
}