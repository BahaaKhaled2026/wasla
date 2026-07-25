package com.wasla.api.infrastructure.configuration

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import javax.sql.DataSource

/**
 * Two independent Flyway migration histories, mirroring the two datasources
 * (rule 8.4/8.9). Control and tenant databases evolve on separate schedules and
 * must never share a migration history table.
 *
 * Runs eagerly at startup via InitializingBean, ahead of any bean that depends on
 * the schema being present (e.g. JdbcTemplate consumers) — Spring Boot's default
 * auto-configured Flyway would normally guarantee this ordering for a single
 * primary datasource; since we opted out of that (see DataSourceConfig), we
 * guarantee ordering explicitly here instead.
 */
@Configuration
class FlywayConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun controlFlywayMigration(
        @Qualifier("controlDataSource") dataSource: DataSource,
    ): InitializingBean = InitializingBean {
        Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/control")
            .baselineOnMigrate(true)
            .load()
            .migrate()
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun tenantFlywayMigration(
        @Qualifier("tenantDataSource") dataSource: DataSource,
    ): InitializingBean = InitializingBean {
        Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/tenant")
            .baselineOnMigrate(true)
            .load()
            .migrate()
    }
}
