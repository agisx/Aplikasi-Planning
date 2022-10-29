package com.agidev.rencanain.dao

import androidx.annotation.NonNull
import androidx.room.*
import com.agidev.rencanain.model.ToDo

@Dao
interface ToDoDao {
    @Query("Select * from todo_tbl")
    fun getToDoList(): List<ToDo>

    @Insert
    fun insertToDo(@NonNull toDo: ToDo)

    @Update
    fun updateToDo(@NonNull toDo: ToDo)

    @Delete
    fun deleteToDo(@NonNull toDo: ToDo)

    // custom sql
    @Query("Select * from todo_tbl where :id")
    fun readOneById(id: Int): ToDo
}