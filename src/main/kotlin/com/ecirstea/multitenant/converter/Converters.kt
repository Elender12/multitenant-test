package com.ecirstea.multitenant.converter

import com.ecirstea.multitenant.model.District
import com.ecirstea.multitenant.model.dto.DistrictDto

class Converters {
    companion object {
        fun convertToDTO( source: District): DistrictDto{
        return DistrictDto(source.id, source.name, source.created, source.modified)
        }
        fun convertFromDTO(source: DistrictDto): District {
            return District(source.name)
        }

    }
}