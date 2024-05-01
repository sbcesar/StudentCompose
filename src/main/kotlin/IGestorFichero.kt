import java.io.File

interface IGestorFichero {
    fun loadStudentListFromFile(fileName: String): List<String>?
    fun saveStudentListToFile(fileName: String, studentList: List<String>)

}