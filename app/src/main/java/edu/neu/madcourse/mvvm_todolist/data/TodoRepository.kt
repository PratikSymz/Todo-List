package edu.neu.madcourse.mvvm_todolist.data

import kotlinx.coroutines.flow.Flow

/**
 * Decides which data source to pick that would be the best for providing to the ViewModel (from local cached, from
 * API or other sources)
 */
interface TodoRepository {
    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun getTodoById(id: Int): Todo?

    fun getTodos(): Flow<List<Todo>>
}