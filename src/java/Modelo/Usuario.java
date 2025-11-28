/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author flori
 */
import java.time.LocalDateTime;

/**
 * Clase modelo Usuario
 * Representa la entidad Usuario en la base de datos
 */
public class Usuario {
    private String id; // ObjectId para MongoDB, String para compatibilidad
    private Integer idUsuario; // id_usuario para PostgreSQL
    private String username;
    private String email;
    private String password;
    private String rol; // Voluntario, Organizacion, Administrador
    private String estado; // Activo, Inactivo, Pendiente
    private String tokenRecuperacion;
    private LocalDateTime tokenExpiracion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimaConexion;

    // Constructor vacío
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "Activo";
    }

    // Constructor completo para PostgreSQL
    public Usuario(Integer idUsuario, String username, String email, 
                   String password, String rol, String estado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estado = estado != null ? estado : "Activo";
        this.fechaCreacion = LocalDateTime.now();
    }

    // Constructor sin ID (para insertar)
    public Usuario(String username, String email, String password, 
                   String rol, String estado) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estado = estado != null ? estado : "Activo";
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public LocalDateTime getTokenExpiracion() {
        return tokenExpiracion;
    }

    public void setTokenExpiracion(LocalDateTime tokenExpiracion) {
        this.tokenExpiracion = tokenExpiracion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getUltimaConexion() {
        return ultimaConexion;
    }

    public void setUltimaConexion(LocalDateTime ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    /**
     * Obtiene el identificador único independiente de la BD
     */
    public String getIdentificador() {
        return id != null ? id : (idUsuario != null ? idUsuario.toString() : null);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", idUsuario=" + idUsuario +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}