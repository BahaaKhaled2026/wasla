package com.wasla.api.infrastructure.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import javax.sql.DataSource

/**
 * Two independent connection pools, per engineering rule 8.4:
 *   - controlDataSource -> wasla_control (tenant registry, contracts, global users)
 *   - tenantDataSource  -> wasla_tenant  (per-tenant schemas)
 *
 * The application resolves which tenant schema to use from the control database
 * through a trusted server-side mapping (see the tenant transaction boundary,
 * rule 8.5) — it never switches databases based on client-supplied data.
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
