package manage.laundry.service.common


data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null, message: String = "Success"): ApiResponse<T> =
            ApiResponse(true, message, data)

        fun <T> error(message: String, data: T? = null): ApiResponse<T> =
            ApiResponse(false, message, data)
    }
}
