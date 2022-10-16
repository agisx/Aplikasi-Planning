package com.agidev.rencanain.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "todo_tbl")
data class ToDo(
    @ColumnInfo(name = "todo_title")
    var title: String = "",

    @ColumnInfo(name = "todo_body")
    val body: String = "",

    @ColumnInfo(name = "todo_created_at")
    val created_at: String = "",

    @ColumnInfo(name = "todo_updated_at")
    val updated_at: String = ""
    ){

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todo_id")
    var id: Int? = null

    override fun toString(): String {
        return "ToDo(title='$title', body='$body', created_at='$created_at', updated_at='$updated_at', id=$id)"
    }
}