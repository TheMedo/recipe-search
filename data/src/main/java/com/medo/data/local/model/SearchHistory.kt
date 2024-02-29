package com.medo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "search_histories")
data class SearchHistory(
    val query: String,
    @PrimaryKey
    val id: String = query.lowercase(),
    val timestamp: Date = Date(),
)