package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "binary_nodes")
data class BinaryNodeEntity(
    @PrimaryKey val userId: String,
    val parentId: String?, // Null for top root
    val position: String?, // "LEFT" or "RIGHT"
    val level: Int = 1,
    val leftChildId: String? = null,
    val rightChildId: String? = null,
    val leftLegCount: Int = 0,
    val rightLegCount: Int = 0,
    val leftBv: Int = 0,
    val rightBv: Int = 0,
    val carryLeftBv: Int = 0,
    val carryRightBv: Int = 0
)
