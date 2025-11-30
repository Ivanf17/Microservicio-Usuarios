/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author flori
 */

public class Administrador {
    private String id; // Para MongoDB
    private Integer idUsuario; // Para PostgreSQL (FK a Usuario)
    private Usuario usuario; // Objeto Usuario asociado
    private String nombre;
    private String cargo; // Cargo del administrador

    public Administrador() {
    }

    // Constructor completo con Usuario
    public Administrador(Usuario usuario, String nombre, String cargo) {
        this.usuario = usuario;
        this.idUsuario = usuario != null ? usuario.getIdUsuario() : null;
        this.id = usuario != null ? usuario.getId() : null;
        this.nombre = nombre;
        this.cargo = cargo;
    }

    // Constructor para crear nuevo (con datos de Usuario)
    public Administrador(String username, String email, String password, 
                         String nombre, String cargo) {
        this.usuario = new Usuario(username, email, password, "Administrador", "Activo");
        this.nombre = nombre;
        this.cargo = cargo;
    }

    // Constructor para cargar desde BD (PostgreSQL)
    public Administrador(Integer idUsuario, String nombre, String cargo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.cargo = cargo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario != null ? idUsuario : (usuario != null ? usuario.getIdUsuario() : null);
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            this.idUsuario = usuario.getIdUsuario();
            this.id = usuario.getId();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getIdentificador() {
        return id != null ? id : (idUsuario != null ? idUsuario.toString() : null);
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "id='" + id + '\'' +
                ", idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}