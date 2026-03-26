package com.example.taskmanagerapp

import retrofit2.http.GET

interface ApiService {
    @GET("todos")
    suspend fun getTasks(): List<Todo>
}

data class Todo(
    val title: String
)