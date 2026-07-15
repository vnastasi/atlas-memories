package md.vnastasi.atlasmemories.result

import kotlinx.coroutines.CancellationException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class Result<out T : Any> {

    data class Success<out T : Any>(
        val data: T
    ) : Result<T>()

    data class Error(
        val failureReason: FailureReason,
        val exception: Exception?
    ) : Result<Nothing>()

    @OptIn(ExperimentalContracts::class)
    fun isSuccess(): Boolean {
        contract {
            returns(true) implies (this@Result is Success<*>)
        }
        return (this is Success<*>)
    }

    @OptIn(ExperimentalContracts::class)
    fun isError(): Boolean {
        contract {
            returns(true) implies (this@Result is Error)
        }
        return (this is Error)
    }

    companion object {

        fun <T : Any> success(data: T): Result<T> = Success(data)

        fun <T : Any> error(failureReason: FailureReason, exception: Exception? = null): Result<T> = Error(failureReason, exception)
    }
}

suspend fun <T : Any, R : Any> Result<T>.map(transform: suspend (T) -> R): Result<R> =
    when (this) {
        is Result.Success<T> -> try {
            Result.Success(transform(this.data))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error(GenericFailureReason.UNEXPECTED_ERROR, e)
        }

        is Result.Error -> this
    }

suspend fun <T : Any, R : Any> Result<T>.flatMap(transform: suspend (T) -> Result<R>): Result<R> =
    when (this) {
        is Result.Success<T> -> try {
            transform(this.data)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error(GenericFailureReason.UNEXPECTED_ERROR, e)
        }

        is Result.Error -> this
    }

suspend fun <T : Any> Result<T>.fold(
    onSuccess: suspend (T) -> Unit = { },
    onError: suspend (FailureReason, Throwable?) -> Unit = { _, _ -> }
) {
    when (this) {
        is Result.Success<T> -> onSuccess(this.data)
        is Result.Error -> onError(this.failureReason, this.exception)
    }
}

suspend fun <T : Any> Result<T>.peek(
    onSuccess: suspend (T) -> Unit = { },
    onError: suspend (FailureReason, Throwable?) -> Unit = { _, _ -> }
): Result<T> {
    fold(onSuccess, onError)
    return this
}

fun <T : Any> Result<T>.dataOrElse(fallback: T): T =
    when (this) {
        is Result.Success<T> -> this.data
        is Result.Error -> fallback
    }

fun <T : Any> Result<T>.dataOrNull(): T? =
    when (this) {
        is Result.Success<T> -> this.data
        is Result.Error -> null
    }
