import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GlobalState {
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun clearUserName() {
        _userName.value = null
    }
}