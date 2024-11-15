package com.beannote.beannoteapi.config.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.reactive.TransactionalOperator

@Component
class TransactionManager(
    private val transactionalOperators: TransactionalOperators
) {
    suspend fun <T> executeWrite(block: suspend (ReactiveTransaction) -> T): T {
        return execute(transactionalOperators.txWriter, allowNull = false, block) ?: throw IllegalArgumentException("")
    }

    suspend fun <T> executeRead(block: suspend (ReactiveTransaction) -> T): T {
        return execute(transactionalOperators.txReader, allowNull = false, block) ?: throw IllegalArgumentException("")
    }

    suspend fun <T> executeReadOrNull(block: suspend (ReactiveTransaction) -> T): T? {
        return execute(transactionalOperators.txReader, allowNull = true, block)
    }

    private suspend fun <T> execute(
        operator: TransactionalOperator,
        allowNull: Boolean = false,
        block: suspend (ReactiveTransaction) -> T
    ): T? {
        return withContext(Dispatchers.IO) {
            val context = currentCoroutineContext().minusKey(Job.Key)
            operator.execute { status ->
                mono(context) { block(status) }
            }.let {
                if (allowNull) it.awaitFirstOrNull() else it.awaitSingle()
            }
        }
    }
}

//suspend fun <T : Any> TransactionalOperator.retryWhenAndAwait(
//    cause: KClass<out Throwable>? = NonTransientDataAccessException::class,
//    maxAttempts: Int = 5,
//    delay: Long = 500,
//    f: suspend (ReactiveTransaction) -> T?,
//): T? {
//    repeat(maxAttempts - 1) { repeatCount ->
//        try {
//            return executeInTx(f)
//        } catch (ex: Throwable) {
//            if (cause!!.java.isAssignableFrom(ex.javaClass)) {
//                logger.info { "try to recover (maxAttempts : $maxAttempts, repeatCount: $repeatCount, delay: $delay))" }
//            } else {
//                throw ex
//            }
//        }
//        delay(delay)
//    }
//    return executeInTx(f)
//}
