package model;

import java.sql.Timestamp;

/**
 * @author Melqui Erazo
 */
public class Ticket {

    private int idTicket;
    private String codigoUnico;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private int idCategoria;
    private int idSolicitante;
    private Integer idTecnicoAsignado; // Objeto Integer para permitir valores NULL si no hay técnico asignado
    private int idEstado;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;
    private Timestamp fechaCierre;

    // Atributos auxiliares para desacoplar consultas con JOIN en los DAO y JTable
    private String nombreCategoria;
    private String nombreSolicitante;
    private String nombreTecnico;
    private String nombreEstado;

    // Constructor vacío (Requerido para instanciación mediante ResultSet en DAO)
    public Ticket() {
    }

    // Constructor completo (Para lectura de BD)
    public Ticket(int idTicket, String codigoUnico, String titulo, String descripcion, 
                  String prioridad, int idCategoria, int idSolicitante, Integer idTecnicoAsignado, 
                  int idEstado, Timestamp fechaCreacion, Timestamp fechaActualizacion, Timestamp fechaCierre) {
        this.idTicket = idTicket;
        this.codigoUnico = codigoUnico;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.idCategoria = idCategoria;
        this.idSolicitante = idSolicitante;
        this.idTecnicoAsignado = idTecnicoAsignado;
        this.idEstado = idEstado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.fechaCierre = fechaCierre;
    }

    // Constructor simplificado para la creación de un nuevo Ticket por un Solicitante
    public Ticket(String codigoUnico, String titulo, String descripcion, 
                  String prioridad, int idCategoria, int idSolicitante) {
        this.codigoUnico = codigoUnico;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.idCategoria = idCategoria;
        this.idSolicitante = idSolicitante;
        this.idEstado = 1; // 1 = 'NUEVO' por defecto según la BD
    }

    // --- GETTERS Y SETTERS ---

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(int idSolicitante) {
        this.idSolicitante = idSolicitante;
    }

    public Integer getIdTecnicoAsignado() {
        return idTecnicoAsignado;
    }

    public void setIdTecnicoAsignado(Integer idTecnicoAsignado) {
        this.idTecnicoAsignado = idTecnicoAsignado;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Timestamp getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Timestamp fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    // Getters y Setters para atributos auxiliares (JOINs)

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getNombreTecnico() {
        return nombreTecnico != null ? nombreTecnico : "Sin Asignar";
    }

    public void setNombreTecnico(String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    @Override
    public String toString() {
        return codigoUnico + " - " + titulo;
    }
}