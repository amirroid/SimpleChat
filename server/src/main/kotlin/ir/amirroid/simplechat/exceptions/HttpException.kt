package ir.amirroid.simplechat.exceptions

data class HttpException(
    val errorCode: Int,
    override val message: String
) : Exception()


fun notFoundError(message: String = "Not Found"): Nothing =
    throw HttpException(404, message)

fun unauthorizedError(message: String = "Unauthorized"): Nothing =
    throw HttpException(401, message)

fun forbiddenError(message: String = "Forbidden"): Nothing =
    throw HttpException(403, message)

fun badRequestError(message: String = "Bad Request"): Nothing =
    throw HttpException(400, message)

fun conflictError(message: String = "Conflict"): Nothing =
    throw HttpException(409, message)

fun internalServerError(message: String = "Internal Server Error"): Nothing =
    throw HttpException(500, message)