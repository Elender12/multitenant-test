package com.ecirstea.multitenant.async

import com.ecirstea.multitenant.core.ThreadLocalStorage
import org.springframework.core.task.TaskDecorator

class TenantAwareTaskDecorator: TaskDecorator {
    override fun decorate(runnable: Runnable): Runnable {
        val tenantName: String = ThreadLocalStorage.getTenantName()
        return Runnable {
            try {
                ThreadLocalStorage.setTenantName(tenantName)
                runnable.run()
            } finally {
                ThreadLocalStorage.setTenantName(null)
            }
        }
    }
}