package com.blacksun.auth.service

import com.blacksun.auth.entity.Account
import com.blacksun.auth.repository.IAccountRepository
import com.blacksun.auth.utils.logger
import io.micronaut.security.authentication.*
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.nio.channels.FileLock
import java.util.*
import java.util.function.Predicate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorService : AuthenticationProvider
{
    @Inject
    private lateinit var repository: IAccountRepository

    override fun authenticate(authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse>
    {
        return repository.findByUserName(authenticationRequest?.identity.toString())
            .filter { acc -> acc.password == authenticationRequest?.secret }
            .map { acc -> UserDetails(acc.userName, Collections.emptyList()) as AuthenticationResponse }
            .map { acc -> Flowable.just(acc) }
            .orElse(Flowable.just(AuthenticationFailed()))
    }
}