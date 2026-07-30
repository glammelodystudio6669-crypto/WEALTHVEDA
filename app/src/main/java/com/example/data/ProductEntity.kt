package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "Premium Alkaline Water Jar",
    val tagline: String = "Healthy Water... Healthy Wealth...",
    val price: Double = 6300.0,
    val bv: Int = 100,
    val pv: Int = 100,
    val description: String = "Multi-stage mineralized ionization technology delivering pH 8.5-9.5 alkaline water with negative ORP antioxidant properties. Includes 20L BPA-Free Food Grade Casing and Smart Dispenser Spout.",
    val phLevel: String = "8.5 - 9.5 pH",
    val imageResId: Int = 0,
    val customImageUri: String? = null,
    val isAvailable: Boolean = true
)
