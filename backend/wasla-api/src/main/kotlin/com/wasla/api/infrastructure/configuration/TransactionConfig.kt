package com.wasla.api.infrastructure.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

@Configuration
class TransactionConfig {

    @Bean(name = ["tenantTransactionManager"])
    fun tenantTransactionManager(
        @Qualifier("tenantDataSource") dataSource: DataSource,
    ): PlatformTransactionManager = DataSourceTransactionManager(dataSource)

    @Bean(name = ["controlTransactionManager"])
    fun controlTransactionManager(
        @Qualifier("controlDataSource") dataSource: DataSource,
    ): PlatformTransactionManager = DataSourceTransactionManager(dataSource)

    @Bean
    fun tenantTransactionTemplate(
        @Qualifier("tenantTransactionManager") transactionManager: PlatformTransactionManager,
    ): TransactionTemplate = TransactionTemplate(transactionManager)

    @Bean
    fun controlTransactionTemplate(
        @Qualifier("controlTransactionManager") transactionManager: PlatformTransactionManager,
    ): TransactionTemplate = TransactionTemplate(transactionManager)
}
