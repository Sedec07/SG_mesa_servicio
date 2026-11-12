package model;

import java.sql.Timestamp;

/**
 * @author Melqui Erazo
 */
public class HistorialTicket {

    private int idHistorial;
    private int idTicket;
    private int idUsuarioAccion;
    private String comentario;
    private Integer idEstadoAnterior; // Integer objeto para permitir NULL (en creación inicial)
    private Integer idEstadoNuevo;    // Integer objeto para permitir NULL (en comentarios simples)
    private Timestamp fechaRegistro;
    private Timestamp fechaCambio;

    // Atributos auxiliares para presentar información detallada en vistas/tablas
    private String codigoTicket;
    private String nombreUsuarioAccion;
    private String nombreEstadoAnterior;
    private String nombreEstadoNuevo;

    // Constructor vacío
    public HistorialTicket() {
    }

    // Constructor completo
    public HistorialTicket(int idHistorial, int idTicket, int idUsuarioAccion, 
                           String comentario, Integer idEstadoAnterior, Integer idEstadoNuevo, 
                           Timestamp fechaRegistro) {
        this.idHistorial = idHistorial;
        this.idTicket = idTicket;
        this.idUsuarioAccion = idUsuarioAccion;
        this.comentario = comentario;
        this.idEstadoAnterior = idEstadoAnterior;
        this.idEstadoNuevo = idEstadoNuevo;
        this.fechaRegistro = fechaRegistro;
    }

    // Constructor simplificado para inserción de traza desde el DAO
    public HistorialTicket(int idTicket, int idUsuarioAccion, String comentario, 
                           Integer idEstadoAnterior, Integer idEstadoNuevo) {
        this.idTicket = idTicket;
        this.idUsuarioAccion = idUsuarioAccion;
        this.comentario = comentario;
        this.idEstadoAnterior = idEstadoAnterior;
        this.idEstadoNuevo = idEstadoNuevo;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdUsuarioAccion() {
        return idUsuarioAccion;
    }

    public void setIdUsuarioAccion(int idUsuarioAccion) {
        this.idUsuarioAccion = idUsuarioAccion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getIdEstadoAnterior() {
        return idEstadoAnterior;
    }

    public void setIdEstadoAnterior(Integer idEstadoAnterior) {
        this.idEstadoAnterior = idEstadoAnterior;
    }

    public Integer getIdEstadoNuevo() {
        return idEstadoNuevo;
    }

    public void setIdEstadoNuevo(Integer idEstadoNuevo) {
        this.idEstadoNuevo = idEstadoNuevo;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // --- GETTERS Y SETTERS AUXILIARES ---

    public String getCodigoTicket() {
        return codigoTicket;
    }

    public void setCodigoTicket(String codigoTicket) {
        this.codigoTicket = codigoTicket;
    }

    public String getNombreUsuarioAccion() {
        return nombreUsuarioAccion;
    }

    public void setNombreUsuarioAccion(String nombreUsuarioAccion) {
        this.nombreUsuarioAccion = nombreUsuarioAccion;
    }

    public String getNombreEstadoAnterior() {
        return nombreEstadoAnterior != null ? nombreEstadoAnterior : "N/A";
    }

    public void setNombreEstadoAnterior(String nombreEstadoAnterior) {
        this.nombreEstadoAnterior = nombreEstadoAnterior;
    }

    public String getNombreEstadoNuevo() {
        return nombreEstadoNuevo != null ? nombreEstadoNuevo : "N/A";
    }

    public void setNombreEstadoNuevo(String nombreEstadoNuevo) {
        this.nombreEstadoNuevo = nombreEstadoNuevo;
    }
    public Timestamp getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Timestamp fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}