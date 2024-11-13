package com.beannote.beannoteapi.config.database

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.ReactiveMongoTransactionManager
import org.springframework.transaction.ReactiveTransactionManager

@Configuration
class ReactiveTransactionConfig(
    private val databaseFactory: ReactiveMongoDatabaseFactory,
) {
    @Bean
    fun reactiveTransactionManager(): ReactiveTransactionManager = ReactiveMongoTransactionManager(databaseFactory)
}
