package com.wasla.worker.infrastructure.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

/**
 * Same dual-pool pattern as wasla-api's DataSourceConfig. wasla-worker does NOT
 * run its own Flyway migrations (see application.yml exclusion) - only wasla-api
 * owns schema migration, to avoid two apps racing to migrate on startup.
 */
@Configuration
@EnableConfigurationProperties(WaslaDataSourceProperties::class)
class DataSourceConfig(
    private val properties: WaslaDataSourceProperties,
) {

    @Bean(name = ["controlDataSource"])
    fun controlDataSource(): DataSource = buildDataSource(properties.control)

    @Bean(name = ["tenantDataSource"])
    fun tenantDataSource(): DataSource = buildDataSource(properties.tenant)

    @Bean(name = ["controlJdbcTemplate"])
    fun controlJdbcTemplate(
        @Qualifier("controlDataSource") dataSource: DataSource,
    ): NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource)

    @Bean(name = ["tenantJdbcTemplate"])
    fun tenantJdbcTemplate(
        @Qualifier("tenantDataSource") dataSource: DataSource,
    ): NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(dataSource)

    private fun buildDataSource(pool: WaslaDataSourceProperties.PoolProperties): DataSource {
        val config = HikariConfig().apply {
            jdbcUrl = pool.jdbcUrl
            username = pool.username
            password = pool.password
            poolName = pool.poolName
            maximumPoolSize = pool.maximumPoolSize
        }
        return HikariDataSource(config)
    }
}
