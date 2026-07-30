package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    AUTH,
    USER_DASHBOARD,
    GENEALOGY_TREE,
    STORE,
    WALLETS,
    KYC_VERIFICATION,
    REPORTS,
    ADMIN_PANEL
}

class WealthVedaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = WealthVedaRepository(db)

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _currentUserId = MutableStateFlow("WV10001") // Default logged-in user
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    val currentUser: StateFlow<UserEntity?> = _currentUserId.flatMapLatest { id ->
        repository.userDao.observeUserById(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentWallet: StateFlow<WalletEntity?> = _currentUserId.flatMapLatest { id ->
        repository.walletDao.observeWalletByUserId(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentBinaryNode: StateFlow<BinaryNodeEntity?> = _currentUserId.flatMapLatest { id ->
        repository.binaryNodeDao.observeNodeByUserId(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentKyc: StateFlow<KycEntity?> = _currentUserId.flatMapLatest { id ->
        repository.kycDao.observeKycByUserId(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userTransactions: StateFlow<List<TransactionEntity>> = _currentUserId.flatMapLatest { id ->
        repository.transactionDao.getTransactionsByUser(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<TransactionEntity>> = repository.transactionDao.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userOrders: StateFlow<List<OrderEntity>> = _currentUserId.flatMapLatest { id ->
        repository.orderDao.getOrdersByUser(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<UserEntity>> = repository.userDao.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBinaryNodes: StateFlow<List<BinaryNodeEntity>> = repository.binaryNodeDao.getAllNodes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val companySettings: StateFlow<CompanySettingsEntity?> = repository.settingsDao.observeSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allProducts: StateFlow<List<ProductEntity>> = repository.productDao.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allKycRecords: StateFlow<List<KycEntity>> = repository.kycDao.getAllKycRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDefaultData()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setCurrentUser(userId: String) {
        _currentUserId.value = userId
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun loginUser(userIdOrEmail: String, pass: String, isAdminLogin: Boolean) {
        viewModelScope.launch {
            val users = allUsers.value
            val found = users.find { (it.userId.equals(userIdOrEmail, true) || it.email.equals(userIdOrEmail, true)) }
            if (found != null) {
                _currentUserId.value = found.userId
                if (isAdminLogin && found.role == "ADMIN") {
                    _currentScreen.value = AppScreen.ADMIN_PANEL
                    _toastMessage.value = "Welcome Admin ${found.name}"
                } else if (!isAdminLogin) {
                    _currentScreen.value = AppScreen.USER_DASHBOARD
                    _toastMessage.value = "Welcome back, ${found.name}"
                } else {
                    _toastMessage.value = "Invalid Admin credentials"
                }
            } else {
                // Auto login fallback for convenience in demo
                if (isAdminLogin) {
                    _currentUserId.value = "WV10000"
                    _currentScreen.value = AppScreen.ADMIN_PANEL
                    _toastMessage.value = "Logged in as Admin (WV10000)"
                } else {
                    _currentUserId.value = "WV10001"
                    _currentScreen.value = AppScreen.USER_DASHBOARD
                    _toastMessage.value = "Logged in as Rajesh Veda (WV10001)"
                }
            }
        }
    }

    fun registerNewUser(
        name: String,
        email: String,
        mobile: String,
        pass: String,
        sponsorId: String,
        placement: String,
        isFreeRegistration: Boolean = true
    ) {
        viewModelScope.launch {
            val res = repository.registerUser(name, email, mobile, pass, sponsorId, placement, isFreeRegistration)
            if (res.isSuccess) {
                val user = res.getOrNull()!!
                _currentUserId.value = user.userId
                _currentScreen.value = AppScreen.USER_DASHBOARD
                val modeStr = if (isFreeRegistration) "Free Member Account" else "Paid Alkaline Pack"
                _toastMessage.value = "Registration Successful ($modeStr)! ID: ${user.userId}"
            } else {
                _toastMessage.value = res.exceptionOrNull()?.message ?: "Registration failed"
            }
        }
    }

    fun requestWithdrawal(amount: Double, method: String, details: String) {
        viewModelScope.launch {
            val res = repository.requestWithdrawal(_currentUserId.value, amount, method, details)
            if (res.isSuccess) {
                _toastMessage.value = res.getOrNull()
            } else {
                _toastMessage.value = res.exceptionOrNull()?.message ?: "Withdrawal request failed"
            }
        }
    }

    fun updateKyc(pan: String, aadhaar: String, bank: String, account: String, ifsc: String) {
        viewModelScope.launch {
            val record = KycEntity(
                userId = _currentUserId.value,
                panNumber = pan,
                aadhaarNumber = aadhaar,
                bankName = bank,
                accountNumber = account,
                ifscCode = ifsc,
                status = "VERIFIED"
            )
            repository.kycDao.insertKyc(record)
            _toastMessage.value = "KYC Details Updated & Verified Successfully"
        }
    }

    fun submitOrder(productName: String, price: Double, address: String, mobile: String) {
        viewModelScope.launch {
            val orderId = "ORD-WV-${(1000..9999).random()}"
            val order = OrderEntity(
                orderId = orderId,
                userId = _currentUserId.value,
                productName = productName,
                quantity = 1,
                totalAmount = price,
                shippingAddress = address,
                mobile = mobile,
                orderStatus = "DISPATCHED",
                trackingCode = "WV-TRK-${(10000..99999).random()}"
            )
            repository.orderDao.insertOrder(order)
            _toastMessage.value = "Order #$orderId Placed Successfully! Alkaline Jar Shipped."
        }
    }

    fun generateBulkSimulatorUsers(count: Int, side: String) {
        viewModelScope.launch {
            repository.generateBulkTestUsers(count, side, _currentUserId.value)
            _toastMessage.value = "Successfully generated $count Test Users on $side Leg!"
        }
    }

    fun triggerDailyRoi() {
        viewModelScope.launch {
            repository.runDailyRoiCalculation()
            _toastMessage.value = "Daily 0.5% ROI Credit Engine Executed Successfully!"
        }
    }

    fun saveCompanySettings(newSettings: CompanySettingsEntity) {
        viewModelScope.launch {
            repository.settingsDao.updateSettings(newSettings)
            _toastMessage.value = "Company Plan & System Settings Saved!"
        }
    }

    fun updateCustomJarImage(id: Int, imageUri: String) {
        viewModelScope.launch {
            repository.productDao.updateCustomImage(id, imageUri)
            val settings = repository.settingsDao.getSettings() ?: CompanySettingsEntity()
            repository.settingsDao.updateSettings(settings.copy(customJarImageUri = imageUri))
            _toastMessage.value = "Alkaline Water Jar Custom Image Updated!"
        }
    }
}
