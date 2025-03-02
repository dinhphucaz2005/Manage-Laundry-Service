package manage.laundry.service.common


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity(
            ApiResponse.success(
                message = ex.message ?: "Invalid input",
                data = null
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity(
            ApiResponse.success(
                message = ex.message ?: "Internal server error",
                data = null
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}
