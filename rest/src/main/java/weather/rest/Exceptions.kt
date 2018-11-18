package weather.rest

import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object Exceptions {
    const val UNKNOWN = 1
    const val TIMEOUT = 2
    const val UNKNOWN_HOST = 3
    const val DATA = 4
}

fun <T> Throwable.map(block: (code: Int, message: String?) -> T): T = when (this) {
    is SocketTimeoutException -> block(Exceptions.TIMEOUT, null)
    is UnknownHostException -> block(Exceptions.UNKNOWN_HOST, null)
    is HttpException -> block(code(), null)
    is JsonDataException -> block(Exceptions.DATA, null)
    else -> block(Exceptions.UNKNOWN, null)
}
