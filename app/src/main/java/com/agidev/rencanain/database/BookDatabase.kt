package com.agidev.rencanain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agidev.rencanain.dao.BookDao
import com.agidev.rencanain.model.Book

@Database(entities = [Book::class], version = 1)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}