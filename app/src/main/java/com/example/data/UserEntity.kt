package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String, // e.g. "WV10001"
    val name: String,
    val email: String = "user@wealthveda.com",
    val mobile: String = "+91 9988776655",
    val passwordHash: String = "pass123",
    val sponsorId: String = "WV10000", // Referrer ID
    val placement: String = "LEFT", // "LEFT" or "RIGHT" or "AUTO"
    val status: String = "ACTIVE", // "ACTIVE", "INACTIVE", "BLOCKED", "PENDING_TOPUP"
    val role: String = "USER", // "USER" or "ADMIN"
    val joinDate: Long = System.currentTimeMillis(),
    val packageAmount: Double = 6300.0,
    val packageBv: Int = 100,
    val packagePv: Int = 100,
    val totalIncome: Double = 0.0,
    val totalPairsMatched: Int = 0,
    val reTopupCount: Int = 1,
    val isTestUser: Boolean = false,
    val avatarUri: String? = null
)
