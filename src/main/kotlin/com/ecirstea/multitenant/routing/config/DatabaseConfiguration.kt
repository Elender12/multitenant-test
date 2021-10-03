package com.ecirstea.multitenant.routing.config

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DatabaseConfiguration @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(

    @param:JsonProperty("url") val url: String,
    @param:JsonProperty("user") val user: String ="",
    @param:JsonProperty("dataSourceClassName") val dataSourceClassName: String ="",
    @param:JsonProperty("password") val password: String ="",
    val tenant: String = ""
) {
    override fun toString(): String {
        return "DatabaseConfiguration(url='$url', user='$user', dataSourceClassName='$dataSourceClassName', password='$password', tenant='$tenant')"
    }
}