package com.ecirstea.multitenant.routing

import com.ecirstea.multitenant.core.ThreadLocalStorage
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class TenantAwareRoutingSource: AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        return ThreadLocalStorage.getTenantName()
    }
}