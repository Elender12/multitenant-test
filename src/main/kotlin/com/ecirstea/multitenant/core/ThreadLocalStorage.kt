package com.ecirstea.multitenant.core

class ThreadLocalStorage {

    companion object {
        private val tenant = ThreadLocal<String>()
        fun setTenantName(tenantName: String?) {
            tenant.set(tenantName)
        }

        fun getTenantName(): String {
            return tenant.get()
        }
    }
}