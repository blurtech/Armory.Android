package tech.blur.armory.common

sealed class Result<TValue, TError> {
    val isSuccess: Boolean get() = this !is Failure
    val isFailure: Boolean get() = this is Failure

    inline fun getOrElse(onFailure: (exception: TError) -> TValue): TValue {
        return when (val error = errorOrNull()) {
            null -> (this as Success).value
            else -> onFailure(error)
        }
    }

    fun errorOrNull(): TError? =
        when (this) {
            is Failure -> error
            else -> null
        }

    fun valueOrNull(): TValue? =
        when (this) {
            is Success -> value
            else -> null
        }

    fun requireValue(): TValue =
        when (this) {
            is Success -> value
            is Failure -> error("Result is Failure: $this")
        }

    inline fun <NValue> mapSuccess(block: (value: TValue) -> NValue): Result<NValue, TError> {
        return when (this) {
            is Success -> success(block(value))
            is Failure -> failure(error)
        }
    }

    fun <NError> mapError(block: (error: TError) -> NError): Result<TValue, NError> {
        return when (this) {
            is Success -> success(value)
            is Failure -> failure(block(error))
        }
    }

    fun mapSuccessToUnit(): Result<Unit, TError> {
        return mapSuccess { }
    }

    fun getOrDefault(defaultValue: TValue): TValue {
        return if (isFailure) defaultValue
        else (this as Success).value
    }

    fun onSuccess(onSuccessBlock: (TValue) -> Unit): Result<TValue, TError> {
        if (this is Success) onSuccessBlock(value)
        return this
    }

    suspend fun onSuccessSuspend(onSuccessBlock: suspend (TValue) -> Unit): Result<TValue, TError> {
        if (this is Success) onSuccessBlock(value)
        return this
    }

    fun onFailure(onErrorBlock: (TError) -> Unit): Result<TValue, TError> {
        if (this is Failure) onErrorBlock(error)
        return this
    }

    data class Success<T, R>(var value: T) : Result<T, R>()
    data class Failure<T, R>(val error: R) : Result<T, R>()

    companion object {
        fun <TValue, TError> success(value: TValue): Result<TValue, TError> = Success(value)

        fun <TValue, TError> failure(throwable: TError): Result<TValue, TError> = Failure(throwable)
    }
}