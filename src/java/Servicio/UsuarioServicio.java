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
import Persistencia.DAOFactory;
import Persistencia.IUsuarioDAO;
import Util.PasswordUtil;
import Util.ValidacionUtil;

import java.util.List;

/**
 * Servicio de gestión de usuarios
 * Cubre RF01, RF04
 */
public class UsuarioServicio {
    
    private IUsuarioDAO usuarioDAO;
    
    public UsuarioServicio() {
        this.usuarioDAO = DAOFactory.getUsuarioDAO();
    }
    
    /**
     * Crea un nuevo usuario
     * RF01: Registro de usuarios
     */
    public Usuario crearUsuario(Usuario usuario) throws Exception {
        // Validaciones
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario requerido");
        }
        
        validarDatosUsuario(usuario);
        
        // Verificar duplicados
        if (usuarioDAO.existeUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El username ya está registrado");
        }
        
        if (usuarioDAO.existeEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // Hashear contraseña
        String passwordHasheado = PasswordUtil.hashPassword(usuario.getPassword());
        usuario.setPassword(passwordHasheado);
        
        // Establecer estado por defecto
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            // Organizaciones inician como Pendiente
            if ("Organizacion".equals(usuario.getRol())) {
                usuario.setEstado("Pendiente");
            } else {
                usuario.setEstado("Activo");
            }
        }
        
        // Crear usuario
        String idGenerado = usuarioDAO.crear(usuario);
        usuario.setId(idGenerado);
        
        System.out.println("✓ Usuario creado: " + usuario.getUsername() + " - Rol: " + usuario.getRol());
        
        return usuario;
    }
    
    /**
     * Lista todos los usuarios
     */
    public List<Usuario> listarTodos() throws Exception {
        return usuarioDAO.listarTodos();
    }
    
    /**
     * Busca un usuario por ID
     */
    public Usuario buscarPorId(String id) throws Exception {
        if (!ValidacionUtil.noVacio(id)) {
            throw new IllegalArgumentException("ID requerido");
        }
        
        Usuario usuario = usuarioDAO.buscarPorId(id);
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        return usuario;
    }
    
    /**
     * Busca un usuario por username
     */
    public Usuario buscarPorUsername(String username) throws Exception {
        if (!ValidacionUtil.noVacio(username)) {
            throw new IllegalArgumentException("Username requerido");
        }
        
        return usuarioDAO.buscarPorUsername(username);
    }
    
    /**
     * Busca un usuario por email
     */
    public Usuario buscarPorEmail(String email) throws Exception {
        if (!ValidacionUtil.validarEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        return usuarioDAO.buscarPorEmail(email);
    }
    
    /**
     * Actualiza un usuario existente
     */
    public void actualizar(Usuario usuario) throws Exception {
        if (usuario == null || usuario.getIdentificador() == null) {
            throw new IllegalArgumentException("Usuario y ID requeridos");
        }
        
        // Verificar que existe
        Usuario existente = usuarioDAO.buscarPorId(usuario.getIdentificador());
        if (existente == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        validarDatosUsuario(usuario);
        
        // Verificar duplicados (excepto el mismo usuario)
        Usuario usuarioPorUsername = usuarioDAO.buscarPorUsername(usuario.getUsername());
        if (usuarioPorUsername != null && 
            !usuarioPorUsername.getIdentificador().equals(usuario.getIdentificador())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }
        
        Usuario usuarioPorEmail = usuarioDAO.buscarPorEmail(usuario.getEmail());
        if (usuarioPorEmail != null && 
            !usuarioPorEmail.getIdentificador().equals(usuario.getIdentificador())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        
        // Si se está cambiando la contraseña, hashearla
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty() &&
            !usuario.getPassword().equals(existente.getPassword())) {
            String passwordHasheado = PasswordUtil.hashPassword(usuario.getPassword());
            usuario.setPassword(passwordHasheado);
        } else {
            // Mantener contraseña existente
            usuario.setPassword(existente.getPassword());
        }
        
        usuarioDAO.actualizar(usuario);
        
        System.out.println("✓ Usuario actualizado: " + usuario.getUsername());
    }
    
    /**
     * Cambia el estado de un usuario
     * RF04: Habilitar o rechazar usuarios/organizaciones
     */
    public void cambiarEstado(String id, String nuevoEstado) throws Exception {
        if (!ValidacionUtil.noVacio(id)) {
            throw new IllegalArgumentException("ID requerido");
        }
        
        if (!ValidacionUtil.validarEstado(nuevoEstado)) {
            throw new IllegalArgumentException("Estado inválido");
        }
        
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        usuarioDAO.cambiarEstado(id, nuevoEstado);
        
        System.out.println("✓ Estado cambiado a " + nuevoEstado + " para: " + usuario.getUsername());
    }
    
    /**
     * Elimina un usuario
     */
    public void eliminar(String id) throws Exception {
        if (!ValidacionUtil.noVacio(id)) {
            throw new IllegalArgumentException("ID requerido");
        }
        
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        usuarioDAO.eliminar(id);
        
        System.out.println("✓ Usuario eliminado: " + usuario.getUsername());
    }
    
    /**
     * Valida los datos básicos de un usuario
     */
    private void validarDatosUsuario(Usuario usuario) {
        if (!ValidacionUtil.validarUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Username inválido (3-20 caracteres alfanuméricos)");
        }
        
        if (!ValidacionUtil.validarEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        if (!ValidacionUtil.validarRol(usuario.getRol())) {
            throw new IllegalArgumentException("Rol inválido");
        }
        
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty() &&
            !PasswordUtil.validarFortaleza(usuario.getPassword())) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
    }
}
