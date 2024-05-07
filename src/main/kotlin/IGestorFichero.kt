import java.io.File

interface IGestorFichero {

    fun loadStudentListFromFile(file: File): List<String>?
    fun saveStudentListToFile(fileName: String, studentList: List<String>)

}