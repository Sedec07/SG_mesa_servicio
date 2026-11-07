package Model;

/**
 *
 * @author Melqui Erazo
 */

public class EstadoTicket {
    private int idEstado;
    private String nombre;

    public EstadoTicket() {}
    public EstadoTicket(int idEstado, String nombre) {
        this.idEstado = idEstado;
        this.nombre = nombre;
    }

    public int getIdEstado() { return idEstado; }
    public void setIdEstado(int idEstado) { this.idEstado = idEstado; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}