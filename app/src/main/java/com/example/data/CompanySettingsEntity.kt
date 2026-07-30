package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_settings")
data class CompanySettingsEntity(
    @PrimaryKey val id: Int = 1,
    val companyName: String = "WEALTH VEDA",
    val tagline: String = "Healthy Water... Healthy Wealth...",
    val packagePrice: Double = 6300.0,
    val referralIncome: Double = 300.0,
    val pairIncome: Double = 600.0,
    val pairRatio: String = "1:1",
    val dailyMaxPairs: Int = 10,
    val dailyCeilingAmount: Double = 6000.0,
    val dailyRoiPercent: Double = 0.5, // 0.5%
    val roiWorkingDaysOnly: Boolean = true, // Monday to Friday only
    val isRoiEnabled: Boolean = true,
    val reTopupMultiplier: Double = 3.0, // 3X total income
    val isReTopupEnabled: Boolean = true,
    val adminDeductionPercent: Double = 5.0,
    val tdsDeductionPercent: Double = 5.0,
    val carryForwardPowerLegOnly: Boolean = true,
    val customJarImageUri: String? = null
)
