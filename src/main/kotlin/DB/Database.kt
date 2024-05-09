package DB

import DatabaseTimeoutException
import SqlErrorException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.SQLTimeoutException

object Database {

    private const val DB_URL = "jdbc:mysql://localhost:3306/studentdb"
    private const val USER = "root"
    private const val PASS = "password"

    init {
        try {
            // Asegurarse de que el driver JDBC de MySQL esté disponible
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (e: ClassNotFoundException) {
            e.printStackTrace();
        }
    }

    // Función para establecer una conexión a la base de datos
    fun getConnection(): Connection =
        try {
            DriverManager.getConnection(DB_URL, USER, PASS)
        } catch (e: SQLTimeoutException) {
            throw DatabaseTimeoutException("La conexión ha excedido el tiempo de espera permitido.")
        } catch (e: SQLException) {
            throw SqlErrorException("Error de SQL: ${e.message}")
        }



    // Función para cerrar una conexión a la base de datos
    fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }
}