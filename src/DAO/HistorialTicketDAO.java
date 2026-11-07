
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.HistorialTicket;

/**
 *
 * @author Melqui Erazo
 */
public class HistorialTicketDAO {

    private final MySQLConnection mysqlConnection;

    public HistorialTicketDAO() {
        this.mysqlConnection = new MySQLConnection();
    }

    /**
     * 1. Inserta un nuevo registro de auditoría en la bitácora del historial.
     */
    public boolean insertar(HistorialTicket h) {
        String sql = "INSERT INTO historial_tickets (id_ticket, id_usuario_accion, comentario, id_estado_anterior, id_estado_nuevo) "
                   + "VALUES (?, ?, ?, ?, ?)";
        Connection con = mysqlConnection.conectar();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, h.getIdTicket());
            ps.setInt(2, h.getIdUsuarioAccion());
            ps.setString(3, h.getComentario());

            if (h.getIdEstadoAnterior() != null && h.getIdEstadoAnterior() > 0) {
                ps.setInt(4, h.getIdEstadoAnterior());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            if (h.getIdEstadoNuevo() != null && h.getIdEstadoNuevo() > 0) {
                ps.setInt(5, h.getIdEstadoNuevo());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar historial de ticket: " + e.getMessage());
            return false;
        } finally {
            mysqlConnection.desconectar();
        }
    }

    /**
     * 2. Consulta la trazabilidad completa de un ticket específico ordenado por fecha.
     */
    public List<HistorialTicket> listarPorTicket(int idTicket) {
        List<HistorialTicket> lista = new ArrayList<>();
        String sql = "SELECT h.id_historial, h.id_ticket, t.codigo_unico, "
                   + "h.id_usuario_accion, u.nombre AS usuario_accion, "
                   + "h.comentario, "
                   + "h.id_estado_anterior, e_ant.nombre AS estado_anterior, "
                   + "h.id_estado_nuevo, e_nue.nombre AS estado_nuevo, "
                   + "h.fecha_registro "
                   + "FROM historial_tickets h "
                   + "INNER JOIN tickets t ON h.id_ticket = t.id_ticket "
                   + "INNER JOIN usuarios u ON h.id_usuario_accion = u.id_usuario "
                   + "LEFT JOIN estados_ticket e_ant ON h.id_estado_anterior = e_ant.id_estado "
                   + "LEFT JOIN estados_ticket e_nue ON h.id_estado_nuevo = e_nue.id_estado "
                   + "WHERE h.id_ticket = ? "
                   + "ORDER BY h.fecha_registro ASC";

        Connection con = mysqlConnection.conectar();
        if (con == null) return lista;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTicket);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HistorialTicket h = new HistorialTicket();
                    h.setIdHistorial(rs.getInt("id_historial"));
                    h.setIdTicket(rs.getInt("id_ticket"));
                    h.setCodigoTicket(rs.getString("codigo_unico"));
                    h.setIdUsuarioAccion(rs.getInt("id_usuario_accion"));
                    h.setNombreUsuarioAccion(rs.getString("usuario_accion"));
                    h.setComentario(rs.getString("comentario"));

                    int idAnt = rs.getInt("id_estado_anterior");
                    if (!rs.wasNull()) {
                        h.setIdEstadoAnterior(idAnt);
                        h.setNombreEstadoAnterior(rs.getString("estado_anterior"));
                    } else {
                        h.setIdEstadoAnterior(null);
                        h.setNombreEstadoAnterior("N/A");
                    }

                    int idNue = rs.getInt("id_estado_nuevo");
                    if (!rs.wasNull()) {
                        h.setIdEstadoNuevo(idNue);
                        h.setNombreEstadoNuevo(rs.getString("estado_nuevo"));
                    } else {
                        h.setIdEstadoNuevo(null);
                        h.setNombreEstadoNuevo("N/A");
                    }

                    h.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    lista.add(h);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar historial de ticket: " + e.getMessage());
        } finally {
            mysqlConnection.desconectar();
        }
        return lista;
    }
}