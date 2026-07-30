package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        WalletEntity::class,
        BinaryNodeEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        TransactionEntity::class,
        KycEntity::class,
        CompanySettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun walletDao(): WalletDao
    abstract fun binaryNodeDao(): BinaryNodeDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun transactionDao(): TransactionDao
    abstract fun kycDao(): KycDao
    abstract fun companySettingsDao(): CompanySettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wealth_veda_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
