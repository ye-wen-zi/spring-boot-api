package com.example.storefront.constants;

public enum OrderStatus {
    PENDING,      // Chờ thanh toán / Chờ xác nhận
    PROCESSING,   // Đang xử lý đóng gói
    SHIPPING,     // Đang giao hàng
    DELIVERED,    // Đã giao hàng thành công
    CANCELLED     // Đã hủy
}
