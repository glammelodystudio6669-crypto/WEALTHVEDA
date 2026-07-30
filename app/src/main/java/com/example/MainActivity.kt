package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppScreen
import com.example.ui.WealthVedaViewModel
import com.example.ui.components.TopNavBar
import com.example.ui.screens.*
import com.example.ui.theme.WealthVedaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: WealthVedaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WealthVedaTheme {
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
                val currentWallet by viewModel.currentWallet.collectAsStateWithLifecycle()
                val currentBinaryNode by viewModel.currentBinaryNode.collectAsStateWithLifecycle()
                val currentKyc by viewModel.currentKyc.collectAsStateWithLifecycle()

                val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
                val allBinaryNodes by viewModel.allBinaryNodes.collectAsStateWithLifecycle()
                val userTransactions by viewModel.userTransactions.collectAsStateWithLifecycle()
                val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()
                val userOrders by viewModel.userOrders.collectAsStateWithLifecycle()
                val companySettings by viewModel.companySettings.collectAsStateWithLifecycle()
                val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
                val allKycRecords by viewModel.allKycRecords.collectAsStateWithLifecycle()

                val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

                LaunchedEffect(toastMessage) {
                    toastMessage?.let { msg ->
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        viewModel.clearToast()
                    }
                }

                Scaffold(
                    topBar = {
                        TopNavBar(
                            currentScreen = currentScreen,
                            currentUser = currentUser,
                            onNavigate = { viewModel.navigateTo(it) },
                            onUserSwitch = { viewModel.setCurrentUser(it) }
                        )
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            AppScreen.HOME -> HomeScreen(
                                settings = companySettings,
                                onNavigate = { viewModel.navigateTo(it) }
                            )

                            AppScreen.AUTH -> AuthScreen(
                                onLogin = { id, pass, isAdmin -> viewModel.loginUser(id, pass, isAdmin) },
                                onRegister = { name, email, mob, pass, spId, pos, isFree ->
                                    viewModel.registerNewUser(name, email, mob, pass, spId, pos, isFree)
                                }
                            )

                            AppScreen.USER_DASHBOARD -> UserDashboardScreen(
                                user = currentUser,
                                wallet = currentWallet,
                                node = currentBinaryNode,
                                settings = companySettings,
                                onNavigate = { viewModel.navigateTo(it) }
                            )

                            AppScreen.GENEALOGY_TREE -> GenealogyTreeScreen(
                                users = allUsers,
                                nodes = allBinaryNodes,
                                rootUserId = currentUser?.userId ?: "WV10001"
                            )

                            AppScreen.STORE -> StoreScreen(
                                products = allProducts,
                                orders = userOrders,
                                onSubmitOrder = { name, price, addr, mob -> viewModel.submitOrder(name, price, addr, mob) },
                                onUpdateCustomImage = { id, uri -> viewModel.updateCustomJarImage(id, uri) }
                            )

                            AppScreen.WALLETS -> WalletsAndWithdrawalScreen(
                                wallet = currentWallet,
                                transactions = userTransactions,
                                settings = companySettings,
                                onRequestWithdrawal = { amt, method, details -> viewModel.requestWithdrawal(amt, method, details) }
                            )

                            AppScreen.KYC_VERIFICATION -> KycScreen(
                                kyc = currentKyc,
                                onUpdateKyc = { pan, aadh, bank, acc, ifsc -> viewModel.updateKyc(pan, aadh, bank, acc, ifsc) }
                            )

                            AppScreen.REPORTS -> ReportsScreen(
                                user = currentUser,
                                transactions = userTransactions
                            )

                            AppScreen.ADMIN_PANEL -> AdminDashboardScreen(
                                users = allUsers,
                                transactions = allTransactions,
                                kycRecords = allKycRecords,
                                settings = companySettings,
                                onGenerateBulkSimulator = { count, side -> viewModel.generateBulkSimulatorUsers(count, side) },
                                onTriggerDailyRoi = { viewModel.triggerDailyRoi() },
                                onSaveSettings = { viewModel.saveCompanySettings(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
