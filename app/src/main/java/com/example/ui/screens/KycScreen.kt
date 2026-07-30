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
import com.example.data.KycEntity
import com.example.ui.theme.*

@Composable
fun KycScreen(
    kyc: KycEntity?,
    onUpdateKyc: (String, String, String, String, String) -> Unit
) {
    val scrollState = rememberScrollState()

    val currentKyc = kyc ?: KycEntity(userId = "WV10001", panNumber = "ABCDE1234F", aadhaarNumber = "1234 5678 9012", bankName = "HDFC Bank", accountNumber = "50100234567890", ifscCode = "HDFC0000123", status = "VERIFIED")

    var panNumber by remember { mutableStateOf(currentKyc.panNumber) }
    var aadhaarNumber by remember { mutableStateOf(currentKyc.aadhaarNumber) }
    var bankName by remember { mutableStateOf(currentKyc.bankName) }
    var accountNumber by remember { mutableStateOf(currentKyc.accountNumber) }
    var ifscCode by remember { mutableStateOf(currentKyc.ifscCode) }

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
                Text("KYC VERIFICATION", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
                Text("Submit PAN, Aadhaar & Bank Details for Payout Approval", fontSize = 12.sp, color = TextMuted)
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (currentKyc.status == "VERIFIED") SuccessGreen else WarningOrange
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (currentKyc.status == "VERIFIED") Icons.Default.Verified else Icons.Default.Pending,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(currentKyc.status, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PAN Card Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. PAN CARD DETAILS", fontWeight = FontWeight.Bold, color = RoyalBlueDark, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = panNumber,
                    onValueChange = { panNumber = it },
                    label = { Text("PAN Number (e.g. ABCDE1234F)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload PAN Photo Copy")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Aadhaar Card Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. AADHAAR CARD DETAILS", fontWeight = FontWeight.Bold, color = RoyalBlueDark, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = aadhaarNumber,
                    onValueChange = { aadhaarNumber = it },
                    label = { Text("12-Digit Aadhaar Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload Aadhaar Front & Back Copy")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bank Details Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLight)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. BANK ACCOUNT FOR PAYOUTS", fontWeight = FontWeight.Bold, color = RoyalBlueDark, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = bankName, onValueChange = { bankName = it }, label = { Text("Bank Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = accountNumber, onValueChange = { accountNumber = it }, label = { Text("Account Number") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = ifscCode, onValueChange = { ifscCode = it }, label = { Text("IFSC Code") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload Bank Passbook / Cheque Copy")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onUpdateKyc(panNumber, aadhaarNumber, bankName, accountNumber, ifscCode) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("SUBMIT KYC DOCUMENTS", color = GoldBright, fontWeight = FontWeight.Bold)
        }
    }
}
