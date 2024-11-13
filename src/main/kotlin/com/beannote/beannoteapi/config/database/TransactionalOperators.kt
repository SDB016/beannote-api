package com.beannote.beannoteapi.config.database

import org.springframework.stereotype.Component
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.reactive.TransactionalOperator

@Component
class TransactionalOperators(
    private val transactionalOperator: TransactionalOperator,
    private val reactiveTransactionManager: ReactiveTransactionManager,
) {
    val txWriter: TransactionalOperator =
        TransactionalOperator.create(
            reactiveTransactionManager,
            object : TransactionDefinition {
                override fun getPropagationBehavior(): Int = TransactionDefinition.PROPAGATION_REQUIRED
            },
        )

    val newTxWriter: TransactionalOperator =
        TransactionalOperator.create(
            reactiveTransactionManager,
            object : TransactionDefinition {
                override fun getPropagationBehavior(): Int = TransactionDefinition.PROPAGATION_REQUIRES_NEW
            },
        )

    val txReader: TransactionalOperator =
        TransactionalOperator.create(
            reactiveTransactionManager,
            object : TransactionDefinition {
                override fun getPropagationBehavior(): Int = TransactionDefinition.PROPAGATION_REQUIRED

                override fun isReadOnly(): Boolean = true
            },
        )
}
