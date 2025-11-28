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
import Modelo.Organizacion;
import Persistencia.DAOFactory;
import Persistencia.IOrganizacionDAO;
import Util.ValidacionUtil;

import java.util.List;

/**
 * Servicio de gestión de organizaciones
 * Cubre RF01: Registro de organizaciones
 * Cubre RF04: Validación de organizaciones
 */
public class OrganizacionServicio {
    
    private IOrganizacionDAO organizacionDAO;
    private UsuarioServicio usuarioServicio;
    
    public OrganizacionServicio() {
        this.organizacionDAO = DAOFactory.getOrganizacionDAO();
        this.usuarioServicio = new UsuarioServicio();
    }
    
    /**
     * Registra una nueva organización
     * RF01: Registro con datos institucionales y credenciales
     */
    public Organizacion registrarOrganizacion(Organizacion organizacion) throws Exception {
        // Validaciones
        if (organizacion == null) {
            throw new IllegalArgumentException("Datos de la organización requeridos");
        }
        
        validarDatosOrganizacion(organizacion);
        
        // Verificar NIT duplicado
        if (organizacionDAO.existeNIT(organizacion.getNit())) {
            throw new IllegalArgumentException("El NIT ya está registrado");
        }
        
        // Crear usuario primero (estado Pendiente hasta validación del admin)
        Usuario usuario = organizacion.getUsuario();
        usuario.setRol("Organizacion");
        usuario.setEstado("Pendiente");
        
        Usuario usuarioCreado = usuarioServicio.crearUsuario(usuario);
        
        // Asociar ID generado a la organización
        organizacion.setIdUsuario(usuarioCreado.getIdUsuario());
        organizacion.setId(usuarioCreado.getId());
        organizacion.setEstadoValidacion("Pendiente");
        
        // Crear organización
        String idGenerado = organizacionDAO.crear(organizacion);
        organizacion.setId(idGenerado);
        
        System.out.println("✓ Organización registrada (Pendiente validación): " + organizacion.getNombre());
        
        return organizacion;
    }
    
    /**
     * Lista todas las organizaciones
     */
    public List<Organizacion> listarTodas() throws Exception {
        return organizacionDAO.listarTodas();
    }
    
    /**
     * Lista organizaciones pendientes de validación
     * RF04: Para que el administrador valide organizaciones
     */
    public List<Organizacion> listarPendientes() throws Exception {
        return organizacionDAO.listarPorEstadoValidacion("Pendiente");
    }
    
    /**
     * Lista organizaciones aprobadas
     */
    public List<Organizacion> listarAprobadas() throws Exception {
        return organizacionDAO.listarPorEstadoValidacion("Aprobado");
    }
    
    /**
     * Busca una organización por ID de usuario
     */
    public Organizacion buscarPorIdUsuario(String idUsuario) throws Exception {
        if (!ValidacionUtil.noVacio(idUsuario)) {
            throw new IllegalArgumentException("ID de usuario requerido");
        }
        
        Organizacion organizacion = organizacionDAO.buscarPorIdUsuario(idUsuario);
        
        if (organizacion == null) {
            throw new IllegalArgumentException("Organización no encontrada");
        }
        
        return organizacion;
    }
    
    /**
     * Busca una organización por NIT
     */
    public Organizacion buscarPorNIT(String nit) throws Exception {
        if (!ValidacionUtil.validarNIT(nit)) {
            throw new IllegalArgumentException("NIT inválido");
        }
        
        return organizacionDAO.buscarPorNIT(nit);
    }
    
