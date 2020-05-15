package com.blacksun.auth.configuration

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("application")
class ApplicationConfigurationProperties : ApplicationConfiguration
{
    protected val DEFAULT_MAX: Int = 10;
    private var max: Int = DEFAULT_MAX;

    override fun getMax(): Int
    {
        return max;
    }

    fun setMax(max: Int)
    {
        this.max = max;
    }
}