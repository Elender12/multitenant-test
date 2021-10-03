package com.ecirstea.multitenant.routing

import com.ecirstea.multitenant.interceptors.TenantNameInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
@Configuration
class WebMvcConfig: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(TenantNameInterceptor())
    }
}