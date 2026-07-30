package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey val userId: String,
    val incomeWallet: Double = 0.0,
    val roiWallet: Double = 0.0,
    val shoppingWallet: Double = 0.0,
    val withdrawWallet: Double = 0.0
)
