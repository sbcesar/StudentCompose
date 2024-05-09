import DB.StudentViewModelDB
import Files.FicheroTxt
import Files.StudentViewModelFile
import Interfaces.IStudentViewModel
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import kotlinx.coroutines.delay
import java.awt.Toolkit
import java.io.File

@Composable
fun MainWindow(
    title: String,
    icon: Painter,
    windowState: WindowState,
    resizable: Boolean,
    onCloseMainWindow: () -> Unit
    ) {
    Window(
        onCloseRequest = onCloseMainWindow,
        title = title,
        icon = icon,
        resizable = resizable,
        state = windowState
    ) {
        val gestorFichero = FicheroTxt()
        val studentRepository = StudentRepository()
        val nombreFichero = "Students.txt"
        val studentViewModelFile = StudentViewModelFile(gestorFichero, File(nombreFichero))
        val studentViewModelDB = StudentViewModelDB(studentRepository)

        MaterialTheme {
            Surface(
                color = Color.LightGray,
                modifier = Modifier.fillMaxSize()
            ) {
                //StudentScreen(studentViewModelFile)
                StudentScreen(studentViewModelDB)
            }
        }
    }
}


@Composable
@Preview
fun StudentScreen(
    viewModel: IStudentViewModel
) {
    val nombreFichero = "Students.txt"

    val studentName by viewModel.newStudent
    val studentList = viewModel.studentList
    val studentCount = viewModel.studentList.size

    val infoMessage by viewModel.infoMessage
    val showInfoMessage by viewModel.showInfoMessage

    val scrollBarVerticalState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(key1 = true) {
        viewModel.loadStudents()
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(7.dp,Alignment.CenterVertically)
            ) {

                OutlinedTextField(
                    value = studentName,
                    onValueChange =  { name -> viewModel.newStudentChange(name) },
                    label = { Text("Student Name") },
                    modifier = Modifier
                        .focusRequester(focusRequester),
                )

                Button(
                    onClick = {
                        viewModel.addStudent()
                    }
                ) {
                    Text("Add new student")
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(7.dp,Alignment.CenterVertically)
            ) {

                Text("Students: $studentCount")

                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .width(200.dp)
                        .border(2.dp, Color.Black)
                        .background(Color.White)
                        .padding(horizontal = 20.dp)
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 12.dp),
                        scrollBarVerticalState
                    ) {

                        items(studentList.size) { index ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = studentList[index],
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .weight(1f)
                                )

                                IconButton(
                                    onClick = { viewModel.deleteStudent(index) }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }

                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }

                    VerticalScrollbar(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(
                            scrollState = scrollBarVerticalState
                        )
                    )
                }

                Button(
                    onClick = {
                        viewModel.clearStudent()
                    }
                ) {
                    Text("Clear all")
                }
            }

        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            onClick = {
                viewModel.saveStudents(nombreFichero,studentList)

                viewModel.showInfoMessage(true)
            },
        ) {
            Text("Save Changes")
        }
    }

    // Gestión de la visibilidad del mensaje informativo
    if (showInfoMessage) {
        InfoMessage(
            message = infoMessage,
            onCloseInfoMessage = {
                viewModel.showInfoMessage(false)
                viewModel.updateInfoMessage("")
            }
        )
    }

    // Automáticamente oculta el mensaje después de un retraso
    LaunchedEffect(showInfoMessage) {
        if (showInfoMessage) {
            delay(2000)
            viewModel.showInfoMessage(false)
            viewModel.updateInfoMessage("")
        }
    }
}

@Composable
fun GetWindowState(
    windowWidth: Dp,
    windowHeight: Dp,
): WindowState {
    // Obtener las dimensiones de la pantalla
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenWidth = screenSize.width
    val screenHeight = screenSize.height

    // Calcular la posición para centrar la ventana
    val positionX = (screenWidth / 2 - windowWidth.value.toInt() / 2)
    val positionY = (screenHeight / 2 - windowHeight.value.toInt() / 2)

    return rememberWindowState(
        size = DpSize(windowWidth, windowHeight),
        position = WindowPosition(positionX.dp, positionY.dp)
    )
}

@Composable
fun InfoMessage(message: String, onCloseInfoMessage: () -> Unit) {
    Dialog(
        icon = painterResource("info_icon.png"),
        title = "Atencion",
        resizable = false,
        onCloseRequest = onCloseInfoMessage
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(message)
        }
    }
}



