package alessandro.vendramini.hackernews.data.api

sealed class ApiResource<T>(
    val data: T? = null,
    val exception: Exception? = null,
    val throwable: Throwable? = null,
) {
    class Success<T>(data: T?) : ApiResource<T>(data = data)
    class Unauthorized<T>(throwable: Throwable) : ApiResource<T>(throwable = throwable)
    class GeneralError<T>(exception: Exception, data: T? = null) : ApiResource<T>(
        data = data,
        exception = exception,
    )
    class ConnectionError<T> : ApiResource<T>()
    class ConnectionTimeOut<T> : ApiResource<T>()
}