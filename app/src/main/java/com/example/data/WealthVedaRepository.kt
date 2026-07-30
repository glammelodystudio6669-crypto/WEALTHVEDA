package com.example.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.math.min

class WealthVedaRepository(private val db: AppDatabase) {

    val userDao = db.userDao()
    val walletDao = db.walletDao()
    val binaryNodeDao = db.binaryNodeDao()
    val productDao = db.productDao()
    val orderDao = db.orderDao()
    val transactionDao = db.transactionDao()
    val kycDao = db.kycDao()
    val settingsDao = db.companySettingsDao()

    // Seeds initial database if empty
    suspend fun initializeDefaultData() = withContext(Dispatchers.IO) {
        val existingSettings = settingsDao.getSettings()
        if (existingSettings == null) {
            val defaultSettings = CompanySettingsEntity()
            settingsDao.updateSettings(defaultSettings)

            // Seed Product
            val defaultProduct = ProductEntity(
                id = 1,
                name = "Premium Alkaline Water Jar",
                tagline = "Healthy Water... Healthy Wealth...",
                price = 6300.0,
                bv = 100,
                pv = 100,
                description = "20L Alkaline Ionized Water Jar with pH 8.5-9.5, negative ORP antioxidant water, multi-stage mineral enrichment, stainless steel faucet, and food-grade smart casing."
            )
            productDao.insertProduct(defaultProduct)

            // Seed Admin User
            val adminUser = UserEntity(
                userId = "WV10000",
                name = "Wealth Veda Admin",
                email = "admin@wealthveda.com",
                mobile = "+91 9876543210",
                passwordHash = "admin123",
                sponsorId = "ROOT",
                placement = "NONE",
                role = "ADMIN",
                totalIncome = 45000.0,
                totalPairsMatched = 75
            )
            userDao.insertUser(adminUser)
            walletDao.insertWallet(WalletEntity(userId = "WV10000", incomeWallet = 25000.0, roiWallet = 5000.0, shoppingWallet = 15000.0, withdrawWallet = 0.0))
            binaryNodeDao.insertNode(BinaryNodeEntity(userId = "WV10000", parentId = null, position = null, leftChildId = "WV10001", rightChildId = "WV10004", leftLegCount = 120, rightLegCount = 110, leftBv = 12000, rightBv = 11000, carryLeftBv = 1000, carryRightBv = 0))

            // Seed Main User
            val user1 = UserEntity(
                userId = "WV10001",
                name = "Rajesh Veda",
                email = "rajesh@wealthveda.com",
                mobile = "+91 9988776655",
                passwordHash = "user123",
                sponsorId = "WV10000",
                placement = "LEFT",
                totalIncome = 12600.0,
                totalPairsMatched = 18
            )
            userDao.insertUser(user1)
            walletDao.insertWallet(WalletEntity(userId = "WV10001", incomeWallet = 8400.0, roiWallet = 1260.0, shoppingWallet = 2000.0, withdrawWallet = 940.0))
            binaryNodeDao.insertNode(BinaryNodeEntity(userId = "WV10001", parentId = "WV10000", position = "LEFT", leftChildId = "WV10002", rightChildId = "WV10003", leftLegCount = 45, rightLegCount = 38, leftBv = 4500, rightBv = 3800, carryLeftBv = 700, carryRightBv = 0))

            // Seed Left & Right Members
            val user2 = UserEntity(userId = "WV10002", name = "Sunita Sharma", email = "sunita@gmail.com", mobile = "+91 9123456789", passwordHash = "pass123", sponsorId = "WV10001", placement = "LEFT", totalIncome = 3600.0, totalPairsMatched = 5)
            val user3 = UserEntity(userId = "WV10003", name = "Vikram Patel", email = "vikram@gmail.com", mobile = "+91 9812345678", passwordHash = "pass123", sponsorId = "WV10001", placement = "RIGHT", totalIncome = 2400.0, totalPairsMatched = 3)
            val user4 = UserEntity(userId = "WV10004", name = "Priya Roy", email = "priya@gmail.com", mobile = "+91 9712345678", passwordHash = "pass123", sponsorId = "WV10000", placement = "RIGHT", totalIncome = 1800.0, totalPairsMatched = 2)

            userDao.insertUsers(listOf(user2, user3, user4))

            walletDao.insertWallet(WalletEntity(userId = "WV10002", incomeWallet = 2100.0, roiWallet = 315.0, shoppingWallet = 1185.0))
            walletDao.insertWallet(WalletEntity(userId = "WV10003", incomeWallet = 1500.0, roiWallet = 315.0, shoppingWallet = 585.0))
            walletDao.insertWallet(WalletEntity(userId = "WV10004", incomeWallet = 1200.0, roiWallet = 315.0, shoppingWallet = 285.0))

            binaryNodeDao.insertNode(BinaryNodeEntity(userId = "WV10002", parentId = "WV10001", position = "LEFT", leftBv = 2000, rightBv = 1500, carryLeftBv = 500))
            binaryNodeDao.insertNode(BinaryNodeEntity(userId = "WV10003", parentId = "WV10001", position = "RIGHT", leftBv = 1200, rightBv = 1000, carryLeftBv = 200))
            binaryNodeDao.insertNode(BinaryNodeEntity(userId = "WV10004", parentId = "WV10000", position = "RIGHT", leftBv = 800, rightBv = 600, carryLeftBv = 200))

            // Seed Sample Transactions
            transactionDao.insertTransaction(TransactionEntity(userId = "WV10001", type = "REFERRAL", amount = 300.0, netAmount = 300.0, description = "Sponsor Bonus for Sunita Sharma (WV10002)"))
            transactionDao.insertTransaction(TransactionEntity(userId = "WV10001", type = "PAIR_MATCH", amount = 1200.0, netAmount = 1080.0, adminDeduction = 60.0, tdsDeduction = 60.0, description = "Binary Pair Match (2 Pairs matched)"))
            transactionDao.insertTransaction(TransactionEntity(userId = "WV10001", type = "ROI", amount = 31.50, netAmount = 31.50, description = "Daily 0.5% ROI Credit"))

            // Seed Sample KYC
            kycDao.insertKyc(KycEntity(userId = "WV10001", panNumber = "ABCDE1234F", aadhaarNumber = "1234 5678 9012", bankName = "HDFC Bank", accountNumber = "50100234567890", ifscCode = "HDFC0000123", status = "VERIFIED"))

            // Seed Initial Order
            orderDao.insertOrder(OrderEntity(orderId = "ORD-WV-1001", userId = "WV10001", productName = "Premium Alkaline Water Jar", quantity = 1, totalAmount = 6300.0, shippingAddress = "123 Veda Corporate Towers, MG Road, Bengaluru, India", mobile = "+91 9988776655", orderStatus = "DELIVERED"))
        }
    }

