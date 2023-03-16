package edu.neu.madcourse.mvvm_todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The fields of our data (table)
 */
@Entity
data class Todo(
    @PrimaryKey val _id: Int? = null,
    val title: String,
    val description: String?,
    val isDone: Boolean,
)
