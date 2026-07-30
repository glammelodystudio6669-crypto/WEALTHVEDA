package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CompanySettingsEntity
import com.example.ui.AppScreen
import com.example.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    settings: CompanySettingsEntity?,
    onNavigate: (AppScreen) -> Unit
) {
    val scrollState = rememberScrollState()

    // Floating bubble animation state
    val infiniteTransition = rememberInfiniteTransition(label = "bubble")
    val bubbleOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bubbleY"
    )

    val pkgPrice = settings?.packagePrice ?: 6300.0
    val refBonus = settings?.referralIncome ?: 300.0
    val pairIncome = settings?.pairIncome ?: 600.0
    val dailyCap = settings?.dailyCeilingAmount ?: 6000.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
    ) {
        // ================= HERO SECTION =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .background(RoyalBlueDark)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.img_hero_banner_water_1785399787838),
                contentDescription = "Wealth Veda Hero Water",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.45f
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                RoyalBlueDark.copy(alpha = 0.6f),
                                RoyalBlueDark.copy(alpha = 0.85f),
                                SurfaceDark
                            )
                        )
                    )
            )

            // Floating Water Bubble FX
            Box(modifier = Modifier.offset(y = bubbleOffsetY.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = AquaBright.copy(alpha = 0.25f), radius = 18f, center = androidx.compose.ui.geometry.Offset(100f, 200f))
                    drawCircle(color = GoldBright.copy(alpha = 0.2f), radius = 24f, center = androidx.compose.ui.geometry.Offset(320f, 150f))
                    drawCircle(color = AquaBright.copy(alpha = 0.3f), radius = 12f, center = androidx.compose.ui.geometry.Offset(240f, 320f))
                }
            }

            // Hero Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = RoyalBluePrimary.copy(alpha = 0.7f),
                    border = BorderStroke(1.dp, GoldAccent)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.WaterDrop, contentDescription = null, tint = AquaBright, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PREMIUM ALKALINE WATER & BINARY BUSINESS",
                            color = GoldBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "WEALTH VEDA",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Healthy Water... Healthy Wealth...",
                    color = GoldBright,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Jar Hero Floating Frame
                Card(
                    modifier = Modifier
                        .size(160.dp)
                        .shadow(16.dp, CircleShape)
                        .border(2.dp, GoldBright, CircleShape),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = RoyalBluePrimary)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_alkaline_water_jar_1785399769251),
                        contentDescription = "Premium Alkaline Water Jar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { onNavigate(AppScreen.STORE) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = RoyalBlueDark),
                        shape = RoundedCornerShape(24.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BUY JAR (₹${pkgPrice.toInt()})", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onNavigate(AppScreen.AUTH) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.5.dp, GoldBright),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(18.dp), tint = GoldBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("JOIN NOW", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // ================= ANIMATED STATS COUNTERS =================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(y = (-20).dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCounterItem("15,420+", "ACTIVE USERS", Icons.Default.Groups)
                VerticalDivider(modifier = Modifier.height(36.dp), color = TextMuted.copy(alpha = 0.3f))
                StatCounterItem("₹1.2 Cr+", "TOTAL PAYOUT", Icons.Default.MonetizationOn)
                VerticalDivider(modifier = Modifier.height(36.dp), color = TextMuted.copy(alpha = 0.3f))
                StatCounterItem("8,500+", "JARS DELIVERED", Icons.Default.LocalShipping)
            }
        }

        // ================= PRODUCT & HEALTH BENEFITS =================
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "WHY ALKALINE WATER?",
                color = RoyalBluePrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Transform your health with ionized, mineral-rich micro-clustered water.",
                color = TextMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BenefitCard(
                    title = "9.5 pH Balance",
                    desc = "Neutralizes acidity, balances body pH levels",
                    icon = Icons.Default.InvertColors,
                    modifier = Modifier.weight(1f)
                )
                BenefitCard(
                    title = "-250mV ORP",
                    desc = "High antioxidant power anti-aging defense",
                    icon = Icons.Default.Shield,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BenefitCard(
                    title = "Micro-Clustered",
                    desc = "3x faster cellular hydration & detoxification",
                    icon = Icons.Default.Grain,
                    modifier = Modifier.weight(1f)
                )
                BenefitCard(
                    title = "Mineral Enriched",
                    desc = "Natural Calcium, Magnesium & Potassium",
                    icon = Icons.Default.FitnessCenter,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // ================= BUSINESS PLAN OVERVIEW =================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = RoyalBlueDark)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = GoldBright, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BINARY BUSINESS PLAN",
                        color = GoldBright,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Single Package Entry: ₹${pkgPrice.toInt()} (100 BV / 100 PV)",
                    color = AquaSoft,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                PlanRuleRow("Direct Sponsor Bonus", "₹${refBonus.toInt()} per referral")
                PlanRuleRow("Binary Match Ratio", "1:1 (Left:Right)")
                PlanRuleRow("Pair Match Income", "₹${pairIncome.toInt()} per pair")
                PlanRuleRow("Daily Pair Cap Limit", "10 Pairs / Day (Max ₹${dailyCap.toInt()}/day)")
                PlanRuleRow("Daily Non-Working ROI", "0.5% Daily (Mon - Fri)")
                PlanRuleRow("3X Re-Topup Rule", "Income Capped at 3X Total Income")
                PlanRuleRow("System Deductions", "10% Total (5% Admin + 5% TDS)")
            }
        }

        // ================= INTERACTIVE INCOME CALCULATOR =================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            border = BorderStroke(1.dp, GoldAccent)
        ) {
            var pairsSlider by remember { mutableStateOf(5f) }
            val dailyIncome = pairsSlider * pairIncome
            val monthlyIncome = dailyIncome * 26 // 26 working days
            val yearlyIncome = monthlyIncome * 12

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "INCOME CALCULATOR",
                    color = RoyalBluePrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Simulate your potential binary pair income",
                    color = TextMuted,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Daily Pairs Matched: ${pairsSlider.roundToInt()} Pairs/Day",
                    color = RoyalBlueDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Slider(
                    value = pairsSlider,
                    onValueChange = { pairsSlider = it },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = GoldAccent,
                        activeTrackColor = RoyalBluePrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IncomeResultBox("Daily", "₹${dailyIncome.toInt()}", Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    IncomeResultBox("Monthly", "₹${monthlyIncome.toInt()}", Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    IncomeResultBox("Yearly", "₹${yearlyIncome.toInt()}", Modifier.weight(1f))
                }
            }
        }

        // ================= TESTIMONIALS =================
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "SUCCESS STORIES",
                color = RoyalBluePrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            TestimonialCard(
                name = "Ramesh Kumar (Crown Leader)",
                city = "New Delhi",
                quote = "Wealth Veda changed my life. The Alkaline Water Jar improved my health, and the 1:1 binary plan paid ₹1.8 Lakhs in 3 months!"
            )
            Spacer(modifier = Modifier.height(8.dp))
            TestimonialCard(
                name = "Ananya Sen",
                city = "Kolkata",
                quote = "Seamless daily withdrawal payouts and genuine health benefits. Best direct selling company in India!"
            )
        }

        // ================= FAQ & CONTACT FOOTER =================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = RoyalBlueDark)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "CONTACT WEALTH VEDA",
                    color = GoldBright,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = AquaBright)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Corporate Tower, Suite 802, MG Road, Bengaluru", color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = AquaBright)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("support@wealthveda.com", color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = AquaBright)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("+91 1800-VEDA-WATER (Toll Free)", color = Color.White, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "© 2026 WEALTH VEDA PRIVATE LIMITED. All Rights Reserved.",
                    color = TextMuted,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun StatCounterItem(value: String, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = RoyalBluePrimary, modifier = Modifier.size(20.dp))
        Text(value, color = RoyalBlueDark, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BenefitCard(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = AquaBright, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, color = RoyalBlueDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = TextMuted, fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun PlanRuleRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        Text(value, color = GoldBright, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun IncomeResultBox(period: String, amount: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = AquaSoft
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(period, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(amount, color = RoyalBluePrimary, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun TestimonialCard(name: String, city: String, quote: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(quote, color = TextDark, fontSize = 12.sp, style = androidx.compose.ui.text.TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))
            Spacer(modifier = Modifier.height(6.dp))
            Text("$name ($city)", color = RoyalBluePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
