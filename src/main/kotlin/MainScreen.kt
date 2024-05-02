import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import java.io.File


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

@Composable
@Preview
fun MainScreen(
    fichero: IGestorFichero
) {
    val nombreFichero = "Students.txt"

    var studentName by remember { mutableStateOf("") }
    var studentList by remember { mutableStateOf(listOf("Pepe","Pedro","Pepa","Paca")) }
    val studentCount = studentList.count()

    val scrollBarVerticalState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }

    var infoMessage by remember { mutableStateOf("") }
    var showInfoMessage by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {
        fichero.loadStudentListFromFile(nombreFichero)?.let {
            studentList = it
        }
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
                    onValueChange =  { studentName = it },
                    label = { Text("Student Name") },
                    modifier = Modifier
                        .focusRequester(focusRequester),
                )

                Button(
                    onClick = {
                        studentList = studentList + studentName
                        studentName = ""
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
                Text(
                text = "Students: $studentCount"
            )
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
                        items(studentList) { nombre ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = nombre,
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .weight(1f)
                                )
                                IconButton(
                                    onClick = { studentList = studentList.filter { it != nombre } }
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
                        studentList = emptyList()
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
                fichero.saveStudentListToFile(nombreFichero, studentList)
                infoMessage = "Fichero Guardado"
                showInfoMessage = true
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
                showInfoMessage = false
                infoMessage = ""
            }
        )
    }

    // Automáticamente oculta el mensaje después de un retraso
    LaunchedEffect(showInfoMessage) {
        if (showInfoMessage) {
            delay(2000)
            showInfoMessage = false
            infoMessage = ""
        }
    }
}



