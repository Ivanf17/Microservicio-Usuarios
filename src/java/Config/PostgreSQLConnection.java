/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

/**
 *
 * @author flori
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection {
    private static PostgreSQLConnection instance;
    private Connection connection;
    private DatabaseConfig config;
    
    private PostgreSQLConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            config = DatabaseConfig.getInstance();
            this.connection = DriverManager.getConnection(
                config.getPostgresUrl(),
                config.getPostgresUser(),
                config.getPostgresPassword()
            );
            System.out.println("✓ Conexión PostgreSQL establecida");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL no encontrado", e);
        }
    }
    
    public static synchronized PostgreSQLConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new PostgreSQLConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión PostgreSQL cerrada");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al cerrar conexión: " + e.getMessage());
        }
    }
    
    public static Connection getNewConnection() throws SQLException {
        DatabaseConfig config = DatabaseConfig.getInstance();
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(
                config.getPostgresUrl(),
                config.getPostgresUser(),
                config.getPostgresPassword()
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL no encontrado", e);
        }
    }
}
