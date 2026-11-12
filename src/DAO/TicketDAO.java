package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Ticket;
/**
 *
 * @author Melqui Erazo
 */
public class TicketDAO {

    private final MySQLConnection mysqlConnection;

    public TicketDAO() {
        this.mysqlConnection = new MySQLConnection();
    }

    /**
     * 1. Inserta un nuevo Ticket en la base de datos.
     */
    public boolean insertar(Ticket t) {
        String sql = "INSERT INTO tickets (codigo_unico, titulo, descripcion, prioridad, id_categoria, id_solicitante, id_tecnico_asignado, id_estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = mysqlConnection.conectar();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getCodigoUnico());
            ps.setString(2, t.getTitulo());
            ps.setString(3, t.getDescripcion());
            ps.setString(4, t.getPrioridad());
            ps.setInt(5, t.getIdCategoria());
            ps.setInt(6, t.getIdSolicitante());
            
            if (t.getIdTecnicoAsignado() != null && t.getIdTecnicoAsignado() > 0) {
                ps.setInt(7, t.getIdTecnicoAsignado());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            
            ps.setInt(8, t.getIdEstado() > 0 ? t.getIdEstado() : 1); // 1 = 'NUEVO'

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar ticket: " + e.getMessage());
            return false;
        } finally {
            mysqlConnection.desconectar();
        }
    }

    /**
     * 2. Obtiene la lista completa de tickets (Vista Administrador).
     */
    public List<Ticket> listarTodos() {
        String sql = "SELECT t.id_ticket, t.codigo_unico, t.titulo, t.descripcion, t.prioridad, "
                   + "t.id_categoria, c.nombre AS categoria, "
                   + "t.id_solicitante, u_sol.nombre AS solicitante, "
                   + "t.id_tecnico_asignado, u_tec.nombre AS tecnico, "
                   + "t.id_estado, e.nombre AS estado, "
                   + "t.fecha_creacion, t.fecha_actualizacion, t.fecha_cierre "
                   + "FROM tickets t "
                   + "INNER JOIN categorias c ON t.id_categoria = c.id_categoria "
                   + "INNER JOIN usuarios u_sol ON t.id_solicitante = u_sol.id_usuario "
                   + "LEFT JOIN usuarios u_tec ON t.id_tecnico_asignado = u_tec.id_usuario "
                   + "INNER JOIN estados_ticket e ON t.id_estado = e.id_estado "
                   + "ORDER BY t.id_ticket DESC";

        return ejecutarConsultaLista(sql, null);
    }

    /**
     * 3. Obtiene los tickets creados por un solicitante específico (Vista Usuario).
     */
    public List<Ticket> listarPorSolicitante(int idSolicitante) {
        String sql = "SELECT t.id_ticket, t.codigo_unico, t.titulo, t.descripcion, t.prioridad, "
                   + "t.id_categoria, c.nombre AS categoria, "
                   + "t.id_solicitante, u_sol.nombre AS solicitante, "
                   + "t.id_tecnico_asignado, u_tec.nombre AS tecnico, "
                   + "t.id_estado, e.nombre AS estado, "
                   + "t.fecha_creacion, t.fecha_actualizacion, t.fecha_cierre "
                   + "FROM tickets t "
                   + "INNER JOIN categorias c ON t.id_categoria = c.id_categoria "
                   + "INNER JOIN usuarios u_sol ON t.id_solicitante = u_sol.id_usuario "
                   + "LEFT JOIN usuarios u_tec ON t.id_tecnico_asignado = u_tec.id_usuario "
                   + "INNER JOIN estados_ticket e ON t.id_estado = e.id_estado "
                   + "WHERE t.id_solicitante = ? "
                   + "ORDER BY t.id_ticket DESC";

        return ejecutarConsultaLista(sql, idSolicitante);
    }

    /**
     * 4. Obtiene los tickets asignados a un técnico específico (Vista Técnico).
     */
    public List<Ticket> listarPorTecnico(int idTecnico) {
        String sql = "SELECT t.id_ticket, t.codigo_unico, t.titulo, t.descripcion, t.prioridad, "
                   + "t.id_categoria, c.nombre AS categoria, "
                   + "t.id_solicitante, u_sol.nombre AS solicitante, "
                   + "t.id_tecnico_asignado, u_tec.nombre AS tecnico, "
                   + "t.id_estado, e.nombre AS estado, "
                   + "t.fecha_creacion, t.fecha_actualizacion, t.fecha_cierre "
                   + "FROM tickets t "
                   + "INNER JOIN categorias c ON t.id_categoria = c.id_categoria "
                   + "INNER JOIN usuarios u_sol ON t.id_solicitante = u_sol.id_usuario "
                   + "LEFT JOIN usuarios u_tec ON t.id_tecnico_asignado = u_tec.id_usuario "
                   + "INNER JOIN estados_ticket e ON t.id_estado = e.id_estado "
                   + "WHERE t.id_tecnico_asignado = ? "
                   + "ORDER BY t.id_ticket DESC";

        return ejecutarConsultaLista(sql, idTecnico);
    }

