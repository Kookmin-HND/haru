package com.example.harudemo.fragments.todo_fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harudemo.R
import com.example.harudemo.databinding.FragmentTodoListBinding
import com.example.harudemo.fragments.TodoFragment
import com.example.harudemo.todo.TodoData
import com.example.harudemo.todo.types.Section
import com.example.harudemo.todo.adapters.TodoListAdapter
import com.example.harudemo.utils.CustomToast
import java.time.LocalDate
import kotlin.collections.ArrayList

class TodoListFragment : Fragment() {
    // UI Update를 위해 Instance 접근 가능하게끔하고,
    // 이를 Singleton 방식으로하여 객체를 하나로하여 재활용한다.
    companion object {
        private var _instance: TodoListFragment? = null
        val instance: TodoListFragment
            get() {
                if (_instance == null) {
                    _instance = TodoListFragment()
                }
                return _instance!!
            }

        val COLORS = listOf(
            "#EE4266",
            "#FC9E4F",
            "#ECF39E",
            "#114B5F",
            "#EDD382",
            "#90A955",
            "#E2E4F6",
            "#FF521B",
            "#1F271B",
            "#1A936F",
            "#DEFFFC",
        )
    }

    private var binding: FragmentTodoListBinding? = null
    private var callback: OnBackPressedCallback? = null
    private var todoFragment: TodoFragment = TodoFragment.instance
    private var todoListAdapter: TodoListAdapter = TodoListAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 뒤로가기 키 핸들링위한 함수, 뒤로가기 눌리면 todoFragment로 돌아간다.
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                todoFragment = TodoFragment.instance
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragments_frame, todoFragment)?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "하루"
        todoListAdapter = TodoListAdapter()
        binding?.rvTodoSectionList?.adapter = todoListAdapter
        binding?.rvTodoSectionList?.layoutManager = LinearLayoutManager(
            binding?.root?.context, LinearLayoutManager.VERTICAL, false
        )
    }

    override fun onResume() {
        super.onResume()
        // TodoFragment로부터 전달된 값에 따라 TodoList Fragment에서 표시할 정보를 sections 배열에 저장 후
        // Recycler View에 전달
        updateSections()
    }

    override fun onDetach() {
        super.onDetach()
        // 뒤로가기 키 이벤트 삭제
        callback?.remove()
    }

    private fun updateSections() {
        when (arguments?.getString("by") as String) {
            "today" -> {
                val today = LocalDate.now().toString()
                TodoData.API.getTodosByDate(
                    "cjeongmin27@gmail.com",
                    today,
                    false,
                    {
                        todoListAdapter.sections = listOf(
                            Section(
                                "하루",
                                it.first,
                                ArrayList(it.second.map { log -> arrayListOf(log) })
                            )
                        )
                        todoListAdapter.completed = false
                    },
                    {
                        CustomToast.makeText(
                            requireContext(),
                            "오늘의 목록을 불러오는데 실패하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
            }
            "week" -> {
                val dates = ArrayList<String>()
                var today = LocalDate.now()
                for (i in 1..7) {
                    dates.add(today.toString())
                    today = today.plusDays(1)
                }
                TodoData.API.getTodosByDateInDates("cjeongmin27@gmail.com", dates, false, {
                    val result: ArrayList<Section> = arrayListOf()
                    for (section in it) {
                        result.add(
                            Section(
                                section.key,
                                section.value.first,
                                ArrayList(section.value.second.map { log -> arrayListOf(log) })
                            )
                        )
                    }
                    todoListAdapter.sections = result
                    todoListAdapter.completed = false
                }, {
                    CustomToast.makeText(
                        requireContext(),
                        "일주일 목록을 불러오는데 실패하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                })

            }
            "all" -> {
                TodoData.API.getAllTodosByFolder("cjeongmin27@gmail.com", false, {
                    val result: ArrayList<Section> = arrayListOf()
                    for (section in it) {
                        result.add(Section(section.key, section.value.first, section.value.second))
                    }
                    todoListAdapter.sections = result
                    todoListAdapter.completed = false
                }, {
                    CustomToast.makeText(
                        requireContext(),
                        "모든 목록을 불러오는데 실패하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
            "completed" -> {
                // FIXME: 폴더 별로 정렬하는 것이 아니라, 날짜 별로 정렬해야 함. 수정 필요.
                TodoData.API.getAllTodosByFolder("cjeongmin27@gmail.com", true, {
                    val result: ArrayList<Section> = arrayListOf()
                    for (section in it) {
                        result.add(Section(section.key, section.value.first, section.value.second))
                    }
                    todoListAdapter.sections = result
                    todoListAdapter.completed = true
                }, {
                    CustomToast.makeText(
                        requireContext(),
                        "모든 완료 목록을 불러오는데 실패하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
            "folder" -> {
                val folderTitle = arguments?.getString("folder-title") as String
                TodoData.API.getTodosByFolder("cjeongmin27@gmail.com", folderTitle, false, {
                    todoListAdapter.sections = listOf(Section(folderTitle, it.first, it.second))
                    todoListAdapter.completed = false
                }, {
                    CustomToast.makeText(
                        requireContext(),
                        "#${folderTitle}를 불러오는데 실패하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }, {})
            }
            else -> {}
        }
    }

    // TODO: Recycler View에서 감지되면 알아서 바꾸도록 변경 필요.
    fun refreshView(): Boolean {
        return if (todoListAdapter.sections.isEmpty()) {
            binding?.rvTodoSectionList?.visibility = View.GONE
            binding?.tvEmpty?.visibility = View.VISIBLE
            false
        } else {
            binding?.rvTodoSectionList?.visibility = View.VISIBLE
            binding?.tvEmpty?.visibility = View.GONE
            true
        }
    }
}