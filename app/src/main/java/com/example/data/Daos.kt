package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY joinDate DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    fun observeUserById(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE sponsorId = :sponsorId")
    fun getDirectReferrals(sponsorId: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1")
    suspend fun getWalletByUserId(userId: String): WalletEntity?

    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1")
    fun observeWalletByUserId(userId: String): Flow<WalletEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: WalletEntity)

    @Update
    suspend fun updateWallet(wallet: WalletEntity)
}

@Dao
interface BinaryNodeDao {
    @Query("SELECT * FROM binary_nodes WHERE userId = :userId LIMIT 1")
    suspend fun getNodeByUserId(userId: String): BinaryNodeEntity?

    @Query("SELECT * FROM binary_nodes WHERE userId = :userId LIMIT 1")
    fun observeNodeByUserId(userId: String): Flow<BinaryNodeEntity?>

    @Query("SELECT * FROM binary_nodes")
    fun getAllNodes(): Flow<List<BinaryNodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNode(node: BinaryNodeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNodes(nodes: List<BinaryNodeEntity>)

    @Update
    suspend fun updateNode(node: BinaryNodeEntity)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("UPDATE products SET customImageUri = :uri WHERE id = :id")
    suspend fun updateCustomImage(id: Int, uri: String)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY timestamp DESC")
    fun getOrdersByUser(userId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getTransactionsByUser(userId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(tx: TransactionEntity)
}

@Dao
interface KycDao {
    @Query("SELECT * FROM kyc_records WHERE userId = :userId LIMIT 1")
    suspend fun getKycByUserId(userId: String): KycEntity?

    @Query("SELECT * FROM kyc_records WHERE userId = :userId LIMIT 1")
    fun observeKycByUserId(userId: String): Flow<KycEntity?>

    @Query("SELECT * FROM kyc_records ORDER BY lastUpdated DESC")
    fun getAllKycRecords(): Flow<List<KycEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKyc(kyc: KycEntity)
}

@Dao
interface CompanySettingsDao {
    @Query("SELECT * FROM company_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): CompanySettingsEntity?

    @Query("SELECT * FROM company_settings WHERE id = 1 LIMIT 1")
    fun observeSettings(): Flow<CompanySettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: CompanySettingsEntity)
}
