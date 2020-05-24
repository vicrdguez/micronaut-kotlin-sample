package com.blacksun.auth.utils

import com.blacksun.auth.enum.HashAlgorithm
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

const val ITERATIONS: Int = 10000
const val KEY_LENGTH: Int = 512

/**
 * Handles password hashing
 *
 * @see SecretKeyFactory
 * @see KeySpec
 *
 * @constructor primary constructor
 *  @param algorithm the algorithm used for password hashing
 *
 * @author vicrdguez
 */
class PasswordEncoder(private val algorithm: HashAlgorithm)
{
    private val salt: ByteArray = ByteArray(16)
    private val base64Encoder = Base64.getEncoder()

    /**
     * Generates a salt for hashing
     *
     * @return base64 formatted string salt
     */
    fun generateSalt(): String
    {
        SecureRandom().nextBytes(salt)
        return base64Encoder.encodeToString(salt)
    }

    /**
     * Hashs a password with the desired algorithm
     *
     *
     * @param password the password to be hashed
     * @return base64 string formatted password hash
     */
    fun hash(password: String): String
    {
        return when (algorithm)
        {
            HashAlgorithm.PBKDF2 -> encodeWithPBKDF2(password)
        }
    }

    private fun encodeWithPBKDF2(password: String): String
    {
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(algorithm.value)

        return base64Encoder.encodeToString(factory.generateSecret(spec).encoded)
    }
}