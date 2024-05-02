import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
@Preview
fun MainScreen(
    fichero: IGestorFichero
) {
    val nombreFichero = "Students.txt"
    var studentName by remember { mutableStateOf("") }
    var studentList by remember { mutableStateOf(listOf("Pepe","Pedro","Pepa","Paca")) }
    var studentCount = studentList.count()
    val scrollBarVerticalState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }


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
                    modifier = Modifier.focusRequester(focusRequester)
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
                            Text(
                                text = nombre,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(5.dp))
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
            }
        ) {
            Text("Save Changes")
        }
    }
}