/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

/**
 *
 * @author flori
 */
/**
 * Configuración de base de datos
 * Patrón Singleton para configuración centralizada
 */
public class DatabaseConfig {
    private static DatabaseConfig instance;
    private String tipoBaseDatos; // "POSTGRESQL" o "MONGODB"
    
    // PostgreSQL Config
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String POSTGRES_USER = "postgres";
    private static final String POSTGRES_PASSWORD = "123456";
    
    // MongoDB Config
    private static final String MONGO_HOST = "localhost";
    private static final int MONGO_PORT = 27017;
    private static final String MONGO_DATABASE = "SGVC";
    
    private DatabaseConfig() {
        // Por defecto usamos PostgreSQL
        this.tipoBaseDatos = "MONGODB";
        // Puedes cambiar a MongoDB: this.tipoBaseDatos = "MONGODB";
    }
    
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
    
    public String getTipoBaseDatos() {
        return tipoBaseDatos;
    }
    
    public void setTipoBaseDatos(String tipo) {
        if (tipo.equals("POSTGRESQL") || tipo.equals("MONGODB")) {
            this.tipoBaseDatos = tipo;
            System.out.println("✓ Base de datos cambiada a: " + tipo);
        } else {
            throw new IllegalArgumentException("Tipo de BD inválido: " + tipo);
        }
    }
    
    // Getters PostgreSQL
    public String getPostgresUrl() {
        return POSTGRES_URL;
    }
    
    public String getPostgresUser() {
        return POSTGRES_USER;
    }
    
    public String getPostgresPassword() {
        return POSTGRES_PASSWORD;
    }
    
    // Getters MongoDB
    public String getMongoHost() {
        return MONGO_HOST;
    }
    
    public int getMongoPort() {
        return MONGO_PORT;
    }
    
    public String getMongoDatabase() {
        return MONGO_DATABASE;
    }
    
    /**
     * Método para cambiar la BD desde código o servlet
     */
    public void cambiarBaseDatos(String nuevaBD) {
        setTipoBaseDatos(nuevaBD);
    }
}
