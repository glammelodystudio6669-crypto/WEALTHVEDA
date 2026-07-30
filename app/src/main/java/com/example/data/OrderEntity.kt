package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val userId: String,
    val productName: String,
    val quantity: Int = 1,
    val totalAmount: Double = 6300.0,
    val bvEarned: Int = 100,
    val pvEarned: Int = 100,
    val shippingAddress: String,
    val mobile: String,
    val orderStatus: String = "DELIVERED", // "PENDING", "PROCESSING", "DISPATCHED", "DELIVERED"
    val trackingCode: String = "WV-TRK-98321",
    val timestamp: Long = System.currentTimeMillis()
)
