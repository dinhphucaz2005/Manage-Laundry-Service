package manage.laundry.service.exception

data class NotFoundException(
    override val message: String,
) : Exception()