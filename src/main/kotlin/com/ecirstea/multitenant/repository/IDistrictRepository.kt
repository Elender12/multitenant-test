package com.ecirstea.multitenant.repository

import com.ecirstea.multitenant.model.District
//import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.CompletableFuture

interface IDistrictRepository: CrudRepository<District, UUID> {
 /*   @Async
    @Query("Select d from District d")
    fun findAllAsync(): CompletableFuture<MutableList<District>>*/


}