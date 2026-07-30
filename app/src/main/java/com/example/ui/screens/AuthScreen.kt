package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    onLogin: (String, String, Boolean) -> Unit,
    onRegister: (String, String, String, String, String, String, Boolean) -> Unit
) {
    var isLoginTab by remember { mutableStateOf(true) }
    var isAdminLogin by remember { mutableStateOf(false) }

    // Form fields
    var userIdOrEmail by remember { mutableStateOf("WV10001") }
    var password by remember { mutableStateOf("user123") }

    var fullName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regMobile by remember { mutableStateOf("") }
    var regPass by remember { mutableStateOf("") }
    var sponsorId by remember { mutableStateOf("WV10000") }
    var placement by remember { mutableStateOf("LEFT") } // "LEFT", "RIGHT", "AUTO"
    var isFreeRegistration by remember { mutableStateOf(true) }

    var showForgotModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header Toggle Tab
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AquaSoft, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (isLoginTab) RoyalBluePrimary else Color.Transparent, RoundedCornerShape(10.dp))
                            .clickable { isLoginTab = true }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("LOGIN", color = if (isLoginTab) GoldBright else TextMuted, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (!isLoginTab) RoyalBluePrimary else Color.Transparent, RoundedCornerShape(10.dp))
                            .clickable { isLoginTab = false }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("REGISTER", color = if (!isLoginTab) GoldBright else TextMuted, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (isLoginTab) {
                    // LOGIN FORM
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAdminLogin) "ADMIN PORTAL" else "MEMBER LOGIN",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBluePrimary
                        )

                        FilterChip(
                            selected = isAdminLogin,
                            onClick = {
                                isAdminLogin = !isAdminLogin
                                userIdOrEmail = if (isAdminLogin) "WV10000" else "WV10001"
                            },
                            label = { Text(if (isAdminLogin) "Admin Mode" else "Member Mode", fontSize = 11.sp) },
                            leadingIcon = { Icon(if (isAdminLogin) Icons.Default.AdminPanelSettings else Icons.Default.Person, null) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = userIdOrEmail,
                        onValueChange = { userIdOrEmail = it },
                        label = { Text("User ID or Email") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.AccountCircle, null) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, null) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextTextButton(text = "Forgot Password?") { showForgotModal = true }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onLogin(userIdOrEmail, password, isAdminLogin) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("SIGN IN", color = GoldBright, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                } else {
                    // REGISTRATION FORM
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AquaSoft,
                        border = BorderStroke(1.dp, GoldAccent),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = RoyalBluePrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("100% FREE REGISTRATION LIVE", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = RoyalBlueDark)
                                Text("Zero joining fee • Reserve your spot in binary network", fontSize = 10.sp, color = TextDark)
                            }
                        }
                    }

                    Text("CREATE NEW ACCOUNT", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
                    Text("Select your preferred registration type", fontSize = 12.sp, color = TextMuted)

                    Spacer(modifier = Modifier.height(10.dp))

                    // Registration Type Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = isFreeRegistration,
                            onClick = { isFreeRegistration = true },
                            label = { Text("Free Reg (₹0)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            leadingIcon = { Icon(Icons.Default.VolunteerActivism, null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SuccessGreen,
                                selectedLabelColor = Color.White
                            )
                        )

                        FilterChip(
                            selected = !isFreeRegistration,
                            onClick = { isFreeRegistration = false },
                            label = { Text("Paid Topup (₹6,300)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            leadingIcon = { Icon(Icons.Default.ShoppingBag, null, modifier = Modifier.size(16.dp)) },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldAccent,
                                selectedLabelColor = RoyalBlueDark
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = sponsorId,
                        onValueChange = { sponsorId = it },
                        label = { Text("Sponsor ID (e.g., WV10000)") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.CardGiftcard, null, tint = GoldAccent) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Leg Placement:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("LEFT", "RIGHT", "AUTO").forEach { pos ->
                            FilterChip(
                                selected = placement == pos,
                                onClick = { placement = pos },
                                label = { Text(pos, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("e.g., Amit Kumar") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = regEmail,
                        onValueChange = { regEmail = it },
                        label = { Text("Email Address") },
                        placeholder = { Text("e.g., amit@gmail.com") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = regMobile,
                        onValueChange = { regMobile = it },
                        label = { Text("Mobile Number") },
                        placeholder = { Text("e.g., +91 9876543210") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = regPass,
                        onValueChange = { regPass = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val nameToUse = fullName.ifBlank { "New Member ${System.currentTimeMillis() % 1000}" }
                            val emailToUse = regEmail.ifBlank { "user${System.currentTimeMillis() % 1000}@wealthveda.com" }
                            val mobToUse = regMobile.ifBlank { "+91 9876543210" }
                            val passToUse = regPass.ifBlank { "user123" }

                            onRegister(nameToUse, emailToUse, mobToUse, passToUse, sponsorId, placement, isFreeRegistration)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFreeRegistration) SuccessGreen else GoldAccent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isFreeRegistration) Icons.Default.CheckCircle else Icons.Default.RocketLaunch,
                            contentDescription = null,
                            tint = if (isFreeRegistration) Color.White else RoyalBlueDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isFreeRegistration) "COMPLETE FREE REGISTRATION (₹0)" else "REGISTER & TOP-UP (₹6,300)",
                            color = if (isFreeRegistration) Color.White else RoyalBlueDark,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isFreeRegistration) "✓ Zero upfront payment • Instant spot reservation in binary tree" else "✓ Includes 20L Alkaline Water Jar + 100 BV Active Account Status",
                        fontSize = 10.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showForgotModal) {
        AlertDialog(
            onDismissRequest = { showForgotModal = false },
            title = { Text("Reset Password") },
            text = {
                Column {
                    Text("An OTP will be sent to your registered mobile/email to reset your password.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(value = "", onValueChange = {}, label = { Text("Enter User ID or Mobile") })
                }
            },
            confirmButton = {
                Button(onClick = { showForgotModal = false }) {
                    Text("Send OTP")
                }
            }
        )
    }
}

@Composable
private fun TextTextButton(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = RoyalBluePrimary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable { onClick() }
    )
}
