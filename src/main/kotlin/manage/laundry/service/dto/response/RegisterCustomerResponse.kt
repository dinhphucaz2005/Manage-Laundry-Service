package manage.laundry.service.dto.response

data class RegisterCustomerResponse(
    val customerId: Int,
    val userId: Int,
    val name: String,
    val email: String,
    val phone: String
)
