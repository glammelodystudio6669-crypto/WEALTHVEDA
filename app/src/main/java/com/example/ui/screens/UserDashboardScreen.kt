package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.AppScreen
import com.example.ui.theme.*

@Composable
fun UserDashboardScreen(
    user: UserEntity?,
    wallet: WalletEntity?,
    node: BinaryNodeEntity?,
    settings: CompanySettingsEntity?,
    onNavigate: (AppScreen) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val u = user ?: UserEntity(userId = "WV10001", name = "Rajesh Veda", email = "rajesh@wealthveda.com", mobile = "+91 9988776655", passwordHash = "user123", sponsorId = "WV10000", placement = "LEFT")
    val w = wallet ?: WalletEntity(userId = u.userId, incomeWallet = 8400.0, roiWallet = 1260.0, shoppingWallet = 2000.0, withdrawWallet = 940.0)
    val n = node ?: BinaryNodeEntity(userId = u.userId, parentId = "WV10000", position = "LEFT", leftLegCount = 45, rightLegCount = 38, leftBv = 4500, rightBv = 3800, carryLeftBv = 700)

    val maxCap = u.packageAmount * (settings?.reTopupMultiplier ?: 3.0) // ₹18,900
    val capProgress = (u.totalIncome / maxCap).coerceIn(0.0, 1.0).toFloat()

    val referralLink = "https://wealthveda.com/register?sponsor=${u.userId}&placement=${u.placement}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        if (u.packageAmount == 0.0 || u.status == "FREE_MEMBER") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AquaSoft),
                border = BorderStroke(1.5.dp, GoldAccent)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("FREE MEMBER ACCOUNT ACTIVE", color = RoyalBlueDark, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Top-up with ₹6,300 Alkaline Water Jar package anytime to activate binary income & pair matching!", color = TextDark, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onNavigate(AppScreen.STORE) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("TOP UP", color = RoyalBlueDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // ================= USER EXECUTIVE CARD =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = RoyalBlueDark),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(u.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("MEMBER ID: ${u.userId}", color = GoldBright, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SuccessGreen
                    ) {
                        Text(
                            text = u.status,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    InfoLabel("Sponsor", u.sponsorId)
                    InfoLabel("Package", "₹${u.packageAmount.toInt()}")
                    InfoLabel("Left Leg", "${n.leftLegCount} (${n.leftBv} BV)")
                    InfoLabel("Right Leg", "${n.rightLegCount} (${n.rightBv} BV)")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Referral Link Copy Bar
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = RoyalBluePrimary,
                    border = BorderStroke(1.dp, GoldAccent)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = GoldBright, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = referralLink,
                            color = Color.White,
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Wealth Veda Referral Link", referralLink)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Referral link copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = GoldBright, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= 3X RE-TOPUP & PAIR CAPPING PROGRESS =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("3X Re-Topup Meter", fontWeight = FontWeight.Bold, color = RoyalBlueDark, fontSize = 13.sp)
                    Text("₹${u.totalIncome.toInt()} / ₹${maxCap.toInt()}", fontWeight = FontWeight.Bold, color = GoldDark, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { capProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (capProgress > 0.85f) ErrorRed else GoldAccent,
                    trackColor = AquaSoft
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (capProgress >= 1.0f) "CAPPED! Please re-topup ₹6,300 to continue income." else "${((1 - capProgress) * 100).toInt()}% income limit remaining before 3X Re-topup",
                    fontSize = 11.sp,
                    color = if (capProgress >= 1.0f) ErrorRed else TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= 4 WALLETS GRID =================
        Text("MY WALLETS", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WalletCard("Income Wallet", "₹${w.incomeWallet.toInt()}", Icons.Default.AccountBalanceWallet, RoyalBluePrimary, Modifier.weight(1f))
            WalletCard("ROI Wallet", "₹${w.roiWallet.toInt()}", Icons.Default.Analytics, AquaLight, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            WalletCard("Shopping Wallet", "₹${w.shoppingWallet.toInt()}", Icons.Default.ShoppingBag, GoldDark, Modifier.weight(1f))
            WalletCard("Withdrawal Wallet", "₹${w.withdrawWallet.toInt()}", Icons.Default.Payments, SuccessGreen, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ================= QUICK ACTION SHORTCUTS =================
        Text("QUICK ACTIONS", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionButton("Genealogy", Icons.Default.AccountTree) { onNavigate(AppScreen.GENEALOGY_TREE) }
            QuickActionButton("Buy Jar", Icons.Default.ShoppingCart) { onNavigate(AppScreen.STORE) }
            QuickActionButton("Withdraw", Icons.Default.Payments) { onNavigate(AppScreen.WALLETS) }
            QuickActionButton("KYC", Icons.Default.VerifiedUser) { onNavigate(AppScreen.KYC_VERIFICATION) }
            QuickActionButton("Reports", Icons.Default.BarChart) { onNavigate(AppScreen.REPORTS) }
        }
    }
}

@Composable
private fun InfoLabel(title: String, value: String) {
    Column {
        Text(title, color = AquaBright, fontSize = 10.sp)
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun WalletCard(title: String, amount: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(title, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(amount, color = RoyalBlueDark, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun QuickActionButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = AquaSoft,
            border = BorderStroke(1.dp, GoldAccent),
            modifier = Modifier.size(52.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = RoyalBluePrimary, modifier = Modifier.size(24.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextDark)
    }
}
