package manage.laundry.service.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


data class ShopRegisterRequest(
    @field:NotBlank(message = "Tên chủ tiệm không được để trống")
    @field:Size(min = 2, max = 50, message = "Tên chủ tiệm phải từ 2 đến 50 ký tự")
    val ownerName: String,

    @field:NotBlank(message = "Email không được để trống")
    @field:Email(message = "Email không hợp lệ")
    val email: String,

    @field:NotBlank(message = "Mật khẩu không được để trống")
    @field:Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    val password: String,

    @field:NotBlank(message = "Số điện thoại không được để trống")
    @field:Pattern(
        regexp = "^[0-9]{10,11}$",
        message = "Số điện thoại phải có 10 hoặc 11 chữ số"
    )
    val phone: String,

    @field:NotBlank(message = "Tên tiệm không được để trống")
    @field:Size(min = 2, max = 100, message = "Tên tiệm phải từ 2 đến 100 ký tự")
    val shopName: String,

    @field:NotBlank(message = "Địa chỉ tiệm không được để trống")
    @field:Size(min = 5, max = 200, message = "Địa chỉ phải từ 5 đến 200 ký tự")
    val address: String,

    @field:Pattern(
        regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
        message = "Giờ mở cửa phải đúng định dạng HH:mm (24h)"
    )
    val openTime: String,

    @field:Pattern(
        regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
        message = "Giờ đóng cửa phải đúng định dạng HH:mm (24h)"
    )
    val closeTime: String
)
/*
{
  "ownerName": "Nguyễn Văn A",
  "email": "nguyenvana@example.com",
  "password": "Matkhau123",
  "phone": "0987654321",
  "shopName": "Tiệm Giặt Sấy ABC",
  "address": "123 Đường Lý Thường Kiệt, Quận 10, TP.HCM",
  "openTime": "08:00",
  "closeTime": "21:00"
}

 */