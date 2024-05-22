import DB.Database
import Interfaces.IStudentRepository
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class StudentRepository: IStudentRepository {

    override fun getAllStudents(): Result<List<String>> {
        val connectionDb = Database.getConnection()
        val students = mutableListOf<String>()
        var resultSet: ResultSet? = null
        var stmt: Statement? = null
        return try {
            stmt = connectionDb.createStatement()
            resultSet = stmt.executeQuery("SELECT name FROM students")
            while (resultSet.next()) {
                students.add(resultSet.getString("name"))
            }
            Result.success(students)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            resultSet?.close()
            stmt?.close()
            Database.closeConnection(connectionDb)
        }
    }


    override fun updateStudents(students: List<String>): Result<Unit> {
        val connectionDb = Database.getConnection()
        var statement: Statement? = null
        var preparedStatement: PreparedStatement? = null

        return try {
            connectionDb.autoCommit = false
            statement = connectionDb.createStatement()
            statement.execute("DELETE FROM students")

            preparedStatement = connectionDb.prepareStatement("INSERT INTO students (name) VALUES (?)")
            students.forEach { student ->
                preparedStatement.setString(1,student)
                preparedStatement.executeUpdate()
            }

            connectionDb.commit()
            Result.success(Unit)

        } catch (e: Exception) {
            connectionDb.rollback()
            Result.failure(e)
        } finally {
            preparedStatement?.close()
            statement?.close()
            connectionDb.autoCommit = true
            Database.closeConnection(connectionDb)
        }
    }
}