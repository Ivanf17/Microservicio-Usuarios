/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

/**
 *
 * @author flori
 */

import Config.DatabaseConfig;

/**
 * Factory Pattern para crear instancias de DAOs
 * Permite cambiar entre PostgreSQL y MongoDB dinámicamente
 */
public class DAOFactory {
    
    /**
     * Obtiene la implementación de IUsuarioDAO según la BD configurada
     */
    public static IUsuarioDAO getUsuarioDAO() {
        DatabaseConfig config = DatabaseConfig.getInstance();
        String tipoBD = config.getTipoBaseDatos();
        
        switch (tipoBD) {
            case "POSTGRESQL":
                return new UsuarioPostgreSQLDAO();
            case "MONGODB":
                return new UsuarioMongoDAO();
            default:
                throw new IllegalStateException("Tipo de base de datos no soportado: " + tipoBD);
        }
    }
    
    /**
     * Obtiene la implementación de IVoluntarioDAO según la BD configurada
     */
    public static IVoluntarioDAO getVoluntarioDAO() {
        DatabaseConfig config = DatabaseConfig.getInstance();
        String tipoBD = config.getTipoBaseDatos();
        
        switch (tipoBD) {
            case "POSTGRESQL":
                return new VoluntarioPostgreSQLDAO();
            case "MONGODB":
                return new VoluntarioMongoDAO();
            default:
                throw new IllegalStateException("Tipo de base de datos no soportado: " + tipoBD);
        }
    }
    
    /**
     * Obtiene la implementación de IOrganizacionDAO según la BD configurada
     */
    public static IOrganizacionDAO getOrganizacionDAO() {
        DatabaseConfig config = DatabaseConfig.getInstance();
        String tipoBD = config.getTipoBaseDatos();
        
        switch (tipoBD) {
            case "POSTGRESQL":
                return new OrganizacionPostgreSQLDAO();
            case "MONGODB":
                return new OrganizacionMongoDAO();
            default:
                throw new IllegalStateException("Tipo de base de datos no soportado: " + tipoBD);
        }
    }
    
    /**
     * Cambia el tipo de base de datos en tiempo de ejecución
     * @param tipoBD "POSTGRESQL" o "MONGODB"
     */
    public static void cambiarBaseDatos(String tipoBD) {
        DatabaseConfig config = DatabaseConfig.getInstance();
        config.setTipoBaseDatos(tipoBD);
        System.out.println("✓ Base de datos cambiada a: " + tipoBD);
    }
    
    /**
     * Obtiene el tipo de BD actual
     */
    public static String getTipoBaseDatosActual() {
        return DatabaseConfig.getInstance().getTipoBaseDatos();
    }
}
