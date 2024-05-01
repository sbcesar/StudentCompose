import java.io.File

class FicheroTxt : IGestorFichero {

    override fun loadStudentListFromFile(fileName: String): List<String>? {
        val file = File(fileName)
        return if (file.exists()) {
            file.readLines()
        } else {
            null
        }
    }

    override fun saveStudentListToFile(fileName: String, studentList: List<String>) {
        val file = File(fileName)
        if (file.exists()) {
            file.writeText(studentList.joinToString(separator = "\n"))
        } else {
            file.createNewFile()
            file.writeText(studentList.joinToString(separator = "\n"))
        }
    }


}
