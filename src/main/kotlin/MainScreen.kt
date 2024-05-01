import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun MainScreen() {
    var studentList by remember { mutableStateOf(emptyList<String>()) }
    var studentName by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp,Alignment.CenterVertically)
    ) {
        OutlinedTextField(
            value = studentName,
            onValueChange =  { studentName = it },
            label = { Text("Student Name") }
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                studentList = studentList + studentName
                studentName = ""
            }
        ) {
            Text("Add new student")
        }

        studentList.forEach { student ->
            Text(text = student)
        }
    }
}