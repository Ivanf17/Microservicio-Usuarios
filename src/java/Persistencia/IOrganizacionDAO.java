/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

/**
 *
 * @author flori
 */

import Modelo.Organizacion;
import java.util.List;

public interface IOrganizacionDAO {
    
    /**
     * Crea una nueva organización
     * @param organizacion Organización a crear
     * @return ID generado
     */
    String crear(Organizacion organizacion) throws Exception;
    
    /**
     * Busca una organización por ID de usuario
     * @param idUsuario ID del usuario asociado
     * @return Organización encontrada o null
     */
    Organizacion buscarPorIdUsuario(String idUsuario) throws Exception;
    
    /**
     * Busca una organización por NIT
     * @param nit NIT de la organización
     * @return Organización encontrada o null
     */
    Organizacion buscarPorNIT(String nit) throws Exception;
    
    /**
     * Lista todas las organizaciones
     * @return Lista de organizaciones
     */
    List<Organizacion> listarTodas() throws Exception;
    
    /**
     * Lista organizaciones por estado de validación
     * @param estadoValidacion Estado (Pendiente, Aprobado, Rechazado)
     * @return Lista de organizaciones con ese estado
     */
    List<Organizacion> listarPorEstadoValidacion(String estadoValidacion) throws Exception;
    
    /**
     * Busca organizaciones por nombre
     * @param termino Término de búsqueda
     * @return Lista de organizaciones que coinciden
     */
    List<Organizacion> buscarPorNombre(String termino) throws Exception;
    
    /**
     * Actualiza los datos de una organización
     * @param organizacion Organización con los nuevos datos
     */
    void actualizar(Organizacion organizacion) throws Exception;
    
    /**
     * Cambia el estado de validación de una organización
     * @param idUsuario ID del usuario asociado
     * @param nuevoEstado Nuevo estado (Pendiente, Aprobado, Rechazado)
     */
    void cambiarEstadoValidacion(String idUsuario, String nuevoEstado) throws Exception;
    
    /**
     * Elimina una organización
     * @param idUsuario ID del usuario asociado
     */
    void eliminar(String idUsuario) throws Exception;
    
    /**
     * Verifica si existe una organización con ese NIT
     * @param nit NIT a verificar
     * @return true si existe
     */
    boolean existeNIT(String nit) throws Exception;
    
    /**
     * Verifica si existe una organización con ese ID de usuario
     * @param idUsuario ID del usuario
     * @return true si existe
     */
    boolean existe(String idUsuario) throws Exception;
    
    /**
     * Cuenta el total de organizaciones
     * @return Número de organizaciones
     */
    int contar() throws Exception;
    
    /**
     * Cuenta organizaciones por estado de validación
     * @param estadoValidacion Estado a contar
     * @return Número de organizaciones con ese estado
     */
    int contarPorEstadoValidacion(String estadoValidacion) throws Exception;
}
