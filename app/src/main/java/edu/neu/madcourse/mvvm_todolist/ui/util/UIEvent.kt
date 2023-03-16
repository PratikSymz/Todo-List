package edu.neu.madcourse.mvvm_todolist.ui.util

/**
 * Sealed class for different kinds of UI Events sent from the ViewModel to the View
 */
sealed class UIEvent {
    object PopBackstack : UIEvent()
    data class Navigate(val route: String) : UIEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ) : UIEvent()
}
