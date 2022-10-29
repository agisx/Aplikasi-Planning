package com.agidev.rencanain

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.agidev.rencanain.database.RencanainDatabase
import com.agidev.rencanain.databinding.ActivityMainBinding
import com.agidev.rencanain.model.ToDo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var rvTodoList: RecyclerView
    private lateinit var fabTodoList: FloatingActionButton

    private lateinit var db: RencanainDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvTodoList = findViewById(R.id.rvTodoList)
        fabTodoList = findViewById(R.id.fabTodoList)

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

        try {
            val bundle = intent.extras
            val todoList = bundle?.getString("todo_list")

            val gson = Gson()

            var data: List<ToDo>
            data = gson.fromJson(todoList, object : TypeToken<List<ToDo>>(){}.type)

            loadData(data)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()

        fabTodoList.setOnClickListener {
            val intent = Intent(applicationContext, ToDoDetailActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(applicationContext, intent, Bundle.EMPTY)
        }
    }

    private fun loadData(dataSet: List<ToDo> = listOf()) {
        // init recycle view
        val customAdapter = CustomAdapter(dataSet)

        try {
            rvTodoList.adapter = customAdapter
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    // Recycle view handler
    private class CustomAdapter(private val dataSet: List<ToDo> = listOf()) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTitle: TextView = view.findViewById(R.id.todoRowTitle)
            val tvBody: TextView = view.findViewById(R.id.todoRowBody)
            val tvDate: TextView = view.findViewById(R.id.todoRowDate)

            val todoRowCardView: CardView = view.findViewById(R.id.todoRowCardView)
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.todo_row_item, viewGroup, false)

            view.setOnClickListener {
                if(dataSet.isEmpty()){
                    Toast.makeText(view.context, "To Do tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val position: Int = view.tag as Int

                    val gson = Gson()
                    val toDo = gson.toJson(dataSet[position], ToDo::class.java)

                    val intent = Intent(viewGroup.context, ToDoDetailActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("toDo", toDo)

                    startActivity(viewGroup.context, intent, Bundle.EMPTY)
                }
            }

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            // Get element from your dataset at this position and replace the
            // contents of the view with that element

            val data = dataSet[position]

            viewHolder.todoRowCardView.tag = position

            viewHolder.tvTitle.text = data.title
            viewHolder.tvBody.text = data.body
            viewHolder.tvDate.text = data.created_at
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size
    }
}