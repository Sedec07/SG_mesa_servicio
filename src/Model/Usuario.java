package model;

import java.sql.Timestamp;

/**
 * @author Melqui Erazo
 */
public class Usuario {

    private int idUsuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private int idRol;
    private String nombreRol;
    private String estado; 
    private int ticketsAsignados;
    private Timestamp fechaCreacion;

    // Constructor vacío (Requerido para instanciación en DAO)
    public Usuario() {
    }

    // Constructor completo con ID
    public Usuario(int idUsuario, String nombre, String correo, String contrasena, 
                   int idRol, String estado, int ticketsAsignados, Timestamp fechaCreacion) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.idRol = idRol;
        this.estado = estado;
        this.ticketsAsignados = ticketsAsignados;
        this.fechaCreacion = fechaCreacion;
    }

    // Constructor para registro (sin ID ni campos automáticos)
    public Usuario(String nombre, String correo, String contrasena, int idRol) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.idRol = idRol;
        this.estado = "ACTIVO";
        this.ticketsAsignados = 0;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTicketsAsignados() {
        return ticketsAsignados;
    }

    public void setTicketsAsignados(int ticketsAsignados) {
        this.ticketsAsignados = ticketsAsignados;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}