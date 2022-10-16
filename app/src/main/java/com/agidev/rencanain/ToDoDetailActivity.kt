package com.agidev.rencanain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.agidev.rencanain.database.RencanainDatabase
import com.agidev.rencanain.model.ToDo
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class ToDoDetailActivity : AppCompatActivity() {

    private lateinit var toDo: ToDo

    private lateinit var todoDetailTitle: TextView

    private lateinit var db: RencanainDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_detail)

        todoDetailTitle = findViewById(R.id.todoDetailTitle)

        db =
            Room.databaseBuilder(
                applicationContext,
                RencanainDatabase::class.java,
                RencanainDatabase.getDatabaseName
            )
                .build()
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            try {
                val id: Int = intent.getIntExtra("id", 0)
                if(id == -1){
                    toDo = ToDo()
                }
                else
                {
                    toDo = db.toDoDao().readOneById(id)
                }
                Log.d("onStart", "onStart: ${toDo.toString()}")
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(applicationContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ContextCompat.startActivity(applicationContext, intent, Bundle.EMPTY)
    }
}