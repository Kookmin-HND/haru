package com.example.harudemo.retrofit

import androidx.browser.browseractions.BrowserActionsIntent.BrowserActionsUrlType
import com.example.harudemo.todo.types.Todo
import com.example.harudemo.todo.types.TodoLog
import com.example.harudemo.utils.API
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

data class GetRequestBodyParams(
    @SerializedName("completed")
    val completed: Boolean,

    @SerializedName("dates")
    val dates: List<String>? = null
)

data class PostRequestBodyParams(
    @SerializedName("folder")
    val folder: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("dates")
    val dates: List<String>,

    @SerializedName("days")
    val days: List<Boolean>
)

data class PatchRequestBodyParams(
    @SerializedName("id")
    val id: Number,

    @SerializedName("folder")
    val folder: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("date")
    val dates: List<String>,

    @SerializedName("days")
    val days: List<Boolean>,
)

data class CheckRequestBodyParams(
    @SerializedName("todoId")
    val todoId: Number,

    @SerializedName("date")
    val date: String,

    @SerializedName("completed")
    val completed: Boolean,
)

data class DeleteRequestBodyParams(
    @SerializedName("id")
    val id: Number
)

interface TodoService {
    // 사용자로부터 folder, content, dates, days를 받아서 todo table에 데이터를 저장한다.
    @HTTP(method = "GET", path = "${API.TODOS}/{email}", hasBody = true)
    fun addTodos(
        @Path("email") writer: String,
        @Body requestBodyParams: PostRequestBodyParams
    ): Call<JsonElement>

    // 사용자의 모든 todo를 반환한다.
    // completed 값에 따라 완료여부 값들을 필터링한다.
    @HTTP(method = "GET", path = "${API.TODOS}/{email}", hasBody = true)
    fun getTodos(
        @Path("email") writer: String,
        @Body params: GetRequestBodyParams
    ): Call<ArrayList<Todo>>

    // 사용자의 모든 데이터를 가져오기 (todo-log) 포함.
    @GET("${API.TODOS}/{email}/all")
    fun getTodosAndTodoLogs(@Path("email") writer: String): Call<JsonObject>

    // 사용자로부터 todoId를 입력받아, 해당 todo의 todo-log를 반환한다.
    @GET("${API.TODOS}/log/{todoId}/{completed}")
    fun getLogs(
        @Path("todoId") todoId: Number,
        @Path("completed") completed: Boolean,
    ): Call<ArrayList<TodoLog>>

    // 사용자로부터 todoId를 입력받아, 해당 todo의 completed 상관없이 모두 반환한다.
    @GET("${API.TODOS}/log/{todoId}/all")
    fun getLogsAll(
        @Path("todoId") todoId: Number,
    ): Call<ArrayList<TodoLog>>

    // 사용자가 가지고 있는 todo를 folder로 구분하여 반환한다.
    // completed 값에 따라 완료여부를 필터링한다.
    @HTTP(method = "GET", path = "${API.TODOS}/{email}/folder", hasBody = true)
    fun getAllTodosByFolder(
        @Path("email") writer: String,
        @Body params: GetRequestBodyParams
    ): Call<HashMap<String, ArrayList<Todo>>>

    // 사용자가 작성한 todo 중 folder가 일치하는 todo를 반환한다.
    // completed 값에 따라 완료여부를 필터링한다.
    @HTTP(method = "GET", path = "${API.TODOS}/{email}/folder/{folder}", hasBody = true)
    fun getTodosByFolder(
        @Path("email") writer: String,
        @Path("folder") folder: String,
        @Body params: GetRequestBodyParams
    ): Call<ArrayList<Todo>>

    // 사용자가 작성한 todo 중 dates 내 date가 일치하는 모든 todo를 반환한다.
    // completed 값에 따라 완료여부를 필터링한다.
    @HTTP(method = "GET", path = "${API.TODOS}/{email}/date", hasBody = true)
    fun getTodosByDateInDates(
        @Path("email") writer: String,
        @Body params: GetRequestBodyParams
    ): Call<HashMap<String, ArrayList<Todo>>>

    // 사용자가 가지고 있는 todo를 받아온 dates로 구분하여 반환한다.
    // completed 값에 따라 완료여부를 필터링한다.
    @HTTP(method = "GET", path = "${API.TODOS}/{email}/date/{date}", hasBody = true)
    fun getTodosByDate(
        @Path("email") writer: String,
        @Path("date") date: String,
        @Body params: GetRequestBodyParams
    ): Call<ArrayList<Todo>>

    // 사용자로부터 입력받은 데이터(folder, content, dates, days)를 해당하는 todo를 id값을 기준으로 찾아 변경한다.
    // 그리고 todo-logs에 접근하여 해당 데이터를 삭제, 추가한다.
    @PATCH("${API.TODOS}/")
    fun updateTodo(@Body params: PatchRequestBodyParams): Call<JsonElement>

    // todo-logs DB에 접근하여 입력으로 받은 completed 값으로 바꾸어준다.
    @PATCH("${API.TODOS}/check")
    fun checkTodo(@Body params: CheckRequestBodyParams): Call<JsonElement>

    // 사용자로부터 todo id값을 입력받아 해당 데이터를 삭제한다.
    // 그리고 todo-logs에 접근하여 해당하는 log를 모두 삭제한다.
    @HTTP(method = "DELETE", path = API.TODOS, hasBody = true)
    fun deleteTodo(@Body params: DeleteRequestBodyParams): Call<JsonElement>
}