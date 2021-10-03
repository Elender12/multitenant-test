package com.ecirstea.multitenant.routing

import com.ecirstea.multitenant.core.ThreadLocalStorage
import com.ecirstea.multitenant.routing.config.DatabaseConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import com.zaxxer.hikari.HikariDataSource
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.scheduling.annotation.Scheduled
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentMap
import java.util.stream.Collectors
import javax.sql.DataSource

class DynamicTenantAwareRoutingSource(
    private val filename: String,
    private val objectMapper: ObjectMapper,
    private val tenants: ConcurrentMap<String, HikariDataSource>
): AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        return ThreadLocalStorage.getTenantName()
    }

    override fun determineTargetDataSource(): DataSource {
        val lookupKey = determineCurrentLookupKey() as String
        // And finally return it:
        return tenants[lookupKey]!!

    }
    private fun getDataSources(): ConcurrentMap<String?, HikariDataSource?>? {

        // Deserialize the JSON:
        val configurations: Array<DatabaseConfiguration> = getDatabaseConfigurations()

        // Now create a Lookup Table:
        return Arrays
            .stream(configurations)
            .collect(
                Collectors.toConcurrentMap(DatabaseConfiguration::tenant
                ) { configuration: DatabaseConfiguration -> this.buildDataSource(configuration) }
            )
    }

    private fun getDatabaseConfigurations(): Array<DatabaseConfiguration> {
        return try {
            objectMapper.readValue(
                File(filename),
                Array<DatabaseConfiguration>::class.java
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
    private fun buildDataSource(configuration: DatabaseConfiguration): HikariDataSource? {
        val dataSource = HikariDataSource()
        dataSource.initializationFailTimeout = 0
        dataSource.maximumPoolSize = 5
        dataSource.dataSourceClassName = configuration.dataSourceClassName
        dataSource.addDataSourceProperty("url", configuration.url)
        dataSource.addDataSourceProperty("user", configuration.user)
        dataSource.addDataSourceProperty("password", configuration.password)
        return dataSource
    }

    @Scheduled(fixedDelay = 5000L)
    fun refreshDataSources() {
        val configurations = getDatabaseConfigurations()
        removeObsoleteTenants(configurations)
        insertOrUpdateTenants(configurations)
    }

    private fun insertOrUpdateTenants(configurations: Array<DatabaseConfiguration>) {
        for (configuration in configurations) {
            if (tenants.containsKey(configuration.tenant)) {
                val dataSource = tenants[configuration.tenant]
                // We only shutdown and reload, if the configuration has actually changed...
                if (!isCurrentConfiguration(dataSource, configuration)) {
                    // Make sure we close this DataSource first...
                    dataSource!!.close()
                    // ... and then insert a new DataSource:
                    tenants[configuration.tenant] = buildDataSource(configuration)
                }
            } else {
                tenants[configuration.tenant] = buildDataSource(configuration)
            }
        }
    }

    private fun removeObsoleteTenants(configurations: Array<DatabaseConfiguration>) {

        // Are there Tenants, that have been removed:
        val tenantNamesFromConfiguration: MutableSet<String?>? = Arrays.stream(configurations)
            .map { x -> x?.tenant }
            .collect(Collectors.toSet())
        for (tenant in tenants.keys) {

            // There is currently a Tenant, which is not listed anymore:
            if (!tenantNamesFromConfiguration?.contains(tenant)!!) {

                // So get the DataSource first ...
                val dataSource = tenants[tenant]

                // ... close all existing connections:
                dataSource!!.close()

                // ... and remove it:
                tenants.remove(tenant)
            }
        }
    }

    private fun isCurrentConfiguration(dataSource: HikariDataSource?, configuration: DatabaseConfiguration?): Boolean {
        return (dataSource!!.dataSourceProperties.getProperty("user") == configuration?.user
                && dataSource.dataSourceProperties.getProperty("url") == configuration?.url
                && dataSource.dataSourceProperties.getProperty("password") == configuration?.password
                && dataSource.dataSourceClassName == configuration?.dataSourceClassName)
    }
}