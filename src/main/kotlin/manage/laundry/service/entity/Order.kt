package manage.laundry.service.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne @JoinColumn(name = "customer_id")
    val customer: User,
    @ManyToOne @JoinColumn(name = "shop_id")
    val shop: Shop,
    @Column(columnDefinition = "text", name = "staff_response")
    val staffResponse: String? = null,
    @Column(name = "estimate_price")
    val estimatePrice: Int,
    @Column(name = "total_price")
    val totalPrice: Int? = null,
    @Enumerated(EnumType.STRING)
    val status: Status = Status.NEW,
    @Column(name = "special_instructions")
    val specialInstructions: String? = null,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Status {
        NEW, /*Khách vừa gửi đồ, chưa tính toán chi phí*/
        PENDING, /*Đã báo giá cho khách, đang chờ khách đồng ý*/
        CANCELED, /*Khách không đồng ý giá và hủy đơn*/
        PROCESSING, /*Khách đã đồng ý, đơn hàng đang được giặt ủi*/
        COMPLETED, /*Đồ đã giặt xong, sẵn sàng trả khách*/
        DELIVERED, /*Khách đã nhận đồ nhưng chưa thanh toán*/
        PAID, /*Khách đã trả tiền (tiền mặt/chuyển khoản/thẻ)*/
//        PAID_FAILED /*Giao dịch thanh toán thất bại (thẻ/ví điện tử)*/
    }
}
