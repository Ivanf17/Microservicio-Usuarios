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
import Modelo.Voluntario;
import Persistencia.DAOFactory;
import Persistencia.IVoluntarioDAO;
import Util.ValidacionUtil;

import java.util.List;

/**
 * Servicio de gestión de voluntarios
 * Cubre RF01: Registro de voluntarios
 */
public class VoluntarioServicio {
    
    private IVoluntarioDAO voluntarioDAO;
    private UsuarioServicio usuarioServicio;
    
    public VoluntarioServicio() {
        this.voluntarioDAO = DAOFactory.getVoluntarioDAO();
        this.usuarioServicio = new UsuarioServicio();
    }
    
    /**
     * Registra un nuevo voluntario
     * RF01: Registro con datos personales, credenciales y áreas de interés
     */
    public Voluntario registrarVoluntario(Voluntario voluntario) throws Exception {
        // Validaciones
        if (voluntario == null) {
            throw new IllegalArgumentException("Datos del voluntario requeridos");
        }
        
        validarDatosVoluntario(voluntario);
        
        // Crear usuario primero
        Usuario usuario = voluntario.getUsuario();
        usuario.setRol("Voluntario");
        usuario.setEstado("Activo");
        
        Usuario usuarioCreado = usuarioServicio.crearUsuario(usuario);
        
        // Asociar ID generado al voluntario
        voluntario.setIdUsuario(usuarioCreado.getIdUsuario());
        voluntario.setId(usuarioCreado.getId());
        
        // Crear voluntario
        String idGenerado = voluntarioDAO.crear(voluntario);
        voluntario.setId(idGenerado);
        
        System.out.println("✓ Voluntario registrado: " + voluntario.getNombreCompleto());
        
        return voluntario;
    }
    
    /**
     * Lista todos los voluntarios
     */
    public List<Voluntario> listarTodos() throws Exception {
        return voluntarioDAO.listarTodos();
    }
    
    /**
     * Busca un voluntario por ID de usuario
     */
    public Voluntario buscarPorIdUsuario(String idUsuario) throws Exception {
        if (!ValidacionUtil.noVacio(idUsuario)) {
            throw new IllegalArgumentException("ID de usuario requerido");
        }
        
        Voluntario voluntario = voluntarioDAO.buscarPorIdUsuario(idUsuario);
        
        if (voluntario == null) {
            throw new IllegalArgumentException("Voluntario no encontrado");
        }
        
        return voluntario;
    }
    
    /**
     * Actualiza los datos de un voluntario
     */
    public void actualizar(Voluntario voluntario) throws Exception {
        if (voluntario == null || voluntario.getIdentificador() == null) {
            throw new IllegalArgumentException("Voluntario e ID requeridos");
        }
        
        // Verificar que existe
        Voluntario existente = voluntarioDAO.buscarPorIdUsuario(voluntario.getIdentificador());
        if (existente == null) {
            throw new IllegalArgumentException("Voluntario no encontrado");
        }
        
        validarDatosVoluntario(voluntario);
        
        voluntarioDAO.actualizar(voluntario);
        
        System.out.println("✓ Voluntario actualizado: " + voluntario.getNombreCompleto());
    }
    
    /**
     * Elimina un voluntario (y su usuario asociado)
     */
    public void eliminar(String idUsuario) throws Exception {
        if (!ValidacionUtil.noVacio(idUsuario)) {
            throw new IllegalArgumentException("ID de usuario requerido");
        }
        
        Voluntario voluntario = voluntarioDAO.buscarPorIdUsuario(idUsuario);
        if (voluntario == null) {
            throw new IllegalArgumentException("Voluntario no encontrado");
        }
        
        // Eliminar voluntario
        voluntarioDAO.eliminar(idUsuario);
        
        // Eliminar usuario asociado
        usuarioServicio.eliminar(idUsuario);
        
        System.out.println("✓ Voluntario eliminado: " + voluntario.getNombreCompleto());
    }
    
    /**
     * Cuenta el total de voluntarios registrados
     */
    public int contarVoluntarios() throws Exception {
        return voluntarioDAO.contar();
    }
    
    /**
     * Verifica si existe un voluntario con ese ID
     */
    public boolean existe(String idUsuario) throws Exception {
        return voluntarioDAO.buscarPorIdUsuario(idUsuario) != null;
    }
    
    /**
     * Valida los datos de un voluntario
     */
    private void validarDatosVoluntario(Voluntario voluntario) {
        if (!ValidacionUtil.noVacio(voluntario.getNombre())) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (!ValidacionUtil.noVacio(voluntario.getApellido())) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        
        if (voluntario.getTelefono() != null && !voluntario.getTelefono().isEmpty()) {
            if (!ValidacionUtil.validarTelefono(voluntario.getTelefono())) {
                throw new IllegalArgumentException("Formato de teléfono inválido (7-15 dígitos)");
            }
        }
        
        // La fecha de nacimiento es opcional
        // La dirección es opcional
    }
}
