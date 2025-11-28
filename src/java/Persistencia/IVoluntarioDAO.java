/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

/**
 *
 * @author flori
 */

import Modelo.Voluntario;
import java.util.List;

/**
 * Interface DAO para Voluntario
 */
public interface IVoluntarioDAO {
    
    /**
     * Crea un nuevo voluntario
     * @param voluntario Voluntario a crear
     * @return ID generado
     */
    String crear(Voluntario voluntario) throws Exception;
    
    /**
     * Busca un voluntario por ID de usuario
     * @param idUsuario ID del usuario asociado
     * @return Voluntario encontrado o null
     */
    Voluntario buscarPorIdUsuario(String idUsuario) throws Exception;
    
    /**
     * Lista todos los voluntarios
     * @return Lista de voluntarios
     */
    List<Voluntario> listarTodos() throws Exception;
    
    /**
     * Busca voluntarios por nombre o apellido
     * @param termino Término de búsqueda
     * @return Lista de voluntarios que coinciden
     */
    List<Voluntario> buscarPorNombre(String termino) throws Exception;
    
    /**
     * Actualiza los datos de un voluntario
     * @param voluntario Voluntario con los nuevos datos
     */
    void actualizar(Voluntario voluntario) throws Exception;
    
    /**
     * Elimina un voluntario
     * @param idUsuario ID del usuario asociado
     */
    void eliminar(String idUsuario) throws Exception;
    
    /**
     * Verifica si existe un voluntario con ese ID de usuario
     * @param idUsuario ID del usuario
     * @return true si existe
     */
    boolean existe(String idUsuario) throws Exception;
    
    /**
     * Cuenta el total de voluntarios
     * @return Número de voluntarios
     */
    int contar() throws Exception;
}
