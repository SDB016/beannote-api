package com.beannote.beannoteapi.config.database

import org.springframework.stereotype.Component
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.reactive.TransactionalOperator

object TransactionDefinitions {
    val required = object : TransactionDefinition {
        override fun getPropagationBehavior(): Int = TransactionDefinition.PROPAGATION_REQUIRED
    }
    val requiresNew = object : TransactionDefinition {
        override fun getPropagationBehavior(): Int = TransactionDefinition.PROPAGATION_REQUIRES_NEW
    }
    val readOnly = object : TransactionDefinition {
        override fun getPropagationBehavior(): Int = TransactionDefinition.PROPAGATION_REQUIRED
        override fun isReadOnly(): Boolean = true
    }
}

@Component
class TransactionalOperators(
    private val reactiveTransactionManager: ReactiveTransactionManager,
) {
    val txWriter: TransactionalOperator =
        TransactionalOperator.create(reactiveTransactionManager, TransactionDefinitions.required)

    val newTxWriter: TransactionalOperator =
        TransactionalOperator.create(reactiveTransactionManager, TransactionDefinitions.requiresNew)

    val txReader: TransactionalOperator =
        TransactionalOperator.create(reactiveTransactionManager, TransactionDefinitions.readOnly)
}