    suspend fun registerUser(
        name: String,
        email: String,
        mobile: String,
        password: String,
        sponsorId: String,
        placement: String,
        isFreeRegistration: Boolean = true
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        val settings = settingsDao.getSettings() ?: CompanySettingsEntity()
        val count = (System.currentTimeMillis() % 90000) + 10000
        val newUserId = "WV$count"

        val sponsor = userDao.getUserById(sponsorId) ?: return@withContext Result.failure(Exception("Sponsor ID $sponsorId not found"))

        val pkgAmt = if (isFreeRegistration) 0.0 else settings.packagePrice
        val pkgBv = if (isFreeRegistration) 0 else 100
        val pkgPv = if (isFreeRegistration) 0 else 100
        val userStatus = if (isFreeRegistration) "FREE_MEMBER" else "ACTIVE"

        val newUser = UserEntity(
            userId = newUserId,
            name = name,
            email = email,
            mobile = mobile,
            passwordHash = password,
            sponsorId = sponsorId,
            placement = placement,
            status = userStatus,
            packageAmount = pkgAmt,
            packageBv = pkgBv,
            packagePv = pkgPv
        )
        userDao.insertUser(newUser)

        // Initialize Wallet
        walletDao.insertWallet(WalletEntity(userId = newUserId, shoppingWallet = 0.0))

        // Initialize KYC record
        kycDao.insertKyc(KycEntity(userId = newUserId, status = "PENDING"))

        // Add binary node under sponsor
        val sponsorNode = binaryNodeDao.getNodeByUserId(sponsorId)
        val pos = if (placement == "LEFT" || placement == "RIGHT") placement else "LEFT"

        if (sponsorNode != null) {
            if (pos == "LEFT" && sponsorNode.leftChildId == null) {
                binaryNodeDao.updateNode(sponsorNode.copy(leftChildId = newUserId))
                binaryNodeDao.insertNode(BinaryNodeEntity(userId = newUserId, parentId = sponsorId, position = "LEFT"))
            } else if (pos == "RIGHT" && sponsorNode.rightChildId == null) {
                binaryNodeDao.updateNode(sponsorNode.copy(rightChildId = newUserId))
                binaryNodeDao.insertNode(BinaryNodeEntity(userId = newUserId, parentId = sponsorId, position = "RIGHT"))
            } else {
                // Attach down line
                binaryNodeDao.insertNode(BinaryNodeEntity(userId = newUserId, parentId = sponsorId, position = pos))
            }
        } else {
            binaryNodeDao.insertNode(BinaryNodeEntity(userId = newUserId, parentId = sponsorId, position = pos))
        }

        // Credit Sponsor Bonus and Propagate BV if paid registration
        if (!isFreeRegistration) {
            val sponsorWallet = walletDao.getWalletByUserId(sponsorId) ?: WalletEntity(userId = sponsorId)
            val updatedSponsorWallet = sponsorWallet.copy(
                incomeWallet = sponsorWallet.incomeWallet + settings.referralIncome,
                withdrawWallet = sponsorWallet.withdrawWallet + settings.referralIncome
            )
            walletDao.updateWallet(updatedSponsorWallet)

            // Log Referral Transaction
            transactionDao.insertTransaction(
                TransactionEntity(
                    userId = sponsorId,
                    type = "REFERRAL",
                    amount = settings.referralIncome,
                    netAmount = settings.referralIncome,
                    description = "Direct Referral Bonus for $name ($newUserId)"
                )
            )

            // Propagate BV points up the tree & trigger binary matching
            propagateBvAndMatch(newUserId, 100, pos)
        } else {
            transactionDao.insertTransaction(
                TransactionEntity(
                    userId = sponsorId,
                    type = "REFERRAL_FREE",
                    amount = 0.0,
                    netAmount = 0.0,
                    description = "Free Registration Direct Referral: $name ($newUserId)"
                )
            )
        }

        Result.success(newUser)
    }

