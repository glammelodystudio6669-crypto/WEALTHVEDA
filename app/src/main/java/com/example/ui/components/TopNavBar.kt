package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserEntity
import com.example.ui.AppScreen
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    currentScreen: AppScreen,
    currentUser: UserEntity?,
    onNavigate: (AppScreen) -> Unit,
    onUserSwitch: (String) -> Unit
) {
    var showUserMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = RoyalBlueDark,
        tonalElevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & Company Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onNavigate(AppScreen.HOME) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, GoldAccent, CircleShape)
                            .background(RoyalBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_logo_1785399749358),
                            contentDescription = "Wealth Veda Logo",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "WEALTH VEDA",
                            color = GoldBright,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Healthy Water... Healthy Wealth...",
                            color = AquaBright,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Right Actions: Admin / User Switcher & Navigation Shortcuts
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Quick Action Badge
                    AssistChip(
                        onClick = {
                            if (currentUser?.role == "ADMIN") {
                                onNavigate(AppScreen.ADMIN_PANEL)
                            } else {
                                onNavigate(AppScreen.USER_DASHBOARD)
                            }
                        },
                        label = {
                            Text(
                                text = if (currentUser?.role == "ADMIN") "ADMIN PANEL" else currentUser?.userId ?: "LOGIN",
                                color = GoldBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (currentUser?.role == "ADMIN") Icons.Default.AdminPanelSettings else Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = RoyalBluePrimary.copy(alpha = 0.8f)
                        ),
                        border = AssistChipDefaults.assistChipBorder(borderColor = GoldAccent, enabled = true)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = { showUserMenu = !showUserMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showUserMenu,
                        onDismissRequest = { showUserMenu = false },
                        modifier = Modifier.background(RoyalBlueDark).border(1.dp, GoldAccent, RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text("Home Page", color = Color.White) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.HOME) },
                            leadingIcon = { Icon(Icons.Default.Home, null, tint = GoldAccent) }
                        )
                        DropdownMenuItem(
                            text = { Text("User Dashboard", color = Color.White) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.USER_DASHBOARD) },
                            leadingIcon = { Icon(Icons.Default.Dashboard, null, tint = GoldAccent) }
                        )
                        DropdownMenuItem(
                            text = { Text("Genealogy Tree", color = Color.White) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.GENEALOGY_TREE) },
                            leadingIcon = { Icon(Icons.Default.AccountTree, null, tint = GoldAccent) }
                        )
                        DropdownMenuItem(
                            text = { Text("Alkaline Water Store", color = Color.White) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.STORE) },
                            leadingIcon = { Icon(Icons.Default.ShoppingBag, null, tint = GoldAccent) }
                        )
                        DropdownMenuItem(
                            text = { Text("Wallets & Withdraw", color = Color.White) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.WALLETS) },
                            leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, null, tint = GoldAccent) }
                        )
                        DropdownMenuItem(
                            text = { Text("KYC Verification", color = Color.White) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.KYC_VERIFICATION) },
                            leadingIcon = { Icon(Icons.Default.VerifiedUser, null, tint = GoldAccent) }
                        )
                        DropdownMenuItem(
                            text = { Text("Income Reports", color = Color.White) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.REPORTS) },
                            leadingIcon = { Icon(Icons.Default.BarChart, null, tint = GoldAccent) }
                        )
                        HorizontalDivider(color = CardGlassBorder)
                        DropdownMenuItem(
                            text = { Text("Admin & Simulator", color = GoldBright, fontWeight = FontWeight.Bold) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.ADMIN_PANEL) },
                            leadingIcon = { Icon(Icons.Default.AdminPanelSettings, null, tint = GoldBright) }
                        )
                        DropdownMenuItem(
                            text = { Text("Login / Register", color = Color.White) },
                            onClick = { showUserMenu = false; onNavigate(AppScreen.AUTH) },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = AquaBright) }
                        )
                    }
                }
            }

            // Horizontal Category Navigation Scroll Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(RoyalBlueDark, RoyalBluePrimary, RoyalBlueDark)
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavTabButton("HOME", AppScreen.HOME, currentScreen, onNavigate)
                NavTabButton("DASHBOARD", AppScreen.USER_DASHBOARD, currentScreen, onNavigate)
                NavTabButton("GENEALOGY", AppScreen.GENEALOGY_TREE, currentScreen, onNavigate)
                NavTabButton("E-STORE", AppScreen.STORE, currentScreen, onNavigate)
                NavTabButton("WALLETS", AppScreen.WALLETS, currentScreen, onNavigate)
                NavTabButton("ADMIN", AppScreen.ADMIN_PANEL, currentScreen, onNavigate)
            }
        }
    }
}

@Composable
private fun NavTabButton(
    label: String,
    screen: AppScreen,
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    val selected = currentScreen == screen
    Text(
        text = label,
        color = if (selected) GoldBright else Color.White.copy(alpha = 0.8f),
        fontSize = 11.sp,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) RoyalBlueAccent.copy(alpha = 0.5f) else Color.Transparent)
            .clickable { onNavigate(screen) }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
