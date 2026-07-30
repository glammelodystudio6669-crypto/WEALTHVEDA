package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kyc_records")
data class KycEntity(
    @PrimaryKey val userId: String,
    val panNumber: String = "",
    val panImageUri: String? = null,
    val aadhaarNumber: String = "",
    val aadhaarImageUri: String? = null,
    val bankName: String = "",
    val accountNumber: String = "",
    val ifscCode: String = "",
    val passbookImageUri: String? = null,
    val status: String = "VERIFIED", // "PENDING", "VERIFIED", "REJECTED"
    val rejectionReason: String? = null,
    val lastUpdated: Long = System.currentTimeMillis()
)
