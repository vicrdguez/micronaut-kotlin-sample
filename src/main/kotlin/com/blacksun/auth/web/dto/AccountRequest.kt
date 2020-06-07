package com.blacksun.auth.web.dto

/**
 * Account request DTO
 *
 * @author vicrdguez
 */
data class AccountRequest(
    val userName: String,
    val email: String,
    val password: String
)

