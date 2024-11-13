package com.beannote.beannoteapi.domain.common.extension

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.springframework.dao.NonTransientDataAccessException
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import kotlin.reflect.KClass

val logger = KotlinLogging.logger {}

suspend fun <T> TransactionalOperator.executeWithAwait(block: suspend (ReactiveTransaction) -> T): T =
    withContext(Dispatchers.IO) {
        this@executeWithAwait.executeAndAwait { transaction ->
            block(transaction)
        }
    }

suspend fun <T : Any> TransactionalOperator.retryWhenAndAwait(
    cause: KClass<out Throwable>? = NonTransientDataAccessException::class,
    maxAttempts: Int = 5,
    delay: Long = 500,
    f: suspend (ReactiveTransaction) -> T?,
): T? {
    repeat(maxAttempts - 1) { repeatCount ->
        try {
            return executeWithAwait(f)
        } catch (ex: Throwable) {
            if (cause!!.java.isAssignableFrom(ex.javaClass)) {
                logger.info { "try to recover (maxAttempts : $maxAttempts, repeatCount: $repeatCount, delay: $delay))" }
            } else {
                throw ex
            }
        }
        delay(delay)
    }
    return executeWithAwait(f)
}
