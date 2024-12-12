import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import tests.QuizCreatorApp

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        QuizCreatorApp()
    }
}