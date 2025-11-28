/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

/**
 *
 * @author flori
 */

import Config.PostgreSQLConnection;
import Modelo.Organizacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrganizacionPostgreSQLDAO implements IOrganizacionDAO {
    
    @Override
    public String crear(Organizacion organizacion) throws Exception {
        String sql = "INSERT INTO organizacion (id_usuario, nombre, direccion, telefono, tipo, nit, representante, estado_validacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, organizacion.getIdUsuario());
            ps.setString(2, organizacion.getNombre());
            ps.setString(3, organizacion.getDireccion());
            ps.setString(4, organizacion.getTelefono());
            ps.setString(5, organizacion.getTipo());
            ps.setString(6, organizacion.getNit());
            ps.setString(7, organizacion.getRepresentante());
            ps.setString(8, organizacion.getEstadoValidacion());
            
            ps.executeUpdate();
            
            System.out.println("✓ [PostgreSQL] Organización creada: " + organizacion.getNombre());
            return String.valueOf(organizacion.getIdUsuario());
        }
    }
    
    @Override
    public Organizacion buscarPorIdUsuario(String idUsuario) throws Exception {
        String sql = "SELECT * FROM organizacion WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(idUsuario));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearOrganizacion(rs);
            }
            
            return null;
        }
    }
    
    @Override
    public Organizacion buscarPorNIT(String nit) throws Exception {
        String sql = "SELECT * FROM organizacion WHERE nit = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nit);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearOrganizacion(rs);
            }
            
            return null;
        }
    }
    
    @Override
    public List<Organizacion> listarTodas() throws Exception {
        String sql = "SELECT * FROM organizacion ORDER BY id_usuario DESC";
        List<Organizacion> organizaciones = new ArrayList<>();
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                organizaciones.add(mapearOrganizacion(rs));
            }
        }
        
        return organizaciones;
    }
    
    @Override
    public List<Organizacion> listarPorEstadoValidacion(String estadoValidacion) throws Exception {
        String sql = "SELECT * FROM organizacion WHERE estado_validacion = ? ORDER BY id_usuario DESC";
        List<Organizacion> organizaciones = new ArrayList<>();
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, estadoValidacion);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                organizaciones.add(mapearOrganizacion(rs));
            }
        }
        
        return organizaciones;
    }
    
    @Override
    public List<Organizacion> buscarPorNombre(String termino) throws Exception {
        String sql = "SELECT * FROM organizacion WHERE LOWER(nombre) LIKE ? ORDER BY nombre";
        List<Organizacion> organizaciones = new ArrayList<>();
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String patron = "%" + termino.toLowerCase() + "%";
            ps.setString(1, patron);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                organizaciones.add(mapearOrganizacion(rs));
            }
        }
        
        return organizaciones;
    }
    
    @Override
    public void actualizar(Organizacion organizacion) throws Exception {
        String sql = "UPDATE organizacion SET nombre = ?, direccion = ?, telefono = ?, " +
                     "tipo = ?, representante = ? WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, organizacion.getNombre());
            ps.setString(2, organizacion.getDireccion());
            ps.setString(3, organizacion.getTelefono());
            ps.setString(4, organizacion.getTipo());
            ps.setString(5, organizacion.getRepresentante());
            ps.setInt(6, organizacion.getIdUsuario());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new Exception("Organización no encontrada");
            }
            
            System.out.println("✓ [PostgreSQL] Organización actualizada: " + organizacion.getNombre());
        }
    }
    
    @Override
    public void cambiarEstadoValidacion(String idUsuario, String nuevoEstado) throws Exception {
        String sql = "UPDATE organizacion SET estado_validacion = ? WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(idUsuario));
            
            ps.executeUpdate();
            System.out.println("✓ [PostgreSQL] Estado de validación cambiado a: " + nuevoEstado);
        }
    }
    
    @Override
    public void eliminar(String idUsuario) throws Exception {
        String sql = "DELETE FROM organizacion WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(idUsuario));
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new Exception("Organización no encontrada");
            }
            
            System.out.println("✓ [PostgreSQL] Organización eliminada");
        }
    }
    
    @Override
    public boolean existeNIT(String nit) throws Exception {
        return buscarPorNIT(nit) != null;
    }
    
    @Override
    public boolean existe(String idUsuario) throws Exception {
        return buscarPorIdUsuario(idUsuario) != null;
    }
    
    @Override
    public int contar() throws Exception {
        String sql = "SELECT COUNT(*) as total FROM organizacion";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
            return 0;
        }
    }
    
    @Override
    public int contarPorEstadoValidacion(String estadoValidacion) throws Exception {
        String sql = "SELECT COUNT(*) as total FROM organizacion WHERE estado_validacion = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, estadoValidacion);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
            return 0;
        }
    }
    
    private Organizacion mapearOrganizacion(ResultSet rs) throws SQLException {
        Integer idUsuario = rs.getInt("id_usuario");
        String nombre = rs.getString("nombre");
        String direccion = rs.getString("direccion");
        String telefono = rs.getString("telefono");
        String tipo = rs.getString("tipo");
        String nit = rs.getString("nit");
        String representante = rs.getString("representante");
        String estadoValidacion = rs.getString("estado_validacion");
        
        return new Organizacion(idUsuario, nombre, direccion, telefono, 
                                tipo, nit, representante, estadoValidacion);
    }
}
