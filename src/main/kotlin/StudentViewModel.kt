import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class StudentViewModel(
    private val gestorFichero: IGestorFichero,
    private val nombreFichero: File
): IStudentViewModel {

    private var _newStudent = mutableStateOf("")
    override val newStudent: State<String> = _newStudent

    //Valores por defecto
    private var _studentList = mutableStateListOf("Pepe","Pedro","Pepa","Paca")
    override val studentList: List<String> = _studentList

    private var _infoMessage = mutableStateOf("")
    override val infoMessage: State<String> = _infoMessage

    private var _showInfoMessage = mutableStateOf(false)
    override val showInfoMessage: State<Boolean> = _showInfoMessage


    override fun newStudentChange(name: String) { _newStudent.value = name }

    override fun addStudent() {
        _studentList.add(_newStudent.value)
        _newStudent.value = ""
    }

    override fun loadStudents() {
        val lista = gestorFichero.loadStudentListFromFile(nombreFichero)
        if (lista != null) {
            _studentList.clear()    //Posible error
            lista.forEach { name -> _studentList.add(name) }
        }
    }

    override fun deleteStudent(index: Int) {
        _studentList.removeAt(index)
    }

    override fun saveStudents(nombreFichero: String, studentList: List<String>) {
        gestorFichero.saveStudentListToFile(nombreFichero,studentList)
    }

    override fun clearStudent() {
        _studentList.clear()
    }

    override fun updateInfoMessage(message: String) {
        _infoMessage.value = message
        _showInfoMessage.value = true
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            _showInfoMessage.value = false
            _infoMessage.value = ""
        }
    }

    override fun showInfoMessage(state: Boolean) {
        _showInfoMessage.value = state
    }
}