    private suspend fun propagateBvAndMatch(userId: String, bv: Int, initialSide: String) {
        var currentUserId: String? = userId
        var childUserId = userId

        while (currentUserId != null) {
            val node = binaryNodeDao.getNodeByUserId(currentUserId) ?: break
            val parentId = node.parentId ?: break
            val parentNode = binaryNodeDao.getNodeByUserId(parentId) ?: break

            val isLeft = parentNode.leftChildId == currentUserId || node.position == "LEFT"
            val updatedParent = if (isLeft) {
                parentNode.copy(
                    leftLegCount = parentNode.leftLegCount + 1,
                    leftBv = parentNode.leftBv + bv,
                    carryLeftBv = parentNode.carryLeftBv + bv
                )
            } else {
                parentNode.copy(
                    rightLegCount = parentNode.rightLegCount + 1,
                    rightBv = parentNode.rightBv + bv,
                    carryRightBv = parentNode.carryRightBv + bv
                )
            }
            binaryNodeDao.updateNode(updatedParent)

            // Perform Pair Matching for parent
            calculateAndCreditPairMatch(parentId)

            currentUserId = parentId
            childUserId = currentUserId
        }
    }

    suspend fun calculateAndCreditPairMatch(userId: String) = withContext(Dispatchers.IO) {
        val settings = settingsDao.getSettings() ?: CompanySettingsEntity()
        val node = binaryNodeDao.getNodeByUserId(userId) ?: return@withContext
        val user = userDao.getUserById(userId) ?: return@withContext

        // Check 3X Re-topup limit rule
        val maxAllowedIncome = user.packageAmount * settings.reTopupMultiplier
        if (settings.isReTopupEnabled && user.totalIncome >= maxAllowedIncome) {
            // Capped - user needs re-topup
            return@withContext
        }

        val availableLeft = node.carryLeftBv
        val availableRight = node.carryRightBv

        val matchedBv = min(availableLeft, availableRight)
        if (matchedBv >= 100) {
            val pairsMatched = matchedBv / 100
            val cappedPairs = min(pairsMatched, settings.dailyMaxPairs)

            val rawPairIncome = cappedPairs * settings.pairIncome
            val adminDeduction = rawPairIncome * (settings.adminDeductionPercent / 100.0)
            val tdsDeduction = rawPairIncome * (settings.tdsDeductionPercent / 100.0)
            val netPairIncome = rawPairIncome - adminDeduction - tdsDeduction

            // Update BV carry forward
            val usedBv = cappedPairs * 100
            val newCarryLeft = availableLeft - usedBv
            val newCarryRight = availableRight - usedBv

            binaryNodeDao.updateNode(
                node.copy(
                    carryLeftBv = newCarryLeft,
                    carryRightBv = newCarryRight
                )
            )

            // Credit Income
            val wallet = walletDao.getWalletByUserId(userId) ?: WalletEntity(userId = userId)
            walletDao.updateWallet(
                wallet.copy(
                    incomeWallet = wallet.incomeWallet + netPairIncome,
                    withdrawWallet = wallet.withdrawWallet + netPairIncome
                )
            )

            userDao.updateUser(
                user.copy(
                    totalIncome = user.totalIncome + netPairIncome,
                    totalPairsMatched = user.totalPairsMatched + cappedPairs
                )
            )

            // Transaction log
            transactionDao.insertTransaction(
                TransactionEntity(
                    userId = userId,
                    type = "PAIR_MATCH",
                    amount = rawPairIncome,
                    netAmount = netPairIncome,
                    adminDeduction = adminDeduction,
                    tdsDeduction = tdsDeduction,
                    description = "Binary Pair Match ($cappedPairs Pairs @ ₹${settings.pairIncome.toInt()}/pair)"
                )
            )
        }
    }

