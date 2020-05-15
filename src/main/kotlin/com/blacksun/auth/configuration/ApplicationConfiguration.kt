package com.blacksun.auth.configuration

import javax.validation.constraints.NotNull

interface ApplicationConfiguration
{
    @NotNull fun getMax() : Int;
}