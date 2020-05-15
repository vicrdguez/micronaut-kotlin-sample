package com.blacksun.auth.service

import com.blacksun.auth.entity.Account
import com.blacksun.auth.repository.IAccountRepository
import java.util.*
import javax.inject.Inject

class AccountService()
{

    @Inject
    private lateinit var repository: IAccountRepository;

    fun create(account: Account): Account
    {
        return repository.save(account);
    }

    fun read(id: Long): Optional<Account>
    {
        return repository.findById(id);
    }

    fun update(account: Account) : Account{
        return repository.update(account);
    }

    fun delete(account: Account) {
        return repository.delete(account);
    }
}