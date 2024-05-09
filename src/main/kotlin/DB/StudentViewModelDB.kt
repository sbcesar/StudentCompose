package DB

import Interfaces.IStudentRepository
import Interfaces.IStudentViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StudentViewModelDB(
    private val studentRepository: IStudentRepository
): IStudentViewModel {

    private var _newStudent = mutableStateOf("")
    override val newStudent: State<String> = _newStudent


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
        val result = studentRepository.getAllStudents()
        result.onSuccess { students ->
            _studentList.clear()
            _studentList.addAll(students)
            updateInfoMessage("Registros cargados correctamente.")
        }.onFailure {
            exception -> updateInfoMessage(exception.message ?: "")
        }
    }

    override fun saveStudents(nombreFichero: String, studentList: List<String>) {
        val result = studentRepository.updateStudents(_studentList)
        result.onSuccess {
            updateInfoMessage("Registros guardados correctamente.")
        }.onFailure {
            exception -> updateInfoMessage(exception.message ?: "")
        }

    }

    override fun clearStudent() {
        _studentList.clear()
    }

    override fun deleteStudent(index: Int) {
        _studentList.removeAt(index)
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