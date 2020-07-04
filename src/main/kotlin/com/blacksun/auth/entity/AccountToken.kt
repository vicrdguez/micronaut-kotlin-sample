package com.blacksun.auth.entity

import io.micronaut.context.annotation.Primary
import javax.persistence.*

@Entity
@Table(name = "account_token")
data class AccountToken(
        @Id
        @Primary
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        val accountId: Long,
        val token: String,
        val expiresIn: Long
)