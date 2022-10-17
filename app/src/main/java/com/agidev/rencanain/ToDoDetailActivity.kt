package com.agidev.rencanain

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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

    private var toDo: ToDo = ToDo()

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

//    on resume
    override fun onResume() {
        super.onResume()

        GlobalScope.launch {
            try {
                val id: Int = intent.getIntExtra("id", 0)

                // load data when id not null
                loadData(id)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        todoDetailTopAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadData(id: Int) {
        try {
            toDo = db.toDoDao().readOneById(id)
            if(toDo.id != null){
                todoDetailBody.text = toDo.body
                todoDetailTitle.text = toDo.title
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun save(){
        try {

            if(todoDetailTitle.text.isNotEmpty()){
                toDo.title = todoDetailTitle.text.toString()
            }else{toDo.title = ""}

            if(todoDetailBody.text.isNotEmpty()){
                toDo.body = todoDetailBody.text.toString()
            }else{toDo.body = ""}

            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
            val date = calendar.time
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)

            toDo.updated_at = simpleDateFormat

            // insert when id to do null
            if(toDo.id == null && (todoDetailTitle.text.isNotEmpty() || todoDetailBody.text.isNotEmpty()))
            {
                db.toDoDao().insertToDo(toDo)
            }

            // delete when title and body empty
            else if(toDo.id != null && todoDetailTitle.text.isEmpty() && todoDetailBody.text.isEmpty())
            {
                db.toDoDao().deleteToDo(toDo)
            }

            // update when title and body empty
            else if(toDo.id != null && (todoDetailTitle.text.isNotEmpty() || todoDetailBody.text.isNotEmpty()))
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