    suspend fun runDailyRoiCalculation() = withContext(Dispatchers.IO) {
        val settings = settingsDao.getSettings() ?: CompanySettingsEntity()
        if (!settings.isRoiEnabled) return@withContext

        val users = userDao.getUserById("WV10001") // Run for users
        // Iterate all active non-admin users
        db.userDao().getAllUsers().collect { userList ->
            userList.filter { it.role != "ADMIN" && it.status == "ACTIVE" }.forEach { user ->
                val maxAllowedIncome = user.packageAmount * settings.reTopupMultiplier
                if (!settings.isReTopupEnabled || user.totalIncome < maxAllowedIncome) {
                    val roiAmount = user.packageAmount * (settings.dailyRoiPercent / 100.0) // e.g. ₹31.50
                    val wallet = walletDao.getWalletByUserId(user.userId) ?: WalletEntity(userId = user.userId)

                    walletDao.updateWallet(
                        wallet.copy(
                            roiWallet = wallet.roiWallet + roiAmount,
                            withdrawWallet = wallet.withdrawWallet + roiAmount
                        )
                    )

                    userDao.updateUser(
                        user.copy(
                            totalIncome = user.totalIncome + roiAmount
                        )
                    )

                    transactionDao.insertTransaction(
                        TransactionEntity(
                            userId = user.userId,
                            type = "ROI",
                            amount = roiAmount,
                            netAmount = roiAmount,
                            description = "Daily ${settings.dailyRoiPercent}% ROI Credit (₹6,300 Package)"
                        )
                    )
                }
            }
        }
    }

