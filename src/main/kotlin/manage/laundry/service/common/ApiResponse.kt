package manage.laundry.service.common


data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
) {
    companion object {
        fun <T> success(data: T? = null, message: String = "Success"): ApiResponse<T> =
            ApiResponse(true, message, data)

        fun <T> error(message: String): ApiResponse<T> =
            ApiResponse(false, message, null)
    }
}
