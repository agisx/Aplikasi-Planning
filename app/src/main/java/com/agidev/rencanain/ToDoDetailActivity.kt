package com.agidev.rencanain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.agidev.rencanain.database.RencanainDatabase
import com.agidev.rencanain.databinding.ActivityToDoDetailBinding
import com.agidev.rencanain.model.ToDo
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@DelicateCoroutinesApi
class ToDoDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityToDoDetailBinding

    private lateinit var toDo: ToDo

    private lateinit var todoDetailTopAppBar: MaterialToolbar
    private lateinit var todoDetailTitle: TextView
    private lateinit var todoDetailBody: TextView

    private lateinit var db: RencanainDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToDoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoDetailTopAppBar = findViewById(R.id.todoDetailTopAppBar)
        todoDetailTitle = findViewById(R.id.todoDetailTitle)
        todoDetailBody = findViewById(R.id.todoDetailBody)

        db =
            Room.databaseBuilder(
                applicationContext,
                RencanainDatabase::class.java,
                RencanainDatabase.getDatabaseName
            )
                .build()
    }

    override fun onRestart() {
        super.onRestart()

        GlobalScope.launch {
            try {
                save()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            try {
                val id: Int = intent.getIntExtra("id", 0)

                loadData(id)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        todoDetailTopAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

//    on resume

    private fun loadData(id: Int) {
        try {
            toDo = db.toDoDao().readOneById(id)
            Log.d("onStart", "onStart: $toDo")
            todoDetailTitle.text = toDo.title
            todoDetailBody.text = toDo.body
        } catch (e: Exception){
            loadData(id)

            e.printStackTrace()
        }
    }

    private fun save(){
        try {
            toDo.title = todoDetailTitle.text.toString()
            toDo.body = todoDetailBody.text.toString()

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
            val date = calendar.time
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)

            toDo.updated_at = simpleDateFormat
            if(toDo.id == null){
                db.toDoDao().insertToDo(toDo)
            }
            else
            {
                db.toDoDao().updateToDo(toDo)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        GlobalScope.launch {
            save()
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        ContextCompat.startActivity(applicationContext, intent, Bundle.EMPTY)
    }
}