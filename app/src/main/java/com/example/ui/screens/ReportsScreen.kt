package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TransactionEntity
import com.example.data.UserEntity
import com.example.ui.theme.*

@Composable
fun ReportsScreen(
    user: UserEntity?,
    transactions: List<TransactionEntity>
) {
    val scrollState = rememberScrollState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: All, 1: Pair Match, 2: Sponsor, 3: ROI

    val filteredList = when (selectedTab) {
        1 -> transactions.filter { it.type == "PAIR_MATCH" }
        2 -> transactions.filter { it.type == "REFERRAL" }
        3 -> transactions.filter { it.type == "ROI" }
        else -> transactions
    }

    val totalIncome = transactions.filter { it.type != "WITHDRAWAL" }.sumOf { it.netAmount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("INCOME REPORTS & LEDGER", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
        Text("Detailed breakdown of Sponsor, Binary Pair, and ROI Earnings", fontSize = 12.sp, color = TextMuted)

        Spacer(modifier = Modifier.height(16.dp))

        // Total Net Income Summary Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = RoyalBlueDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("TOTAL NET EARNINGS", color = AquaBright, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("₹${totalIncome.toInt()}", color = GoldBright, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("All Transactions", "Pair Income", "Referrals", "Daily ROI").forEachIndexed { index, label ->
                FilterChip(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredList.isEmpty()) {
            Text("No transactions found in this category.", fontSize = 12.sp, color = TextMuted)
        } else {
            filteredList.forEach { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when (tx.type) {
                                    "REFERRAL" -> AquaLight
                                    "PAIR_MATCH" -> RoyalBluePrimary
                                    "ROI" -> GoldDark
                                    else -> WarningOrange
                                }
                            ) {
                                Text(tx.type, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }

                            Text("₹${tx.netAmount.toInt()}", fontWeight = FontWeight.ExtraBold, color = SuccessGreen, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(tx.description, fontSize = 12.sp, color = TextDark)
                        if (tx.adminDeduction > 0) {
                            Text("Deductions: Admin ₹${tx.adminDeduction.toInt()} + TDS ₹${tx.tdsDeduction.toInt()}", fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}
