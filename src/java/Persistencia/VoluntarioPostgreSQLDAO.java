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
import Modelo.Voluntario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VoluntarioPostgreSQLDAO implements IVoluntarioDAO {
    
    @Override
    public String crear(Voluntario voluntario) throws Exception {
        String sql = "INSERT INTO voluntario (id_usuario, nombre, apellido, fecha_nacimiento, direccion, telefono) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, voluntario.getIdUsuario());
            ps.setString(2, voluntario.getNombre());
            ps.setString(3, voluntario.getApellido());
            
            if (voluntario.getFechaNacimiento() != null) {
                ps.setDate(4, Date.valueOf(voluntario.getFechaNacimiento()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            
            ps.setString(5, voluntario.getDireccion());
            ps.setString(6, voluntario.getTelefono());
            
            ps.executeUpdate();
            
            System.out.println("✓ [PostgreSQL] Voluntario creado: " + voluntario.getNombreCompleto());
            return String.valueOf(voluntario.getIdUsuario());
        }
    }
    
    @Override
    public Voluntario buscarPorIdUsuario(String idUsuario) throws Exception {
        String sql = "SELECT * FROM voluntario WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(idUsuario));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearVoluntario(rs);
            }
            
            return null;
        }
    }
    
    @Override
    public List<Voluntario> listarTodos() throws Exception {
        String sql = "SELECT * FROM voluntario ORDER BY id_usuario DESC";
        List<Voluntario> voluntarios = new ArrayList<>();
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                voluntarios.add(mapearVoluntario(rs));
            }
        }
        
        return voluntarios;
    }
    
    @Override
    public List<Voluntario> buscarPorNombre(String termino) throws Exception {
        String sql = "SELECT * FROM voluntario WHERE LOWER(nombre) LIKE ? OR LOWER(apellido) LIKE ? " +
                     "ORDER BY nombre, apellido";
        List<Voluntario> voluntarios = new ArrayList<>();
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String patron = "%" + termino.toLowerCase() + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                voluntarios.add(mapearVoluntario(rs));
            }
        }
        
        return voluntarios;
    }
    
    @Override
    public void actualizar(Voluntario voluntario) throws Exception {
        String sql = "UPDATE voluntario SET nombre = ?, apellido = ?, fecha_nacimiento = ?, " +
                     "direccion = ?, telefono = ? WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, voluntario.getNombre());
            ps.setString(2, voluntario.getApellido());
            
            if (voluntario.getFechaNacimiento() != null) {
                ps.setDate(3, Date.valueOf(voluntario.getFechaNacimiento()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            
            ps.setString(4, voluntario.getDireccion());
            ps.setString(5, voluntario.getTelefono());
            ps.setInt(6, voluntario.getIdUsuario());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new Exception("Voluntario no encontrado");
            }
            
            System.out.println("✓ [PostgreSQL] Voluntario actualizado: " + voluntario.getNombreCompleto());
        }
    }
    
    @Override
    public void eliminar(String idUsuario) throws Exception {
        String sql = "DELETE FROM voluntario WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(idUsuario));
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new Exception("Voluntario no encontrado");
            }
            
            System.out.println("✓ [PostgreSQL] Voluntario eliminado");
        }
    }
    
    @Override
    public boolean existe(String idUsuario) throws Exception {
        return buscarPorIdUsuario(idUsuario) != null;
    }
    
    @Override
    public int contar() throws Exception {
        String sql = "SELECT COUNT(*) as total FROM voluntario";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
            return 0;
        }
    }
    

    private Voluntario mapearVoluntario(ResultSet rs) throws SQLException {
        Integer idUsuario = rs.getInt("id_usuario");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        
        Date sqlDate = rs.getDate("fecha_nacimiento");
        LocalDate fechaNacimiento = (sqlDate != null) ? sqlDate.toLocalDate() : null;
        
        String direccion = rs.getString("direccion");
        String telefono = rs.getString("telefono");
        
        return new Voluntario(idUsuario, nombre, apellido, fechaNacimiento, direccion, telefono);
    }
}
