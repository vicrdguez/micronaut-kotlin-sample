package com.blacksun.auth.entity


import io.micronaut.context.annotation.Primary
import io.micronaut.data.annotation.DateCreated
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "account")
data class Account(
        @Id
        @Primary
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @NotNull
        val userName: String,
        @NotNull
        val email: String,
        @NotNull
        val password: String,
        @NotNull
        @DateCreated
        var creationTs: Timestamp?,
        var lastLogin: Timestamp?,
        var salt: String?)