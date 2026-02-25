package cl.speedfast.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Gestiona la conexión a la base de datos.
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db";
    private static final String USER = "user"; // Ajustar según config local
    private static final String PASSWORD = "pass"; // Ajustar según config local

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private ConexionDB() {}
    
}
