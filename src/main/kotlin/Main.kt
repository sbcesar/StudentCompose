import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val fichero = FicheroTxt()
    Window(onCloseRequest = ::exitApplication) {
        MainScreen(fichero)
    }
}