    suspend fun generateBulkTestUsers(count: Int, targetSide: String, parentUserId: String = "WV10001") = withContext(Dispatchers.IO) {
        val newUsers = mutableListOf<UserEntity>()
        val newNodes = mutableListOf<BinaryNodeEntity>()
        val newWallets = mutableListOf<WalletEntity>()

        var currentParent = parentUserId
        val side = if (targetSide == "LEFT") "LEFT" else "RIGHT"

        for (i in 1..count) {
            val testId = "TEST-${(100000..999999).random()}"
            val testUser = UserEntity(
                userId = testId,
                name = "Test Member #$i",
                email = "test_$i@wealthveda.sim",
                mobile = "+91 90000${(10000..99999).random()}",
                passwordHash = "test123",
                sponsorId = parentUserId,
                placement = side,
                isTestUser = true
            )
            newUsers.add(testUser)
            newWallets.add(WalletEntity(userId = testId))
            newNodes.add(BinaryNodeEntity(userId = testId, parentId = currentParent, position = side))

            currentParent = testId
        }

        userDao.insertUsers(newUsers)
        binaryNodeDao.insertNodes(newNodes)

        // Bulk update BV on parent node
        val parentNode = binaryNodeDao.getNodeByUserId(parentUserId)
        if (parentNode != null) {
            val totalAddedBv = count * 100
            val updated = if (side == "LEFT") {
                parentNode.copy(
                    leftLegCount = parentNode.leftLegCount + count,
                    leftBv = parentNode.leftBv + totalAddedBv,
                    carryLeftBv = parentNode.carryLeftBv + totalAddedBv
                )
            } else {
                parentNode.copy(
                    rightLegCount = parentNode.rightLegCount + count,
                    rightBv = parentNode.rightBv + totalAddedBv,
                    carryRightBv = parentNode.carryRightBv + totalAddedBv
                )
            }
            binaryNodeDao.updateNode(updated)
            calculateAndCreditPairMatch(parentUserId)
        }
    }

    suspend fun requestWithdrawal(userId: String, amount: Double, withdrawMethod: String, details: String): Result<String> = withContext(Dispatchers.IO) {
        val wallet = walletDao.getWalletByUserId(userId) ?: return@withContext Result.failure(Exception("Wallet not found"))
        if (wallet.withdrawWallet < amount) {
            return@withContext Result.failure(Exception("Insufficient withdrawal wallet balance. Available: ₹${wallet.withdrawWallet}"))
        }

        val settings = settingsDao.getSettings() ?: CompanySettingsEntity()
        val adminDed = amount * (settings.adminDeductionPercent / 100.0)
        val tdsDed = amount * (settings.tdsDeductionPercent / 100.0)
        val netAmt = amount - adminDed - tdsDed

        // Deduct from wallet
        walletDao.updateWallet(wallet.copy(withdrawWallet = wallet.withdrawWallet - amount))

        // Log transaction
        transactionDao.insertTransaction(
            TransactionEntity(
                userId = userId,
                type = "WITHDRAWAL",
                amount = amount,
                netAmount = netAmt,
                adminDeduction = adminDed,
                tdsDeduction = tdsDed,
                description = "Withdrawal request via $withdrawMethod ($details) [10% Deducted]",
                status = "PENDING"
            )
        )

        Result.success("Withdrawal request of ₹$amount submitted successfully. Net Payable: ₹$netAmt")
    }
}
