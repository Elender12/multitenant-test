package com.ecirstea.multitenant.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class DistrictDto(
    @param:JsonProperty("id")
    @get:JsonProperty("id")
    val id: UUID?,
    @param:JsonProperty("name")
    @get:JsonProperty("name")
    val name: String,
    @param:JsonProperty("created")
    @get:JsonProperty("created")
    val created: Date?,
    @get:JsonProperty("modified")
    @param:JsonProperty("modified")
    val modified: Date?
) {

}