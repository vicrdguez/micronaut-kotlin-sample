package com.blacksun.auth.repository

import com.blacksun.auth.entity.Account
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface IAccountRepository : CrudRepository<Account, Long>{
    fun findByUserName(userName : String) : Optional<Account>
}
