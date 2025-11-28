/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

/**
 *
 * @author flori
 */

import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad para manejo seguro de contraseñas
 */
public class PasswordUtil {
    
    private static final int BCRYPT_ROUNDS = 12;
    
    /**
     * Hashea una contraseña usando BCrypt
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }
    
    /**
     * Verifica si una contraseña coincide con el hash
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Genera un token aleatorio para recuperación de contraseña
     */
    public static String generarToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    /**
     * Valida que la contraseña cumpla con requisitos mínimos
     */
    public static boolean validarFortaleza(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        // Puedes agregar más validaciones (mayúsculas, números, etc.)
        return true;
    }
}
