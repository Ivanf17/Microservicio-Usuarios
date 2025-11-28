/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author flori
 */

import java.time.LocalDate;

/**
 * Clase modelo Voluntario
 * Representa la entidad Voluntario en la base de datos
 */
public class Voluntario {
    private String id; // Para MongoDB
    private Integer idUsuario; // Para PostgreSQL (FK a Usuario)
    private Usuario usuario; // Objeto Usuario asociado
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;

    // Constructor vacío
    public Voluntario() {
    }

    // Constructor con Usuario completo
    public Voluntario(Usuario usuario, String nombre, String apellido, 
                      LocalDate fechaNacimiento, String direccion, String telefono) {
        this.usuario = usuario;
        this.idUsuario = usuario != null ? usuario.getIdUsuario() : null;
        this.id = usuario != null ? usuario.getId() : null;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Constructor para crear nuevo (con datos de Usuario)
    public Voluntario(String username, String email, String password,
                      String nombre, String apellido, LocalDate fechaNacimiento, 
                      String direccion, String telefono) {
        this.usuario = new Usuario(username, email, password, "Voluntario", "Activo");
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Constructor para cargar desde BD (PostgreSQL)
    public Voluntario(Integer idUsuario, String nombre, String apellido, 
                      LocalDate fechaNacimiento, String direccion, String telefono) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Getters y Setters
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Obtiene el identificador único independiente de la BD
     */
    public String getIdentificador() {
        return id != null ? id : (idUsuario != null ? idUsuario.toString() : null);
    }

    @Override
    public String toString() {
        return "Voluntario{" +
                "id='" + id + '\'' +
                ", idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
