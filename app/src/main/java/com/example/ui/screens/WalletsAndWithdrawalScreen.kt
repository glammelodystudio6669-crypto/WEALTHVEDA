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
import com.example.data.CompanySettingsEntity
import com.example.data.TransactionEntity
import com.example.data.WalletEntity
import com.example.ui.theme.*

@Composable
fun WalletsAndWithdrawalScreen(
    wallet: WalletEntity?,
    transactions: List<TransactionEntity>,
    settings: CompanySettingsEntity?,
    onRequestWithdrawal: (Double, String, String) -> Unit
) {
    val scrollState = rememberScrollState()
    val w = wallet ?: WalletEntity(userId = "WV10001", incomeWallet = 8400.0, roiWallet = 1260.0, shoppingWallet = 2000.0, withdrawWallet = 940.0)

    var showWithdrawModal by remember { mutableStateOf(false) }

    var withdrawAmountText by remember { mutableStateOf("1000") }
    var withdrawMethod by remember { mutableStateOf("BANK_TRANSFER") } // "BANK_TRANSFER", "UPI"
    var accountOrUpiDetails by remember { mutableStateOf("HDFC Bank 50100234567890 (IFSC: HDFC0000123)") }

    val adminDedPct = settings?.adminDeductionPercent ?: 5.0
    val tdsDedPct = settings?.tdsDeductionPercent ?: 5.0
    val totalDedPct = adminDedPct + tdsDedPct

    val requestedAmt = withdrawAmountText.toDoubleOrNull() ?: 0.0
    val adminDedAmt = requestedAmt * (adminDedPct / 100.0)
    val tdsDedAmt = requestedAmt * (tdsDedPct / 100.0)
    val netPayable = requestedAmt - adminDedAmt - tdsDedAmt

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("WALLETS & WITHDRAWAL", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
        Text("Manage income balances and request bank/UPI payouts", fontSize = 12.sp, color = TextMuted)

        Spacer(modifier = Modifier.height(16.dp))

        // Main Withdrawal Wallet Highlight Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = RoyalBlueDark)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("WITHDRAWABLE BALANCE", color = AquaBright, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("₹${w.withdrawWallet.toInt()}", color = GoldBright, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { showWithdrawModal = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                    ) {
                        Icon(Icons.Default.Payments, contentDescription = null, tint = RoyalBlueDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("REQUEST WITHDRAWAL", color = RoyalBlueDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4 Wallets Grid Breakdown
        Text("BALANCES SUMMARY", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            WalletSubItem("Income Wallet", "₹${w.incomeWallet.toInt()}", RoyalBluePrimary, Modifier.weight(1f))
            WalletSubItem("ROI Wallet", "₹${w.roiWallet.toInt()}", AquaLight, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            WalletSubItem("Shopping Wallet", "₹${w.shoppingWallet.toInt()}", GoldDark, Modifier.weight(1f))
            WalletSubItem("Withdraw Wallet", "₹${w.withdrawWallet.toInt()}", SuccessGreen, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TRANSACTION & WITHDRAWAL HISTORY
        Text("WITHDRAWAL & PAYOUT HISTORY", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
        Spacer(modifier = Modifier.height(8.dp))

        val withdrawals = transactions.filter { it.type == "WITHDRAWAL" }
        if (withdrawals.isEmpty()) {
            Text("No withdrawal records found.", fontSize = 12.sp, color = TextMuted)
        } else {
            withdrawals.forEach { tx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(tx.description, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = RoyalBlueDark, modifier = Modifier.weight(1f))
                            Text(
                                text = tx.status,
                                color = if (tx.status == "PENDING") WarningOrange else SuccessGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Gross: ₹${tx.amount.toInt()} (Admin: ₹${tx.adminDeduction.toInt()} | TDS: ₹${tx.tdsDeduction.toInt()})", fontSize = 11.sp, color = TextMuted)
                            Text("Net: ₹${tx.netAmount.toInt()}", fontWeight = FontWeight.Bold, color = SuccessGreen, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Withdrawal Modal
    if (showWithdrawModal) {
        AlertDialog(
            onDismissRequest = { showWithdrawModal = false },
            title = { Text("Request Payout Withdrawal", fontWeight = FontWeight.Bold, color = RoyalBluePrimary) },
            text = {
                Column {
                    Text("Available Wallet: ₹${w.withdrawWallet.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = withdrawAmountText,
                        onValueChange = { withdrawAmountText = it },
                        label = { Text("Enter Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = withdrawMethod == "BANK_TRANSFER",
                            onClick = { withdrawMethod = "BANK_TRANSFER" },
                            label = { Text("Bank Transfer", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = withdrawMethod == "UPI",
                            onClick = { withdrawMethod = "UPI" },
                            label = { Text("UPI Instant", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = accountOrUpiDetails,
                        onValueChange = { accountOrUpiDetails = it },
                        label = { Text(if (withdrawMethod == "UPI") "UPI ID (e.g., name@upi)" else "Bank Account & IFSC") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 10% Deduction Breakdown Box
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AquaSoft)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Deduction Breakdown (${totalDedPct.toInt()}% Total):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoyalBlueDark)
                            Text("• Admin Charge (${adminDedPct.toInt()}%): -₹${adminDedAmt.toInt()}", fontSize = 11.sp, color = TextMuted)
                            Text("• TDS Charge (${tdsDedPct.toInt()}%): -₹${tdsDedAmt.toInt()}", fontSize = 11.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("NET PAYABLE AMOUNT: ₹${netPayable.coerceAtLeast(0.0).toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (requestedAmt > 0 && requestedAmt <= w.withdrawWallet) {
                            onRequestWithdrawal(requestedAmt, withdrawMethod, accountOrUpiDetails)
                            showWithdrawModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                ) {
                    Text("SUBMIT WITHDRAWAL", color = RoyalBlueDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawModal = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun WalletSubItem(title: String, amount: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(amount, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
