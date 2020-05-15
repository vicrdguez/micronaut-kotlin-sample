package com.blacksun.auth.service

import com.blacksun.auth.entity.Account
import com.blacksun.auth.repository.IAccountRepository
import com.blacksun.auth.web.dto.AccountRequest
import java.util.*


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountService(
    @Inject val repository: IAccountRepository
)
{

    fun create(account: AccountRequest): Account
    {

        return repository.save(Account(null, account.userName, account.email, account.password, null, null, null))
    }

    fun read(id: Long): Optional<Account>
    {
        return repository.findById(id)
    }

    fun update(account: Account): Account
    {
        return repository.update(account)
    }

    fun delete(account: Account)
    {
        return repository.delete(account)
    }
}