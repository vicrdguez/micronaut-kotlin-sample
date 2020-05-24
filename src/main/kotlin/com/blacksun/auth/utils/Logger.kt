package com.blacksun.auth.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Allows slf4j logger to be used in any poart of the project
 *
 * @author vicrdguez
 */
inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)
