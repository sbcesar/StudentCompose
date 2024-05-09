package Interfaces

import androidx.compose.runtime.*

interface IStudentViewModel {

    val newStudent: State<String>
    val studentList: List<String>

    val infoMessage: State<String>
    val showInfoMessage: State<Boolean>

    fun newStudentChange(name: String)
    fun addStudent()
    fun loadStudents()
    fun saveStudents(nombreFichero: String, studentList: List<String>)
    fun clearStudent()
    fun deleteStudent(index: Int)
    fun updateInfoMessage(message: String)
    fun showInfoMessage(state: Boolean)

}