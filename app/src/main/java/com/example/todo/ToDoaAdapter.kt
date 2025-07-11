package com.example.todo

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoaAdapter(
    private val ToDos: MutableList<ToDo>,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<ToDoaAdapter.ToDoViewholder>() {

    class ToDoViewholder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewholder {
        return ToDoViewholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    fun addToDo(todo: ToDo) {
        ToDos.add(todo)
        notifyItemInserted(ToDos.size - 1)
        onDataChanged()
    }

    fun deleteDoneTodos() {
        ToDos.removeAll { toDo -> toDo.isChecked }
        notifyDataSetChanged()
        onDataChanged()
    }

    private fun toggleStrikeThrough(tvTodoTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodoTitle.paintFlags = tvTodoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: ToDoViewholder, position: Int) {
        val curToDo = ToDos[position]
        holder.itemView.apply {
            val tvTodoTitle = findViewById<TextView>(R.id.tvTodoTitle)
            val cbDone = findViewById<CheckBox>(R.id.cbDone)

            tvTodoTitle.text = curToDo.title
            cbDone.isChecked = curToDo.isChecked

            toggleStrikeThrough(tvTodoTitle, curToDo.isChecked)

            cbDone.setOnCheckedChangeListener(null)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(tvTodoTitle, isChecked)
                curToDo.isChecked = isChecked
                onDataChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return ToDos.size
    }
}