    /**
     * 5. Busca un ticket por su ID primario.
     */
    public Ticket buscarPorId(int idTicket) {
        String sql = "SELECT t.id_ticket, t.codigo_unico, t.titulo, t.descripcion, t.prioridad, "
                   + "t.id_categoria, c.nombre AS categoria, "
                   + "t.id_solicitante, u_sol.nombre AS solicitante, "
                   + "t.id_tecnico_asignado, u_tec.nombre AS tecnico, "
                   + "t.id_estado, e.nombre AS estado, "
                   + "t.fecha_creacion, t.fecha_actualizacion, t.fecha_cierre "
                   + "FROM tickets t "
                   + "INNER JOIN categorias c ON t.id_categoria = c.id_categoria "
                   + "INNER JOIN usuarios u_sol ON t.id_solicitante = u_sol.id_usuario "
                   + "LEFT JOIN usuarios u_tec ON t.id_tecnico_asignado = u_tec.id_usuario "
                   + "INNER JOIN estados_ticket e ON t.id_estado = e.id_estado "
                   + "WHERE t.codigo_unico = ?";

        List<Ticket> lista = ejecutarConsultaLista(sql, idTicket);
        return lista.isEmpty() ? null : lista.get(0);
    }

    /**
     * 6. Actualiza el estado de un ticket y registra la fecha de cierre si aplica.
     */
    public boolean actualizarEstado(int idTicket, int idNuevoEstado) {
        // idNuevoEstado 4 = 'RESUELTO', 5 = 'CERRADO'
        String sql = (idNuevoEstado == 4 || idNuevoEstado == 5)
                   ? "UPDATE tickets SET id_estado = ?, fecha_cierre = NOW() WHERE id_ticket = ?"
                   : "UPDATE tickets SET id_estado = ? WHERE id_ticket = ?";

        Connection con = mysqlConnection.conectar();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idNuevoEstado);
            ps.setInt(2, idTicket);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del ticket: " + e.getMessage());
            return false;
        } finally {
            mysqlConnection.desconectar();
        }
    }

    /**
     * 7. Asigna o reasigna un técnico a un ticket.
     */
    public boolean asignarTecnico(int idTicket, int idTecnico) {
        String sql = "UPDATE tickets SET id_tecnico_asignado = ? WHERE id_ticket = ?";
        Connection con = mysqlConnection.conectar();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTecnico);
            ps.setInt(2, idTicket);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al asignar técnico al ticket: " + e.getMessage());
            return false;
        } finally {
            mysqlConnection.desconectar();
        }
    }

    /**
     * 8. Genera el siguiente código único secuencial (Ej: "1002" o "TCK-1002").
     */
    public String generarCodigoUnico() {
        String sql = "SELECT MAX(id_ticket) AS ultimo FROM tickets";
        Connection con = mysqlConnection.conectar();
        int siguienteId = 1001;

        if (con != null) {
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maxId = rs.getInt("ultimo");
                    if (maxId > 0) {
                        siguienteId = maxId + 1001;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al generar código único: " + e.getMessage());
            } finally {
                mysqlConnection.desconectar();
            }
        }
        return String.valueOf(siguienteId);
    }

    // --- MÉTODO AUXILIAR PARA MAPEAR RESULTSET A OBJETOS TICKET ---

    private List<Ticket> ejecutarConsultaLista(String sql, Integer parametroId) {
        List<Ticket> lista = new ArrayList<>();
        Connection con = mysqlConnection.conectar();
        if (con == null) return lista;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametroId != null) {
                ps.setInt(1, parametroId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket t = new Ticket();
                    t.setIdTicket(rs.getInt("id_ticket"));
                    t.setCodigoUnico(rs.getString("codigo_unico"));
                    t.setTitulo(rs.getString("titulo"));
                    t.setDescripcion(rs.getString("descripcion"));
                    t.setPrioridad(rs.getString("prioridad"));
                    t.setIdCategoria(rs.getInt("id_categoria"));
                    t.setNombreCategoria(rs.getString("categoria"));
                    t.setIdSolicitante(rs.getInt("id_solicitante"));
                    t.setNombreSolicitante(rs.getString("solicitante"));
                    
                    int idTec = rs.getInt("id_tecnico_asignado");
                    if (!rs.wasNull()) {
                        t.setIdTecnicoAsignado(idTec);
                        t.setNombreTecnico(rs.getString("tecnico"));
                    } else {
                        t.setIdTecnicoAsignado(null);
                        t.setNombreTecnico("Sin Asignar");
                    }

                    t.setIdEstado(rs.getInt("id_estado"));
                    t.setNombreEstado(rs.getString("estado"));
                    t.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    t.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
                    t.setFechaCierre(rs.getTimestamp("fecha_cierre"));

                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar consulta de tickets: " + e.getMessage());
        } finally {
            mysqlConnection.desconectar();
        }
        return lista;
    }
}