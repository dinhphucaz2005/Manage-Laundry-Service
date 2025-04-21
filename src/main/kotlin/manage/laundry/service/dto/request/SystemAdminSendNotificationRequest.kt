package manage.laundry.service.dto.request

data class SystemAdminSendNotificationRequest(
    val adminKey: String,
    val message: String,
) {
    fun validate() {
        if (adminKey != "adminKey") {
            throw IllegalArgumentException("Invalid admin key")
        }
    }
}