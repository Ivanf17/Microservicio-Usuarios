/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

/**
 *
 * @author flori
 */

import Modelo.Usuario;
import Modelo.LoginDTO;
import Persistencia.DAOFactory;
import Persistencia.IUsuarioDAO;
import Util.PasswordUtil;
import Util.ValidacionUtil;

import java.time.LocalDateTime;

/**
 * Servicio de autenticación y autorización
 */
public class AuthServicio {
    
    private IUsuarioDAO usuarioDAO;
    
    public AuthServicio() {
        this.usuarioDAO = DAOFactory.getUsuarioDAO();
    }
    
    /**
     * Inicia sesión de un usuario
     * RF02: Autenticación mediante correo y contraseña
     */
    public Usuario iniciarSesion(LoginDTO loginDTO) throws Exception {
        // Validaciones
        if (loginDTO == null) {
            throw new IllegalArgumentException("Datos de login requeridos");
        }
        
        if (!ValidacionUtil.noVacio(loginDTO.getUsername())) {
            throw new IllegalArgumentException("El username es obligatorio");
        }
        
        if (!ValidacionUtil.noVacio(loginDTO.getPassword())) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        // Buscar usuario por username
        Usuario usuario = usuarioDAO.buscarPorUsername(loginDTO.getUsername());
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        // Verificar contraseña
        if (!PasswordUtil.checkPassword(loginDTO.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }
        
        // AGREGAR ESTA LÍNEA TEMPORAL:
        
        // Verificar estado del usuario
        if ("Inactivo".equals(usuario.getEstado())) {
            throw new IllegalArgumentException("Usuario inactivo. Contacte al administrador.");
        }
        
        if ("Pendiente".equals(usuario.getEstado()) && "Organizacion".equals(usuario.getRol())) {
            throw new IllegalArgumentException("Organización pendiente de validación.");
        }
        
        // Actualizar última conexión
        usuario.setUltimaConexion(LocalDateTime.now());
        usuarioDAO.actualizarUltimaConexion(usuario.getIdentificador());
        
        System.out.println("✓ Login exitoso: " + usuario.getUsername() + " - Rol: " + usuario.getRol());
        
        return usuario;
    }
    
    /**
     * Cierra sesión (opcional, para logs)
     */
    public void cerrarSesion(String identificadorUsuario) {
        System.out.println("✓ Sesión cerrada para usuario: " + identificadorUsuario);
    }
    
    /**
     * Solicita recuperación de contraseña
     * RF03: Recuperación de contraseña por email
     */
    public String solicitarRecuperacionPassword(String email) throws Exception {
        if (!ValidacionUtil.validarEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        
        if (usuario == null) {
            throw new IllegalArgumentException("No existe usuario con ese email");
        }
        
        // Generar token
        String token = PasswordUtil.generarToken();
        LocalDateTime expiracion = LocalDateTime.now().plusHours(1); // Token válido por 1 hora
        
        // Guardar token en BD
        usuarioDAO.guardarTokenRecuperacion(usuario.getIdentificador(), token, expiracion);
        
        System.out.println("✓ Token generado para: " + email);
        
        // TODO: Enviar email con token (integrar con servicio de email)
        
        return token;
    }
    
    /**
     * Restablece la contraseña usando un token
     */
    public void restablecerPassword(String token, String nuevaPassword) throws Exception {
        if (!ValidacionUtil.noVacio(token)) {
            throw new IllegalArgumentException("Token requerido");
        }
        
        if (!PasswordUtil.validarFortaleza(nuevaPassword)) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        
        Usuario usuario = usuarioDAO.buscarPorToken(token);
        
        if (usuario == null) {
            throw new IllegalArgumentException("Token inválido");
        }
        
        // Verificar expiración
        if (usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado");
        }
        
        // Hashear nueva contraseña
        String passwordHasheado = PasswordUtil.hashPassword(nuevaPassword);
        
        // Actualizar contraseña
        usuarioDAO.actualizarPassword(usuario.getIdentificador(), passwordHasheado);
        
        // Limpiar token
        usuarioDAO.limpiarToken(usuario.getIdentificador());
        
        System.out.println("✓ Contraseña restablecida para: " + usuario.getUsername());
    }
    
    /**
     * Cambia la contraseña de un usuario autenticado
     */
    public void cambiarPassword(String identificadorUsuario, String passwordActual, String nuevaPassword) throws Exception {
        Usuario usuario = usuarioDAO.buscarPorId(identificadorUsuario);
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        // Verificar contraseña actual
        if (!PasswordUtil.checkPassword(passwordActual, usuario.getPassword())) {
            throw new IllegalArgumentException("Contraseña actual incorrecta");
        }
        
        // Validar nueva contraseña
        if (!PasswordUtil.validarFortaleza(nuevaPassword)) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 6 caracteres");
        }
        
        // Hashear y actualizar
        String passwordHasheado = PasswordUtil.hashPassword(nuevaPassword);
        usuarioDAO.actualizarPassword(identificadorUsuario, passwordHasheado);
        
        System.out.println("✓ Contraseña cambiada para: " + usuario.getUsername());
    }
}