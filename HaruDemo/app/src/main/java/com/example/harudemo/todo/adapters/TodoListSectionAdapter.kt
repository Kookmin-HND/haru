package com.example.harudemo.todo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harudemo.databinding.FragmentTodoListItemBinding
import com.example.harudemo.todo.types.Section
import com.example.harudemo.todo.types.Todo

class TodoListSectionAdapter(private val section: Section) :
    RecyclerView.Adapter<TodoListSectionAdapter.TodoListSectionViewHolder>() {
    inner class TodoListSectionViewHolder(private val itemBinding: FragmentTodoListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(todoItem: Todo) {
            // Section으로부터 받은 Todo를 단순히 데이터 삽입
            itemBinding.tvTodoContent.text = todoItem.todo.content
            itemBinding.tvTodoDate.text = todoItem.todoDate.date
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListSectionViewHolder {
        return TodoListSectionViewHolder(
            FragmentTodoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoListSectionViewHolder, position: Int) {
        // 새로 받은 TodoList내에서 같은 id끼리는 정렬되어 있지만 다른 id에서는 정렬되지 않아 보이는데 혼선이 있어
        // 새롭게 다시 정렬.
        section.todoList.sortWith(Comparator { v1, v2 ->
            val date1 = v1.todoDate.date.split('-').map { it.toInt() }
            val date2 = v2.todoDate.date.split('-').map { it.toInt() }
            if (date1[0] == date2[0]) {
                if (date1[1] == date2[1]) {
                    return@Comparator date1[2].compareTo(date2[2])
                }
                return@Comparator date1[1].compareTo(date2[1])
            }
            return@Comparator date1[0].compareTo(date2[0])
        })
        holder.bindItem(section.todoList[position])
    }

    override fun getItemCount(): Int {
        return section.todoList.size
    }

}