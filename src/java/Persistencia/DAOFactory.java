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

public class DAOFactory {
    
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

    public static void cambiarBaseDatos(String tipoBD) {
        DatabaseConfig config = DatabaseConfig.getInstance();
        config.setTipoBaseDatos(tipoBD);
        System.out.println("✓ Base de datos cambiada a: " + tipoBD);
    }

    public static String getTipoBaseDatosActual() {
        return DatabaseConfig.getInstance().getTipoBaseDatos();
    }
}
