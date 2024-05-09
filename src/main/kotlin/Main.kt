import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application

class DatabaseTimeoutException(message: String) : Exception(message)

class SqlErrorException(message: String) : Exception(message)

fun main() = application {

    val icon = painterResource("sample.png")
    val windowState = GetWindowState(
        windowWidth = 800.dp,
        windowHeight = 800.dp
    )

    MainWindow(
        title = "My Students",
        icon = icon,
        windowState = windowState,
        resizable = false,
        onCloseMainWindow = { exitApplication() }
    )
}
