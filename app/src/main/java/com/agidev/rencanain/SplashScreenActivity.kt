package com.agidev.rencanain

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.agidev.rencanain.database.RencanainDatabase
import com.agidev.rencanain.databinding.ActivitySplashScreenBinding
import com.agidev.rencanain.model.ToDo
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    private lateinit var db: RencanainDatabase

    private var dataSet: List<ToDo> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.activity_splash_screen)

        db =
            Room.databaseBuilder(
                applicationContext,
                RencanainDatabase::class.java,
                RencanainDatabase.getDatabaseName
            )
                .build()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            dataSet = db.toDoDao().getToDoList()

            val gson = Gson()
            val data = gson.toJson(dataSet, List::class.java)

            val intent = Intent(applicationContext, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("todo_list", data)

            ContextCompat.startActivity(applicationContext, intent, Bundle.EMPTY)

            finish()
        }
    }
}