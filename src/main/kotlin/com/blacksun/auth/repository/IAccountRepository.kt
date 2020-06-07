package com.blacksun.auth.repository

import com.blacksun.auth.entity.Account
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

/**
 * JPA repository implementing Micranaut crud repository
 * Handles all the data layer for the Account entity
 *
 * @see CrudRepository
 * @see Account
 *
 * @author vicrdguez
 */
@Repository
interface IAccountRepository : CrudRepository<Account, Long>{
    fun findByUserName(userName : String) : Optional<Account>
    fun existsByEmail(email: String): Boolean
    fun update(@Id id: Long?, password: String, salt: String)
}
