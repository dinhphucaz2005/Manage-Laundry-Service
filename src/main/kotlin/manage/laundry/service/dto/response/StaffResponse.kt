package manage.laundry.service.dto.response

import manage.laundry.service.entity.Staff

data class StaffResponse(
    val staffId: Int,
    val userId: Int,
    val name: String,
    val email: String,
    val phone: String,
    val position: String,
    val shopId: Int
) {
    companion object {
        fun from(it: Staff): StaffResponse {
            return StaffResponse(
                staffId = it.id,
                userId = it.user.id,
                name = it.user.name,
                email = it.user.email,
                phone = it.user.phone,
                position = "",
                shopId = it.shop.id
            )
        }
    }
}
