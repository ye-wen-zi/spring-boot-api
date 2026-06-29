package com.example.storefront.constants;

public enum TransactionStatus {
    PENDING, // Đang chờ người dùng quét mã / nhập OTP
    SUCCESS, // Thanh toán thành công
    FAILED, // Thất bại (sai mật khẩu, hết hạn, lỗi hệ thống)
    REFUNDED // Đã hoàn tiền thành công cho khách
}