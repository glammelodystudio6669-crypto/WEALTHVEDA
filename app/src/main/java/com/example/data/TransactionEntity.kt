package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val type: String, // "REFERRAL", "PAIR_MATCH", "ROI", "WITHDRAWAL", "RE_TOPUP", "TRANSFER", "DEDUCTION"
    val amount: Double,
    val netAmount: Double = amount,
    val adminDeduction: Double = 0.0,
    val tdsDeduction: Double = 0.0,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS" // "SUCCESS", "PENDING", "REJECTED"
)
