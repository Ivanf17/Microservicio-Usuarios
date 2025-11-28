/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

/**
 *
 * @author flori
 */

import Modelo.Usuario;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO para Usuario
 * Define las operaciones CRUD y métodos específicos
 */
public interface IUsuarioDAO {
    
    /**
     * Crea un nuevo usuario
     * @param usuario Usuario a crear
     * @return ID generado (String para compatibilidad con MongoDB)
     */
    String crear(Usuario usuario) throws Exception;
    
    /**
     * Busca un usuario por su identificador
     * @param id ID del usuario (puede ser Integer para PostgreSQL o String para MongoDB)
     * @return Usuario encontrado o null
     */
    Usuario buscarPorId(String id) throws Exception;
    
    /**
     * Busca un usuario por username
     * @param username Username del usuario
     * @return Usuario encontrado o null
     */
    Usuario buscarPorUsername(String username) throws Exception;
    
    /**
     * Busca un usuario por email
     * @param email Email del usuario
     * @return Usuario encontrado o null
     */
    Usuario buscarPorEmail(String email) throws Exception;
    
    /**
     * Busca un usuario por token de recuperación
     * @param token Token de recuperación
     * @return Usuario encontrado o null
     */
    Usuario buscarPorToken(String token) throws Exception;
    
    /**
     * Lista todos los usuarios
     * @return Lista de usuarios
     */
    List<Usuario> listarTodos() throws Exception;
    
    /**
     * Lista usuarios por rol
     * @param rol Rol a filtrar
     * @return Lista de usuarios con ese rol
     */
    List<Usuario> listarPorRol(String rol) throws Exception;
    
    /**
     * Lista usuarios por estado
     * @param estado Estado a filtrar
     * @return Lista de usuarios con ese estado
     */
    List<Usuario> listarPorEstado(String estado) throws Exception;
    
    /**
     * Actualiza un usuario existente
     * @param usuario Usuario con los nuevos datos
     */
    void actualizar(Usuario usuario) throws Exception;
    
    /**
     * Actualiza la contraseña de un usuario
     * @param id ID del usuario
     * @param nuevaPassword Nueva contraseña hasheada
     */
    void actualizarPassword(String id, String nuevaPassword) throws Exception;
    
    /**
     * Actualiza la última conexión del usuario
     * @param id ID del usuario
     */
    void actualizarUltimaConexion(String id) throws Exception;
    
    /**
     * Cambia el estado de un usuario
     * @param id ID del usuario
     * @param nuevoEstado Nuevo estado (Activo, Inactivo, Pendiente)
     */
    void cambiarEstado(String id, String nuevoEstado) throws Exception;
    
    /**
     * Guarda un token de recuperación de contraseña
     * @param id ID del usuario
     * @param token Token generado
     * @param expiracion Fecha de expiración del token
     */
    void guardarTokenRecuperacion(String id, String token, LocalDateTime expiracion) throws Exception;
    
    /**
     * Limpia el token de recuperación después de usarlo
     * @param id ID del usuario
     */
    void limpiarToken(String id) throws Exception;
    
    /**
     * Elimina un usuario
     * @param id ID del usuario
     */
    void eliminar(String id) throws Exception;
    
    /**
     * Verifica si existe un usuario con ese username
     * @param username Username a verificar
     * @return true si existe
     */
    boolean existeUsername(String username) throws Exception;
    
    /**
     * Verifica si existe un usuario con ese email
     * @param email Email a verificar
     * @return true si existe
     */
    boolean existeEmail(String email) throws Exception;
    
    /**
     * Cuenta el total de usuarios
     * @return Número de usuarios
     */
    int contar() throws Exception;
    
    /**
     * Cuenta usuarios por rol
     * @param rol Rol a contar
     * @return Número de usuarios con ese rol
     */
    int contarPorRol(String rol) throws Exception;
}