/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author flori
 */

/**
 * Clase modelo Organización
 * Representa la entidad Organización en la base de datos
 */
public class Organizacion {
    private String id; // Para MongoDB
    private Integer idUsuario; // Para PostgreSQL (FK a Usuario)
    private Usuario usuario; // Objeto Usuario asociado
    private String nombre;
    private String direccion;
    private String telefono;
    private String tipo; // Tipo de organización (ONG, Fundación, etc.)
    private String nit; // NIT único de la organización
    private String representante; // Representante legal
    private String estadoValidacion; // Pendiente, Aprobado, Rechazado

    // Constructor vacío
    public Organizacion() {
        this.estadoValidacion = "Pendiente";
    }

    // Constructor completo con Usuario
    public Organizacion(Usuario usuario, String nombre, String direccion, 
                        String telefono, String tipo, String nit, 
                        String representante, String estadoValidacion) {
        this.usuario = usuario;
        this.idUsuario = usuario != null ? usuario.getIdUsuario() : null;
        this.id = usuario != null ? usuario.getId() : null;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipo = tipo;
        this.nit = nit;
        this.representante = representante;
        this.estadoValidacion = estadoValidacion != null ? estadoValidacion : "Pendiente";
    }

    // Constructor para crear nueva (con datos de Usuario)
    public Organizacion(String username, String email, String password,
                        String nombre, String direccion, String telefono, 
                        String tipo, String nit, String representante) {
        this.usuario = new Usuario(username, email, password, "Organizacion", "Pendiente");
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipo = tipo;
        this.nit = nit;
        this.representante = representante;
        this.estadoValidacion = "Pendiente";
    }

    // Constructor para cargar desde BD (PostgreSQL)
    public Organizacion(Integer idUsuario, String nombre, String direccion, 
                        String telefono, String tipo, String nit, 
                        String representante, String estadoValidacion) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipo = tipo;
        this.nit = nit;
        this.representante = representante;
        this.estadoValidacion = estadoValidacion;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getEstadoValidacion() {
        return estadoValidacion;
    }

    public void setEstadoValidacion(String estadoValidacion) {
        this.estadoValidacion = estadoValidacion;
    }

    /**
     * Obtiene el identificador único independiente de la BD
     */
    public String getIdentificador() {
        return id != null ? id : (idUsuario != null ? idUsuario.toString() : null);
    }

    /**
     * Verifica si la organización está aprobada
     */
    public boolean estaAprobada() {
        return "Aprobado".equals(estadoValidacion);
    }

    @Override
    public String toString() {
        return "Organizacion{" +
                "id='" + id + '\'' +
                ", idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", nit='" + nit + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estadoValidacion='" + estadoValidacion + '\'' +
                '}';
    }
}