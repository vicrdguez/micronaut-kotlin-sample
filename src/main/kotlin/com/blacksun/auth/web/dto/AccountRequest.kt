package com.blacksun.auth.web.dto


data class AccountRequest(
    val userName: String,
    val email: String,
    val password: String
)

