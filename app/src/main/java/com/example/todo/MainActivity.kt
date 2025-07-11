package com.example.todo

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: ToDoaAdapter
    private lateinit var todoList: MutableList<ToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoList = loadToDoList()
        todoAdapter = ToDoaAdapter(todoList) {
            saveToDoList(todoList)
        }

        val rvToDoItems = findViewById<RecyclerView>(R.id.rvTodoItems)
        rvToDoItems.adapter = todoAdapter
        rvToDoItems.layoutManager = LinearLayoutManager(this)

        val btnToDoAdd = findViewById<Button>(R.id.btnToDoAdd)
        val etTodoTitle = findViewById<EditText>(R.id.etTodoTitle)
        val btnToDoDelete = findViewById<Button>(R.id.btnToDoDelete)

        btnToDoAdd.setOnClickListener {
            val todoTitle = etTodoTitle.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = ToDo(todoTitle)
                todoAdapter.addToDo(todo)
                etTodoTitle.text.clear()
                saveToDoList(todoList)
            }
        }

        btnToDoDelete.setOnClickListener {
            todoAdapter.deleteDoneTodos()
            saveToDoList(todoList)
        }
    }

    private fun saveToDoList(list: List<ToDo>) {
        val sharedPreferences = getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(list)
        editor.putString("todo_list", json)
        editor.apply()
    }

    private fun loadToDoList(): MutableList<ToDo> {
        val sharedPreferences = getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("todo_list", null)
        val type = object : TypeToken<MutableList<ToDo>>() {}.type
        return Gson().fromJson(json, type) ?: mutableListOf()
    }
}
