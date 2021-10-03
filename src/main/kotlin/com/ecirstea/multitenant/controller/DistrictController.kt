package com.ecirstea.multitenant.controller

import com.ecirstea.multitenant.converter.Converters
import com.ecirstea.multitenant.model.dto.DistrictDto
import com.ecirstea.multitenant.repository.IDistrictRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DistrictController @Autowired constructor(repository: IDistrictRepository) {
    final var repo : IDistrictRepository = repository

    @PostMapping("/districts")
    fun post(@RequestBody entity: DistrictDto): DistrictDto {
        val source = Converters.convertFromDTO(entity)
        val result = repo.save(source)
        println("Result is: $result")
        return Converters.convertToDTO(result)
    }



}