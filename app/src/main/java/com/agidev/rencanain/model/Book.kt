package com.agidev.rencanain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author_name") val authorName: String,
    @ColumnInfo(name = "total_pages") val totalPages: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
