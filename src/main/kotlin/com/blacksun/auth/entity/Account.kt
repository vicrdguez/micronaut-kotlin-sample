package com.blacksun.auth.entity

import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "account")
data class Account(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        @NotNull
        val userName: String,
        @NotNull
        val email: String,
        @NotNull
        val password: String,
        @NotNull
        val creation: Timestamp,
        val lastLogin: Timestamp)