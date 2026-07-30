package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BinaryNodeEntity
import com.example.data.UserEntity
import com.example.ui.theme.*

@Composable
fun GenealogyTreeScreen(
    users: List<UserEntity>,
    nodes: List<BinaryNodeEntity>,
    rootUserId: String = "WV10001"
) {
    var searchQuery by remember { mutableStateOf("") }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    var selectedNodeUser by remember { mutableStateOf<UserEntity?>(null) }
    var activeRootId by remember { mutableStateOf(rootUserId) }

    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    val rootUser = users.find { it.userId == activeRootId } ?: users.firstOrNull() ?: UserEntity(userId = "WV10001", name = "Rajesh Veda", sponsorId = "WV10000", placement = "LEFT")
    val rootNode = nodes.find { it.userId == rootUser.userId } ?: BinaryNodeEntity(userId = rootUser.userId, parentId = "WV10000", position = "LEFT", leftLegCount = 45, rightLegCount = 38, leftBv = 4500, rightBv = 3800)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(12.dp)
    ) {
        // Search & Controls Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("BINARY GENEALOGY TREE", fontWeight = FontWeight.Bold, color = RoyalBluePrimary, fontSize = 16.sp)

                    Row {
                        IconButton(onClick = { zoomLevel = (zoomLevel + 0.1f).coerceAtMost(1.5f) }) {
                            Icon(Icons.Default.ZoomIn, contentDescription = "Zoom In", tint = RoyalBluePrimary)
                        }
                        IconButton(onClick = { zoomLevel = (zoomLevel - 0.1f).coerceAtLeast(0.6f) }) {
                            Icon(Icons.Default.ZoomOut, contentDescription = "Zoom Out", tint = RoyalBluePrimary)
                        }
                        IconButton(onClick = { activeRootId = rootUserId; zoomLevel = 1.0f }) {
                            Icon(Icons.Default.RestartAlt, contentDescription = "Reset Tree", tint = RoyalBluePrimary)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search User ID or Name...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = TextMuted) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val target = users.find { it.userId.equals(searchQuery.trim(), true) || it.name.contains(searchQuery.trim(), true) }
                            if (target != null) {
                                activeRootId = target.userId
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                    ) {
                        Text("FIND", color = RoyalBlueDark, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Status Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LegendItem("Active", SuccessGreen)
                    LegendItem("Inactive", WarningOrange)
                    LegendItem("Blocked", ErrorRed)
                    LegendItem("Pending", GoldBright)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tree Renderer Viewport
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, CardGlassBorder, RoundedCornerShape(12.dp))
                .background(RoyalBlueDark)
                .horizontalScroll(horizontalScroll)
                .verticalScroll(verticalScroll)
                .padding(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                // ROOT NODE
                TreeNodeCard(
                    user = rootUser,
                    node = rootNode,
                    isSearchMatch = searchQuery.isNotBlank() && rootUser.userId.contains(searchQuery, true),
                    onClick = { selectedNodeUser = rootUser }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // LEVEL 1: LEFT & RIGHT BRANCHES
                val leftChildId = rootNode.leftChildId ?: "WV10002"
                val rightChildId = rootNode.rightChildId ?: "WV10003"

                val leftUser = users.find { it.userId == leftChildId } ?: UserEntity(userId = leftChildId, name = "Sunita Sharma", sponsorId = rootUser.userId, placement = "LEFT")
                val leftNode = nodes.find { it.userId == leftChildId } ?: BinaryNodeEntity(userId = leftChildId, parentId = rootUser.userId, position = "LEFT", leftBv = 2000, rightBv = 1500)

                val rightUser = users.find { it.userId == rightChildId } ?: UserEntity(userId = rightChildId, name = "Vikram Patel", sponsorId = rootUser.userId, placement = "RIGHT")
                val rightNode = nodes.find { it.userId == rightChildId } ?: BinaryNodeEntity(userId = rightChildId, parentId = rootUser.userId, position = "RIGHT", leftBv = 1200, rightBv = 1000)

                Row(
                    modifier = Modifier.width(360.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("LEG: LEFT", color = AquaBright, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        TreeNodeCard(
                            user = leftUser,
                            node = leftNode,
                            isSearchMatch = searchQuery.isNotBlank() && leftUser.userId.contains(searchQuery, true),
                            onClick = { activeRootId = leftUser.userId }
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("LEG: RIGHT", color = GoldBright, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        TreeNodeCard(
                            user = rightUser,
                            node = rightNode,
                            isSearchMatch = searchQuery.isNotBlank() && rightUser.userId.contains(searchQuery, true),
                            onClick = { activeRootId = rightUser.userId }
                        )
                    }
                }
            }
        }
    }

    // Node Detail Modal
    if (selectedNodeUser != null) {
        val u = selectedNodeUser!!
        AlertDialog(
            onDismissRequest = { selectedNodeUser = null },
            title = { Text("Member Details: ${u.userId}", fontWeight = FontWeight.Bold, color = RoyalBluePrimary) },
            text = {
                Column {
                    Text("Name: ${u.name}", fontWeight = FontWeight.Bold)
                    Text("Sponsor ID: ${u.sponsorId}")
                    Text("Placement Leg: ${u.placement}")
                    Text("Package: ₹${u.packageAmount.toInt()}")
                    Text("Total Pairs Matched: ${u.totalPairsMatched}")
                    Text("Total Income Earned: ₹${u.totalIncome.toInt()}")
                    Text("Status: ${u.status}", color = SuccessGreen, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(onClick = { activeRootId = u.userId; selectedNodeUser = null }) {
                    Text("Set as Root")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedNodeUser = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 10.sp, color = TextDark, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun TreeNodeCard(
    user: UserEntity,
    node: BinaryNodeEntity,
    isSearchMatch: Boolean,
    onClick: () -> Unit
) {
    val statusColor = when (user.status) {
        "ACTIVE" -> SuccessGreen
        "INACTIVE" -> WarningOrange
        "BLOCKED" -> ErrorRed
        else -> GoldBright
    }

    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() }
            .border(
                width = if (isSearchMatch) 2.dp else 1.dp,
                color = if (isSearchMatch) GoldBright else CardGlassBorder,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalBluePrimary)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(statusColor))
                Spacer(modifier = Modifier.width(6.dp))
                Text(user.userId, color = GoldBright, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(user.name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1)

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(color = CardGlassBorder)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("LEFT", color = AquaBright, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("${node.leftLegCount}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("${node.leftBv} BV", color = TextMuted, fontSize = 8.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("RIGHT", color = GoldBright, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("${node.rightLegCount}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("${node.rightBv} BV", color = TextMuted, fontSize = 8.sp)
                }
            }
        }
    }
}
