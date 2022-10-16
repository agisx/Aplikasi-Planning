package com.agidev.rencanain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.agidev.rencanain.dao.ToDoDao
import com.agidev.rencanain.model.ToDo

@Database(entities = [ToDo::class], version = 1)
abstract class RencanainDatabase: RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
    companion object{
        const val getDatabaseName: String = "rencanaindb"
    }
}