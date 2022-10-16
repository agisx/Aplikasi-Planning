package com.agidev.rencanain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.agidev.rencanain.model.Book

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBooks(): List<Book>

    @Insert
    fun insert(vararg books: Book)

    @Delete
    fun delete(book: Book)
}