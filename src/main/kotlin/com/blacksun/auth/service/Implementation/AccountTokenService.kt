package com.blacksun.auth.service.Implementation

import com.blacksun.auth.entity.AccountToken
import com.blacksun.auth.repository.IAccountTokenRepository
import com.blacksun.auth.service.IAccountService
import com.blacksun.auth.service.IAccountTokenService
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountTokenService(
        @Inject private val repository: IAccountTokenRepository
) : IAccountTokenService
{
    override fun save(accountId: Long, token: String)
    {
        repository.save(AccountToken(null, accountId, token, 86400))
    }

    override fun read(id: Long): Optional<AccountToken>
    {
        return repository.findByAccountId(id)
    }
}