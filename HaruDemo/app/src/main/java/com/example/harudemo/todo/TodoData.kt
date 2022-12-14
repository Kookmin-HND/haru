package com.example.harudemo.todo

import android.util.Log
import com.example.harudemo.retrofit.TodoRetrofitManager
import com.example.harudemo.todo.types.Section
import com.example.harudemo.todo.types.Todo
import com.example.harudemo.todo.types.TodoLog
import com.example.harudemo.utils.RESPONSE_STATUS
import com.google.gson.JsonElement
import retrofit2.Response

object TodoData {
    object API {
        // DB에 todo를 추가한다.
        // 동시에 todo-log도 추가된다.
        fun create(
            writer: String,
            folder: String,
            content: String,
            dates: List<String>,
            days: List<Boolean>,
            okayCallback: (JsonElement) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {}
        ) {
            TodoRetrofitManager.instance.addTodo(
                writer,
                folder,
                content,
                dates,
                days,
                completion = { responseStatus, response ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> response?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                })
        }

        // 사용자의 모든 todo를 반환한다.
        // completed 값에 따라 완료여부 값들을 필터링한다.
        fun getTodos(
            writer: String,
            completed: Boolean,
            okayCallback: (HashMap<Number, Pair<Todo, ArrayList<TodoLog>>>) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {}
        ) {
            // 만약 유저가 유저의 TodoData를 가지고 있지 않으면 불러온다.
            TodoRetrofitManager.instance.getTodos(
                writer,
                completed,
                completion = { responseStatus, response ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> response?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                })
        }

        fun getAllTodos(
            writer: String,
            okayCallback: (HashMap<String, Pair<ArrayList<Todo>, ArrayList<ArrayList<TodoLog>>>>) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {},
        ) {
            TodoRetrofitManager.instance.getAllTodos(
                writer,
                completion = { responseStatus, hashMap ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> hashMap?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                }
            )
        }

        // 사용자가 가지고 있는 todo를 folder로 구분하여 반환한다.
        fun getAllTodosByFolder(
            writer: String,
            completed: Boolean,
            okayCallback: (HashMap<String, Pair<ArrayList<Todo>, ArrayList<ArrayList<TodoLog>>>>) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {},
        ) {
            TodoRetrofitManager.instance.getAllTodosByFolder(
                writer,
                completed,
                completion = { responseStatus, hashMap ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> hashMap?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                }
            )
        }

        // 사용자가 작성한 todo 중 folder가 일치하는 todo를 반환한다.
        // completed 값에 따라 완료여부를 필터링한다.
        fun getTodosByFolder(
            writer: String,
            folder: String,
            completed: Boolean,
            okayCallback: (Pair<ArrayList<Todo>, ArrayList<ArrayList<TodoLog>>>) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {},
        ) {
            TodoRetrofitManager.instance.getTodosByFolder(
                writer,
                folder,
                completed,
                completion = { responseStatus, todos ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> todos?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                }
            )
        }

        // 사용자가 작성한 모든 todo를 date로 구분하여 반환한다.
        // completed 값에 따라 완료여부를 필터링한다.
        fun getAllTodosByDate(
            writer: String,
            completed: Boolean,
            okayCallback: (HashMap<String, Pair<ArrayList<Todo>, ArrayList<TodoLog>>>) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {}
        ) {
            TodoRetrofitManager.instance.getAllTodosByDate(
                writer,
                completed,
                completion = { responseStatus, todosByDates ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> todosByDates?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                }
            )
        }

        // 사용자가 작성한 todo 중 date가 일치하는 모든 todo를 반환한다.
        // completed 값에 따라 완료여부를 필터링한다.
        fun getTodosByDateInDates(
            writer: String,
            dates: List<String>,
            completed: Boolean,
            okayCallback: (HashMap<String, Pair<ArrayList<Todo>, ArrayList<TodoLog>>>) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {},
        ) {
            TodoRetrofitManager.instance.getTodosByDateInDates(
                writer, dates, completed, completion = { responseStatus, todosByDates ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> todosByDates?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                }
            )
        }

        // 사용자가 작성한 todo 데이터의 폴더 명과 데이터 수를 반환한다.
        fun getFoldersAndCount(
            writer: String,
            okayCallback: (ArrayList<Pair<String, Int>>) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {},
        ) {
            TodoRetrofitManager.instance.getFoldersAndCount(
                writer,
                completion = { responseStatus, pairs ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> pairs?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                }
            )
        }

        // 사용자가 작성한 todo 중 date가 일치하는 모든 todo를 반환한다.
        // completed 값에 따라 완료여부를 필터링한다.
        fun getTodosByDate(
            writer: String,
            date: String,
            completed: Boolean,
            okayCallback: (Pair<ArrayList<Todo>, ArrayList<TodoLog>>) -> Unit = {},
            failCallback: () -> Unit = {},
            noContentCallback: () -> Unit = {},
        ) {
            TodoRetrofitManager.instance.getTodosByDate(
                writer, date, completed,
                completion = { responseStatus, todos ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> todos?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> failCallback()
                        RESPONSE_STATUS.NO_CONTENT -> noContentCallback()
                    }
                }
            )
        }

        // DB에서 일치하는 todo를 업데이트한다.
        fun update(
            id: Number,
            folder: String,
            content: String,
            dates: List<String>,
            days: List<Boolean>,
            okayCallback: (response: JsonElement) -> Unit = {},
            failCallback: (response: JsonElement) -> Unit = {},
            noContentCallback: (response: JsonElement) -> Unit = {},
        ) {
            TodoRetrofitManager.instance.updateTodo(id,
                folder,
                content,
                dates,
                days,
                completion = { responseStatus, response ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> response?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> response?.let(failCallback)
                        RESPONSE_STATUS.NO_CONTENT -> response?.let(noContentCallback)
                    }
                })
        }

        // todo-logs DB에 접근하여 입력으로 받은 completed 값으로 바꾸어준다.
        fun checkTodo(
            todoId: Number,
            date: String,
            completed: Boolean,
            okayCallback: (response: JsonElement) -> Unit = {},
            failCallback: (response: JsonElement) -> Unit = {},
            noContentCallback: (response: JsonElement) -> Unit = {},
        ) {
            TodoRetrofitManager.instance.checkTodo(
                todoId, date, completed, completion = { responseStatus, response ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> response?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> response?.let(failCallback)
                        RESPONSE_STATUS.NO_CONTENT -> response?.let(noContentCallback)
                    }
                }
            )
        }

        // todo-logs DB에 접근하여 입력으로 받은 completed 값으로 바꾸어준다.
        fun delete(
            id: Number,
            okayCallback: (response: JsonElement) -> Unit = {},
            failCallback: (response: JsonElement) -> Unit = {},
            noContentCallback: (response: JsonElement) -> Unit = {},
        ) {
            TodoRetrofitManager.instance.deleteTodo(
                id,
                completion = { responseStatus, response ->
                    when (responseStatus) {
                        RESPONSE_STATUS.OKAY -> response?.let(okayCallback)
                        RESPONSE_STATUS.FAIL -> response?.let(failCallback)
                        RESPONSE_STATUS.NO_CONTENT -> response?.let(noContentCallback)
                    }
                })
        }
    }
}