    /**
     * Actualiza los datos de una organización
     */
    public void actualizar(Organizacion organizacion) throws Exception {
        if (organizacion == null || organizacion.getIdentificador() == null) {
            throw new IllegalArgumentException("Organización e ID requeridos");
        }
        
        // Verificar que existe
        Organizacion existente = organizacionDAO.buscarPorIdUsuario(organizacion.getIdentificador());
        if (existente == null) {
            throw new IllegalArgumentException("Organización no encontrada");
        }
        
        validarDatosOrganizacion(organizacion);
        
        // Verificar NIT duplicado (excepto la misma organización)
        Organizacion orgPorNIT = organizacionDAO.buscarPorNIT(organizacion.getNit());
        if (orgPorNIT != null && 
            !orgPorNIT.getIdentificador().equals(organizacion.getIdentificador())) {
            throw new IllegalArgumentException("El NIT ya está en uso por otra organización");
        }
        
        organizacionDAO.actualizar(organizacion);
        
        System.out.println("✓ Organización actualizada: " + organizacion.getNombre());
    }
    
    /**
     * Valida una organización (la aprueba o rechaza)
     * RF04: El administrador valida las organizaciones registradas
     */
    public void validarOrganizacion(String idUsuario, boolean aprobada, String motivoRechazo) throws Exception {
        if (!ValidacionUtil.noVacio(idUsuario)) {
            throw new IllegalArgumentException("ID de usuario requerido");
        }
        
        Organizacion organizacion = organizacionDAO.buscarPorIdUsuario(idUsuario);
        if (organizacion == null) {
            throw new IllegalArgumentException("Organización no encontrada");
        }
        
        String nuevoEstado = aprobada ? "Aprobado" : "Rechazado";
        
        // Actualizar estado de validación
        organizacionDAO.cambiarEstadoValidacion(idUsuario, nuevoEstado);
        
        // Actualizar estado del usuario asociado
        String estadoUsuario = aprobada ? "Activo" : "Inactivo";
        usuarioServicio.cambiarEstado(idUsuario, estadoUsuario);
        
        System.out.println("✓ Organización " + nuevoEstado.toLowerCase() + ": " + organizacion.getNombre());
        
        if (!aprobada && motivoRechazo != null) {
            System.out.println("  Motivo: " + motivoRechazo);
            // TODO: Enviar email con motivo de rechazo
        }
    }
    
    /**
     * Elimina una organización (y su usuario asociado)
     */
    public void eliminar(String idUsuario) throws Exception {
        if (!ValidacionUtil.noVacio(idUsuario)) {
            throw new IllegalArgumentException("ID de usuario requerido");
        }
        
        Organizacion organizacion = organizacionDAO.buscarPorIdUsuario(idUsuario);
        if (organizacion == null) {
            throw new IllegalArgumentException("Organización no encontrada");
        }
        
        // Eliminar organización
        organizacionDAO.eliminar(idUsuario);
        
        // Eliminar usuario asociado
        usuarioServicio.eliminar(idUsuario);
        
        System.out.println("✓ Organización eliminada: " + organizacion.getNombre());
    }
    
    /**
     * Cuenta el total de organizaciones
     */
    public int contarOrganizaciones() throws Exception {
        return organizacionDAO.contar();
    }
    
    /**
     * Cuenta organizaciones por estado de validación
     */
    public int contarPorEstado(String estadoValidacion) throws Exception {
        return organizacionDAO.contarPorEstadoValidacion(estadoValidacion);
    }
    
    /**
     * Valida los datos de una organización
     */
    private void validarDatosOrganizacion(Organizacion organizacion) {
        if (!ValidacionUtil.noVacio(organizacion.getNombre())) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        
        if (!ValidacionUtil.validarNIT(organizacion.getNit())) {
            throw new IllegalArgumentException("NIT inválido (9-15 dígitos)");
        }
        
        if (!ValidacionUtil.noVacio(organizacion.getRepresentante())) {
            throw new IllegalArgumentException("El representante legal es obligatorio");
        }
        
        if (organizacion.getTelefono() != null && !organizacion.getTelefono().isEmpty()) {
            if (!ValidacionUtil.validarTelefono(organizacion.getTelefono())) {
                throw new IllegalArgumentException("Formato de teléfono inválido (7-15 dígitos)");
            }
        }
        
        // Dirección y tipo son opcionales
    }
}