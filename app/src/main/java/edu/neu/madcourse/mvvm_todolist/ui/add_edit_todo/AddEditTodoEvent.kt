package edu.neu.madcourse.mvvm_todolist.ui.add_edit_todo

/**
 * Sealed class for different kinds of UI Events sent from the View back to the ViewModel
 */
sealed class AddEditTodoEvent {

    // User updates the title
    data class OnTitleChange(val title: String) : AddEditTodoEvent()

    // User updates the description
    data class OnDescriptionChange(val description: String) : AddEditTodoEvent()

    // User clicks save
    object OnSaveTodoClick : AddEditTodoEvent()
}
