package com.blacksun.auth.service

import com.blacksun.auth.entity.Account
import com.blacksun.auth.enum.HashAlgorithm
import com.blacksun.auth.repository.IAccountRepository
import com.blacksun.auth.utils.PasswordEncoder
import com.blacksun.auth.web.dto.AccountRequest
import io.micronaut.security.authentication.*
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorService(
    @Inject val repository: IAccountRepository
) : AuthenticationProvider
{
    override fun authenticate(authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse>
    {
        return repository.findByUserName(authenticationRequest?.identity.toString())
            .filter { acc -> acc.password == authenticationRequest?.secret }
            .map { acc -> UserDetails(acc.userName, Collections.emptyList()) as AuthenticationResponse }
            .map { acc -> Flowable.just(acc) }
            .orElse(Flowable.just(AuthenticationFailed()))
    }

    fun create(account: AccountRequest): Account
    {
        val encoder = PasswordEncoder(HashAlgorithm.PBKDF2)
        val salt: String = encoder.generateSalt()
        val encodedPass: String = encoder.hash(account.password)
        return repository.save(
            Account(null, account.userName, account.email, encodedPass, null, null, salt)
        )
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