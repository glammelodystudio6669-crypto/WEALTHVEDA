package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    users: List<UserEntity>,
    transactions: List<TransactionEntity>,
    kycRecords: List<KycEntity>,
    settings: CompanySettingsEntity?,
    onGenerateBulkSimulator: (Int, String) -> Unit,
    onTriggerDailyRoi: () -> Unit,
    onSaveSettings: (CompanySettingsEntity) -> Unit
) {
    val scrollState = rememberScrollState()

    val currentSettings = settings ?: CompanySettingsEntity()

    var pkgPrice by remember { mutableStateOf(currentSettings.packagePrice.toString()) }
    var refBonus by remember { mutableStateOf(currentSettings.referralIncome.toString()) }
    var pairIncome by remember { mutableStateOf(currentSettings.pairIncome.toString()) }
    var dailyCap by remember { mutableStateOf(currentSettings.dailyCeilingAmount.toString()) }
    var dailyRoiPct by remember { mutableStateOf(currentSettings.dailyRoiPercent.toString()) }
    var adminDedPct by remember { mutableStateOf(currentSettings.adminDeductionPercent.toString()) }
    var tdsDedPct by remember { mutableStateOf(currentSettings.tdsDeductionPercent.toString()) }

    var selectedLeg by remember { mutableStateOf("LEFT") } // "LEFT" or "RIGHT"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("ADMIN CONTROL CENTER", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
                Text("System Operations, Settings & Genealogy Simulator", fontSize = 12.sp, color = TextMuted)
            }

            Surface(shape = RoundedCornerShape(20.dp), color = GoldAccent) {
                Text("ADMIN MODE", color = RoyalBlueDark, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // System High-level Stats
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AdminStatCard("Total Users", "${users.size}", Icons.Default.Groups, Modifier.weight(1f))
            AdminStatCard("Payout Log", "₹${transactions.sumOf { it.netAmount }.toInt()}", Icons.Default.MonetizationOn, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= GENEALOGY SIMULATOR (TEST MODE) =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = RoyalBlueDark)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountTree, contentDescription = null, tint = GoldBright)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GENEALOGY SIMULATOR (TEST MODE)", color = GoldBright, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Text("Automatically create bulk test IDs and update binary tree matching in real-time.", color = AquaSoft, fontSize = 11.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Target Leg:", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("LEFT", "RIGHT").forEach { side ->
                        FilterChip(
                            selected = selectedLeg == side,
                            onClick = { selectedLeg = side },
                            label = { Text("$side LEG", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Generate Test IDs:", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(100, 500, 1000, 5000).forEach { count ->
                        Button(
                            onClick = { onGenerateBulkSimulator(count, selectedLeg) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Text("+$count", color = RoyalBlueDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= DAILY ROI ENGINE TRIGGER =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("RUN DAILY 0.5% ROI CREDIT ENGINE", fontWeight = FontWeight.Bold, color = RoyalBluePrimary, fontSize = 14.sp)
                Text("Distributes daily ROI to active ₹6,300 package holders (Mon-Fri check)", fontSize = 11.sp, color = TextMuted)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onTriggerDailyRoi() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AquaLight)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("TRIGGER ROI DISTRIBUTION", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= BUSINESS PLAN & SYSTEM SETTINGS =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("COMPANY BUSINESS PLAN SETTINGS", fontWeight = FontWeight.Bold, color = RoyalBluePrimary, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = pkgPrice, onValueChange = { pkgPrice = it }, label = { Text("Package Price (₹)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = refBonus, onValueChange = { refBonus = it }, label = { Text("Referral Bonus (₹)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = pairIncome, onValueChange = { pairIncome = it }, label = { Text("Pair Income (₹)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = dailyCap, onValueChange = { dailyCap = it }, label = { Text("Daily Ceiling Cap (₹)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = dailyRoiPct, onValueChange = { dailyRoiPct = it }, label = { Text("Daily ROI %") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = adminDedPct, onValueChange = { adminDedPct = it }, label = { Text("Admin %") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = tdsDedPct, onValueChange = { tdsDedPct = it }, label = { Text("TDS %") }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val updated = currentSettings.copy(
                            packagePrice = pkgPrice.toDoubleOrNull() ?: 6300.0,
                            referralIncome = refBonus.toDoubleOrNull() ?: 300.0,
                            pairIncome = pairIncome.toDoubleOrNull() ?: 600.0,
                            dailyCeilingAmount = dailyCap.toDoubleOrNull() ?: 6000.0,
                            dailyRoiPercent = dailyRoiPct.toDoubleOrNull() ?: 0.5,
                            adminDeductionPercent = adminDedPct.toDoubleOrNull() ?: 5.0,
                            tdsDeductionPercent = tdsDedPct.toDoubleOrNull() ?: 5.0
                        )
                        onSaveSettings(updated)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)
                ) {
                    Text("SAVE COMPANY SETTINGS", color = GoldBright, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = RoyalBluePrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, fontSize = 11.sp, color = TextMuted)
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = RoyalBlueDark)
        }
    }
}
