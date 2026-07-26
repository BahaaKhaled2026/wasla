package com.wasla.worker.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties(prefix = "wasla.datasource")
data class WaslaDataSourceProperties(
    @NestedConfigurationProperty
    val control: PoolProperties,
    @NestedConfigurationProperty
    val tenant: PoolProperties,
) {
    data class PoolProperties(
        val jdbcUrl: String,
        val username: String,
        val password: String,
        val poolName: String,
        val maximumPoolSize: Int,
    )
}
