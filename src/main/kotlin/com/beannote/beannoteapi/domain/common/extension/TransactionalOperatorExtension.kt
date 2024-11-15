//package com.beannote.beannoteapi.domain.common.extension
//
//import io.github.oshai.kotlinlogging.KotlinLogging
//import kotlin.reflect.KClass
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.currentCoroutineContext
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.reactive.awaitFirstOrNull
//import kotlinx.coroutines.reactive.awaitSingle
//import kotlinx.coroutines.reactor.mono
//import kotlinx.coroutines.withContext
//import org.springframework.dao.NonTransientDataAccessException
//import org.springframework.transaction.ReactiveTransaction
//import org.springframework.transaction.reactive.TransactionalOperator
//
//val logger = KotlinLogging.logger {}
//
//suspend fun <T> TransactionalOperator.executeInTx(
//    block: suspend (ReactiveTransaction) -> T,
//    allowNull: Boolean = false
//): T? {
//    return withContext(Dispatchers.IO) {
//        val context = currentCoroutineContext().minusKey(Job.Key)
//        execute { status ->
//            mono(context) { block(status) }
//        }.let {
//            if (allowNull) it.awaitFirstOrNull() else it.awaitSingle()
//        }
//    }
//}
//
//suspend fun <T> TransactionalOperator.executeInTx(block: suspend (ReactiveTransaction) -> T): T =
//    executeInTx(block, allowNull = false) ?: throw IllegalStateException("Transactional block returned null unexpectedly")  // TODO("예외처리")
//
//suspend fun <T> TransactionalOperator.executeInTxOrNull(block: suspend (ReactiveTransaction) -> T): T? =
//    executeInTx(block, allowNull = true)
//
//
////suspend fun <T : Any> TransactionalOperator.retryWhenAndAwait(
////    cause: KClass<out Throwable>? = NonTransientDataAccessException::class,
////    maxAttempts: Int = 5,
////    delay: Long = 500,
////    f: suspend (ReactiveTransaction) -> T?,
////): T? {
////    repeat(maxAttempts - 1) { repeatCount ->
////        try {
////            return executeInTx(f)
////        } catch (ex: Throwable) {
////            if (cause!!.java.isAssignableFrom(ex.javaClass)) {
////                logger.info { "try to recover (maxAttempts : $maxAttempts, repeatCount: $repeatCount, delay: $delay))" }
////            } else {
////                throw ex
////            }
////        }
////        delay(delay)
////    }
////    return executeInTx(f)
////}
