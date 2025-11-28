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
import Modelo.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class UsuarioPostgreSQLDAO implements IUsuarioDAO {
    
    @Override
    public String crear(Usuario usuario) throws Exception {
        String sql = "INSERT INTO usuario (username, email, password, rol, estado, fecha_creacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_usuario";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPassword());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getEstado());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int idGenerado = rs.getInt("id_usuario");
                usuario.setIdUsuario(idGenerado);
                System.out.println("✓ [PostgreSQL] Usuario creado con ID: " + idGenerado);
                return String.valueOf(idGenerado);
            }
            
            throw new Exception("No se pudo obtener el ID generado");
        }
    }
    
    @Override
    public Usuario buscarPorId(String id) throws Exception {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
            return null;
        }
    }
    
    @Override
    public Usuario buscarPorUsername(String username) throws Exception {
        String sql = "SELECT * FROM usuario WHERE username = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
            return null;
        }
    }
    
    @Override
    public Usuario buscarPorEmail(String email) throws Exception {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
            return null;
        }
    }
    
    @Override
    public Usuario buscarPorToken(String token) throws Exception {
        String sql = "SELECT * FROM usuario WHERE token_recuperacion = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
            return null;
        }
    }
    
    @Override
    public List<Usuario> listarTodos() throws Exception {
        String sql = "SELECT * FROM usuario ORDER BY id_usuario DESC";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        
        return usuarios;
    }
    
    @Override
    public List<Usuario> listarPorRol(String rol) throws Exception {
        String sql = "SELECT * FROM usuario WHERE rol = ? ORDER BY id_usuario DESC";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        
        return usuarios;
    }
    
    @Override
    public List<Usuario> listarPorEstado(String estado) throws Exception {
        String sql = "SELECT * FROM usuario WHERE estado = ? ORDER BY id_usuario DESC";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        
        return usuarios;
    }
    
    @Override
    public void actualizar(Usuario usuario) throws Exception {
        String sql = "UPDATE usuario SET username = ?, email = ?, password = ?, " +
                     "rol = ?, estado = ? WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getPassword());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getEstado());
            ps.setInt(6, usuario.getIdUsuario());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new Exception("Usuario no encontrado");
            }
            
            System.out.println("✓ [PostgreSQL] Usuario actualizado: " + usuario.getUsername());
        }
    }
    
    @Override
    public void actualizarPassword(String id, String nuevaPassword) throws Exception {
        String sql = "UPDATE usuario SET password = ? WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nuevaPassword);
            ps.setInt(2, Integer.parseInt(id));
            
            ps.executeUpdate();
            System.out.println("✓ [PostgreSQL] Contraseña actualizada");
        }
    }
    
    @Override
    public void actualizarUltimaConexion(String id) throws Exception {
        String sql = "UPDATE usuario SET ultima_conexion = ? WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, Integer.parseInt(id));
            
            ps.executeUpdate();
        }
    }
    
    @Override
    public void cambiarEstado(String id, String nuevoEstado) throws Exception {
        String sql = "UPDATE usuario SET estado = ? WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nuevoEstado);
            ps.setInt(2, Integer.parseInt(id));
            
            ps.executeUpdate();
            System.out.println("✓ [PostgreSQL] Estado cambiado a: " + nuevoEstado);
        }
    }
    
    @Override
    public void guardarTokenRecuperacion(String id, String token, LocalDateTime expiracion) throws Exception {
        String sql = "UPDATE usuario SET token_recuperacion = ?, token_expiracion = ? WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, token);
            ps.setTimestamp(2, Timestamp.valueOf(expiracion));
            ps.setInt(3, Integer.parseInt(id));
            
            ps.executeUpdate();
            System.out.println("✓ [PostgreSQL] Token guardado");
        }
    }
    
    @Override
    public void limpiarToken(String id) throws Exception {
        String sql = "UPDATE usuario SET token_recuperacion = NULL, token_expiracion = NULL WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        }
    }
    
    @Override
    public void eliminar(String id) throws Exception {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(id));
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new Exception("Usuario no encontrado");
            }
            
            System.out.println("✓ [PostgreSQL] Usuario eliminado");
        }
    }
    
    @Override
    public boolean existeUsername(String username) throws Exception {
        return buscarPorUsername(username) != null;
    }
    
    @Override
    public boolean existeEmail(String email) throws Exception {
        return buscarPorEmail(email) != null;
    }
    
    @Override
    public int contar() throws Exception {
        String sql = "SELECT COUNT(*) as total FROM usuario";
        
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
    public int contarPorRol(String rol) throws Exception {
        String sql = "SELECT COUNT(*) as total FROM usuario WHERE rol = ?";
        
        try (Connection conn = PostgreSQLConnection.getNewConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
            return 0;
        }
    }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setUsername(rs.getString("username"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password"));
        usuario.setRol(rs.getString("rol"));
        usuario.setEstado(rs.getString("estado"));
        
        // Campos opcionales
        String token = rs.getString("token_recuperacion");
        if (token != null) {
            usuario.setTokenRecuperacion(token);
        }
        
        Timestamp tokenExp = rs.getTimestamp("token_expiracion");
        if (tokenExp != null) {
            usuario.setTokenExpiracion(tokenExp.toLocalDateTime());
        }
        
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            usuario.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        
        Timestamp ultimaConexion = rs.getTimestamp("ultima_conexion");
        if (ultimaConexion != null) {
            usuario.setUltimaConexion(ultimaConexion.toLocalDateTime());
        }
        
        return usuario;
    }
}
