
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Categoria;

/**
 *
 * @author Melqui Erazo
 */
public class CategoriaDAO {

    private final MySQLConnection mysqlConnection;

    public CategoriaDAO() {
        this.mysqlConnection = new MySQLConnection();
    }

    /**
     * Obtiene el listado completo de categorías registradas en la base de datos.
     */
    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion FROM categorias ORDER BY id_categoria ASC";

        Connection con = mysqlConnection.conectar();
        if (con == null) return lista;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        } finally {
            mysqlConnection.desconectar();
        }
        return lista;
    }
}
