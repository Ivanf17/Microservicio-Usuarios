/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 *
 * @author flori
 */

import java.util.regex.Pattern;

/**
 * Utilidad para validaciones de datos
 */
public class ValidacionUtil {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    private static final Pattern TELEFONO_PATTERN = 
        Pattern.compile("^[0-9]{7,15}$");
    
    private static final Pattern NIT_PATTERN = 
        Pattern.compile("^[0-9]{9,15}$");
    
    /**
     * Valida formato de email
     */
    public static boolean validarEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Valida formato de username
     */
    public static boolean validarUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Valida formato de teléfono
     */
    public static boolean validarTelefono(String telefono) {
        return telefono != null && TELEFONO_PATTERN.matcher(telefono).matches();
    }
    
    /**
     * Valida formato de NIT
     */
    public static boolean validarNIT(String nit) {
        return nit != null && NIT_PATTERN.matcher(nit).matches();
    }
    
    /**
     * Valida que un string no sea nulo ni vacío
     */
    public static boolean noVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Valida rol de usuario
     */
    public static boolean validarRol(String rol) {
        return rol != null && 
               (rol.equals("Voluntario") || rol.equals("Organizacion") || rol.equals("Administrador"));
    }
    
    /**
     * Valida estado de usuario
     */
    public static boolean validarEstado(String estado) {
        return estado != null && 
               (estado.equals("Activo") || estado.equals("Inactivo") || estado.equals("Pendiente"));
    }
}