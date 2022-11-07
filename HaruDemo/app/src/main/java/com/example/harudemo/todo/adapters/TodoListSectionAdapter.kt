package com.example.harudemo.todo.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harudemo.R
import com.example.harudemo.databinding.FragmentTodoListItemBinding
import com.example.harudemo.fragments.TodoFragment
import com.example.harudemo.fragments.todo_fragments.TodoListFragment
import com.example.harudemo.todo.TodoData
import com.example.harudemo.todo.TodoInputActivity
import com.example.harudemo.todo.types.Section
import com.example.harudemo.todo.types.Todo
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.schedule


class TodoListSectionAdapter(
    private val section: Section
) : RecyclerView.Adapter<TodoListSectionAdapter.TodoListSectionViewHolder>() {
    inner class TodoListSectionViewHolder(private val itemBinding: FragmentTodoListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun bindItem(todo: Todo) {
            // Section으로부터 받은 Todo를 단순히 데이터 삽입
            val days = arrayListOf("월", "화", "수", "목", "금", "토", "일")

            if (todo.completed) {
                itemBinding.btnCheckTodo.setBackgroundResource(R.drawable.todo_completed_check_button)
            }

            itemBinding.tvTodoContent.text = todo.content

            val dateToken = todo.date.split('-').map { it.toInt() }
            val date = LocalDate.of(dateToken[0], dateToken[1], dateToken[2])
            itemBinding.tvTodoDate.text = "$date (${days[date.dayOfWeek.value - 1]})"

            // Update 실행 Action
            itemBinding.root.setOnClickListener {
                if (todo.completed) {
                    return@setOnClickListener
                }

                val intent = Intent(it.context, TodoInputActivity::class.java)
                intent.putExtra("update", true)
                intent.putExtra("todo", todo)
                it.context.startActivity(intent)
            }

            // completed Toggle Action
            itemBinding.btnCheckTodo.setOnClickListener {
                TodoData.API.update(todo.id, todo.folder, todo.content, todo.date, true, {
                    TodoData.update(todo, completed = !todo.completed)
                    if (TodoData.getTodosByFolder(todo.folder).isEmpty()) {
                        TodoFragment.folderListAdapter.notifyDataSetChanged()
                    }
                    if (todo.completed) {
                        itemBinding.btnCheckTodo.setBackgroundResource(R.drawable.todo_completed_check_button)
                        itemBinding.tvTodoContent.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        itemBinding.btnCheckTodo.setBackgroundResource(R.drawable.todo_check_button)
                        itemBinding.tvTodoContent.paintFlags = Paint.LINEAR_TEXT_FLAG
                    }


                    Timer("Completed Item", true).schedule(500) {
                        TodoListFragment.instance.activity?.runOnUiThread {
                            TodoListFragment.instance.onResume()
                        }
                    }
                })
            }

            itemBinding.btnDelete.setOnClickListener {
                // Delete 버튼 클릭시 삭제한다.
                TodoData.API.delete(todo.id, {
                    TodoData.delete(todo)
                    TodoListFragment.instance.onResume()
                })
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListSectionViewHolder {
        return TodoListSectionViewHolder(
            FragmentTodoListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoListSectionViewHolder, position: Int) {
        // 날짜별 정렬
        section.todoList.sortWith(Comparator { v1, v2 ->
            val date1 = v1.date.split('-').map { it.toInt() }
            val date2 = v2.date.split('-').map { it.toInt() }
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