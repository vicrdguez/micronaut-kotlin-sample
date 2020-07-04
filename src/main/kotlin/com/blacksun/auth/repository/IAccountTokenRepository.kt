package com.blacksun.auth.repository

import com.blacksun.auth.entity.AccountToken
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface IAccountTokenRepository: CrudRepository<AccountToken, Long>
{
    fun findByAccountId(accountId: Long): Optional<AccountToken>
}