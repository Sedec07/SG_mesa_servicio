package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;

/**
 *
 * @author Melqui Erazo
 */
public class UsuarioDAO {

    private final MySQLConnection mysqlConnection;

    public UsuarioDAO() {
        this.mysqlConnection = new MySQLConnection();
    }

    /**
     * 1. Autentica al usuario en el sistema según su correo y contraseña activa.
     */
    public Usuario validarLogin(String correo, String contrasena) {
        String sql = "SELECT u.id_usuario, u.nombre, u.correo, u.contrasena, u.id_rol, r.nombre AS rol, u.estado, u.tickets_asignados, u.fecha_creacion "
                   + "FROM usuarios u "
                   + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                   + "WHERE u.correo = ? AND u.contrasena = ? AND u.estado = 'ACTIVO'";

        Connection con = mysqlConnection.conectar();
        if (con == null) return null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setCorreo(rs.getString("correo"));
                    u.setContrasena(rs.getString("contrasena"));
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setNombreRol(rs.getString("rol"));
                    u.setEstado(rs.getString("estado"));
                    u.setTicketsAsignados(rs.getInt("tickets_asignados"));
                    u.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
        } finally {
            mysqlConnection.desconectar();
        }
        return null;
    }

    /**
     * 2. Inserta un nuevo usuario en la base de datos.
     */
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, id_rol, estado, tickets_asignados) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = mysqlConnection.conectar();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getContrasena());
            ps.setInt(4, u.getIdRol());
            ps.setString(5, u.getEstado() != null ? u.getEstado() : "ACTIVO");
            ps.setInt(6, u.getTicketsAsignados());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        } finally {
            mysqlConnection.desconectar();
        }
    }

    /**
     * 3. Obtiene la lista completa de usuarios registrados.
     */
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.nombre, u.correo, u.contrasena, u.id_rol, r.nombre AS rol, u.estado, u.tickets_asignados, u.fecha_creacion "
                   + "FROM usuarios u "
                   + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                   + "ORDER BY u.id_usuario ASC";

        Connection con = mysqlConnection.conectar();
        if (con == null) return lista;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasena(rs.getString("contrasena"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setNombreRol(rs.getString("rol"));
                u.setEstado(rs.getString("estado"));
                u.setTicketsAsignados(rs.getInt("tickets_asignados"));
                u.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        } finally {
            mysqlConnection.desconectar();
        }
        return lista;
    }

    /**
     * 4. Algoritmo de Balanceo de Carga: Busca el técnico activo (id_rol = 2) con menos tickets asignados.
     */
    public Usuario obtenerTecnicoMenosCargado() {
        String sql = "SELECT u.id_usuario, u.nombre, u.correo, u.contrasena, u.id_rol, r.nombre AS rol, u.estado, u.tickets_asignados, u.fecha_creacion "
                   + "FROM usuarios u "
                   + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                   + "WHERE u.id_rol = 2 AND u.estado = 'ACTIVO' "
                   + "ORDER BY u.tickets_asignados ASC, u.id_usuario ASC LIMIT 1";

        Connection con = mysqlConnection.conectar();
        if (con == null) return null;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasena(rs.getString("contrasena"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setNombreRol(rs.getString("rol"));
                u.setEstado(rs.getString("estado"));
                u.setTicketsAsignados(rs.getInt("tickets_asignados"));
                u.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                return u;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener técnico menos cargado: " + e.getMessage());
        } finally {
            mysqlConnection.desconectar();
        }
        return null;
    }

    /**
     * 5. Incrementa en +1 el contador de tickets asignados al técnico.
     */
    public boolean incrementarTicketsAsignados(int idUsuario) {
        String sql = "UPDATE usuarios SET tickets_asignados = tickets_asignados + 1 WHERE id_usuario = ?";
        Connection con = mysqlConnection.conectar();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al incrementar tickets asignados: " + e.getMessage());
            return false;
        } finally {
            mysqlConnection.desconectar();
        }
    }

    /**
     * 6. Actualiza la información básica de un usuario.
     */
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, contrasena = ?, id_rol = ?, estado = ? WHERE id_usuario = ?";
        Connection con = mysqlConnection.conectar();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getContrasena());
            ps.setInt(4, u.getIdRol());
            ps.setString(5, u.getEstado());
            ps.setInt(6, u.getIdUsuario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        } finally {
            mysqlConnection.desconectar();
        }
    }

    /**
     * 7. Cambia el estado de un usuario a 'INACTIVO'.
     */
    public boolean inactivar(int idUsuario) {
        String sql = "UPDATE usuarios SET estado = 'INACTIVO' WHERE id_usuario = ?";
        Connection con = mysqlConnection.conectar();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al inactivar usuario: " + e.getMessage());
            return false;
        } finally {
            mysqlConnection.desconectar();
        }
    }
}