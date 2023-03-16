package edu.neu.madcourse.mvvm_todolist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


/**
 * DAO: Data Access Object, that defines the different ways we want to access our data
 */
@Dao
interface TodoDao {

    /**
     * suspend:
     * Halt the current function it is executed in until we know that the execution is finished
     * No need for callbacks
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)    // Replace old data with same id with new
    suspend fun insertTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo WHERE _id = :id")
    suspend fun getTodoById(id: Int): Todo?

    @Query("SELECT * FROM todo")
    fun getTodos(): Flow<List<Todo>>    // Receive live updates from db (Multiple values)
}