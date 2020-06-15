package com.blacksun.auth.service.Implementation

import com.blacksun.auth.entity.Account
import com.blacksun.auth.enum.HashAlgorithm
import com.blacksun.auth.repository.IAccountRepository
import com.blacksun.auth.service.IAccountService
import com.blacksun.auth.utils.PasswordEncoder
import com.blacksun.auth.web.dto.AccountRequest
import io.micronaut.security.authentication.*
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This service will contain all the necessary domain logic related to Accounts
 *
 * @author vicrdguez
 */
@Singleton
class AccountService(
        @Inject private val repository: IAccountRepository
) : AuthenticationProvider, IAccountService
{
    override fun authenticate(authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse>
    {
        return repository.findByUserName(authenticationRequest?.identity.toString())
                .filter()
                {
                    account ->
                        val encoder = PasswordEncoder(HashAlgorithm.PBKDF2)
                        account.password == encoder.hash(authenticationRequest?.secret.toString(), account.salt)
                }
                .map()
                {
                    account ->
                        val authenticationResponse = UserDetails(account.userName, Collections.emptyList()) as AuthenticationResponse
                        authenticationResponse
                }
                .map { account -> Flowable.just(account) }
                .orElse(Flowable.just(AuthenticationFailed()))
    }

    override fun register(account: AccountRequest): Account
    {
        val encoder = PasswordEncoder(HashAlgorithm.PBKDF2)
        val salt: String = encoder.generateSalt()
        val encodedPassword: String = encoder.hash(account.password)
        return repository.save(
                Account(null, account.userName, account.email, encodedPassword, null, null, salt)
        )
    }

    override fun read(id: Long): Optional<Account>
    {
        return repository.findById(id)
    }

    override fun update(account: Account): Account
    {
        return repository.update(account)
    }

    override fun delete(id: Long)
    {
        return repository.deleteById(id)
    }

    override fun sendPasswordResetEmail(email: String): Boolean
    {
        //TODO(add condition fot exist function and call mail client when true)
        return repository.existsByEmail(email)
    }

    override fun updatePassword(id: Long, password: String)
    {
        val encoder = PasswordEncoder(HashAlgorithm.PBKDF2)
        val salt: String = encoder.generateSalt()
        val encodedPassword: String = encoder.hash(password)

        return repository.update(id, encodedPassword, salt)
    }

    override fun updatePassword(id: Long, oldPassword: String, newPassword: String): Boolean
    {
        val account: Account? = repository.findById(id).orElse(null)
        if(account != null)
        {
            val encoder = PasswordEncoder(HashAlgorithm.PBKDF2)
            if(account.password == encoder.hash(oldPassword, account.salt))
            {
                updatePassword(id, newPassword)
                return true
            }
            return false
        }
        return false
    }
}