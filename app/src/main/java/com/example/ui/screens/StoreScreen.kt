package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.OrderEntity
import com.example.data.ProductEntity
import com.example.ui.theme.*

@Composable
fun StoreScreen(
    products: List<ProductEntity>,
    orders: List<OrderEntity>,
    onSubmitOrder: (String, Double, String, String) -> Unit,
    onUpdateCustomImage: (Int, String) -> Unit
) {
    val scrollState = rememberScrollState()

    var showBuyModal by remember { mutableStateOf(false) }
    var showCustomImageModal by remember { mutableStateOf(false) }

    var shippingAddress by remember { mutableStateOf("123 Veda Corporate Towers, MG Road, Bengaluru, India") }
    var contactMobile by remember { mutableStateOf("+91 9988776655") }

    var customImageInput by remember { mutableStateOf("") }

    val jarProduct = products.firstOrNull() ?: ProductEntity()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("ALKALINE WATER E-STORE", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
        Text("Official Product Store for WEALTH VEDA", fontSize = 12.sp, color = TextMuted)

        Spacer(modifier = Modifier.height(16.dp))

        // Product Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(RoyalBlueDark),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_alkaline_water_jar_1785399769251),
                        contentDescription = "Premium Alkaline Water Jar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = GoldAccent
                    ) {
                        Text(
                            text = "100 BV / 100 PV",
                            color = RoyalBlueDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(jarProduct.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
                        Text(jarProduct.tagline, fontSize = 12.sp, color = AquaLight, fontWeight = FontWeight.Medium)
                    }

                    Text("₹${jarProduct.price.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = RoyalBlueDark)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = jarProduct.description,
                    fontSize = 13.sp,
                    color = TextDark,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { showBuyModal = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = GoldBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BUY NOW (₹${jarProduct.price.toInt()})", color = GoldBright, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showCustomImageModal = true },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, GoldAccent)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = GoldDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("UPLOAD JAR PIC", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ORDER TRACKING HISTORY
        Text("MY ORDERS & TRACKING", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
        Spacer(modifier = Modifier.height(8.dp))

        if (orders.isEmpty()) {
            Text("No order history yet. Click 'BUY NOW' to order your Alkaline Water Jar.", fontSize = 12.sp, color = TextMuted)
        } else {
            orders.forEach { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("ORDER #${order.orderId}", fontWeight = FontWeight.Bold, color = RoyalBlueDark, fontSize = 13.sp)
                            Text(order.orderStatus, color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Text("Product: ${order.productName}", fontSize = 12.sp, color = TextDark)
                        Text("Amount: ₹${order.totalAmount.toInt()} | Tracking: ${order.trackingCode}", fontSize = 11.sp, color = TextMuted)
                    }
                }
            }
        }
    }

    // Buy Checkout Dialog
    if (showBuyModal) {
        AlertDialog(
            onDismissRequest = { showBuyModal = false },
            title = { Text("Checkout Premium Alkaline Jar", fontWeight = FontWeight.Bold, color = RoyalBluePrimary) },
            text = {
                Column {
                    Text("Package Price: ₹${jarProduct.price.toInt()} (Includes 100 BV & PV)")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = shippingAddress,
                        onValueChange = { shippingAddress = it },
                        label = { Text("Shipping Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = contactMobile,
                        onValueChange = { contactMobile = it },
                        label = { Text("Mobile Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSubmitOrder(jarProduct.name, jarProduct.price, shippingAddress, contactMobile)
                        showBuyModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent)
                ) {
                    Text("CONFIRM ORDER", color = RoyalBlueDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBuyModal = false }) { Text("Cancel") }
            }
        )
    }

    // Custom Image Upload Dialog
    if (showCustomImageModal) {
        AlertDialog(
            onDismissRequest = { showCustomImageModal = false },
            title = { Text("Upload Custom Jar Image", fontWeight = FontWeight.Bold, color = RoyalBluePrimary) },
            text = {
                Column {
                    Text("When you upload your own Alkaline Water Jar image, it will be saved in the database.", fontSize = 12.sp, color = TextMuted)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customImageInput,
                        onValueChange = { customImageInput = it },
                        label = { Text("Image URL or File Path") },
                        placeholder = { Text("e.g. /sdcard/my_jar.png") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (customImageInput.isNotBlank()) {
                            onUpdateCustomImage(jarProduct.id, customImageInput)
                        }
                        showCustomImageModal = false
                    }
                ) {
                    Text("SAVE IMAGE")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomImageModal = false }) { Text("Cancel") }
            }
        )
    }
}
