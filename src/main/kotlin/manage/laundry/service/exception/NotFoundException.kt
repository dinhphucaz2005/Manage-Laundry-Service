package manage.laundry.service.exception

data class CustomException(
    override val message: String,
) : RuntimeException()
