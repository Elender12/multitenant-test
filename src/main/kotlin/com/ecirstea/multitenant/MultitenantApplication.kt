package com.ecirstea.multitenant

import com.ecirstea.multitenant.routing.TenantAwareRoutingSource
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

//@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, XADataSourceAutoConfiguration::class])
class MultitenantApplication

fun main(args: Array<String>) {
	dataSource()
	runApplication<MultitenantApplication>(*args)

}

@Bean
fun dataSource(): DataSource {
	val dataSource: AbstractRoutingDataSource = TenantAwareRoutingSource()
	val targetDataSources: MutableMap<Any, Any> = HashMap()
	targetDataSources["TenantOne"] = tenantOne()
	targetDataSources["TenantTwo"] = tenantTwo()
	dataSource.setTargetDataSources(targetDataSources)
	dataSource.afterPropertiesSet()
	return dataSource
}

fun tenantOne(): DataSource {
	val dataSource = HikariDataSource()
	dataSource.initializationFailTimeout = 0
	dataSource.maximumPoolSize = 5
	dataSource.dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
	dataSource.addDataSourceProperty("url", "jdbc:postgresql://localhost:5432/testdb")
	dataSource.addDataSourceProperty("user", "postgres")
	dataSource.addDataSourceProperty("password", "elena")
	return dataSource
}

fun tenantTwo(): DataSource {
	val dataSource = HikariDataSource()
	dataSource.initializationFailTimeout = 0
	dataSource.maximumPoolSize = 5
	dataSource.dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
	dataSource.addDataSourceProperty("url", "jdbc:postgresql://127.0.0.1:5432/testdb2")
	dataSource.addDataSourceProperty("user", "postgres")
	dataSource.addDataSourceProperty("password", "elena")
	return dataSource
}
