package controller;

import DAO.UsuarioDAO;
import java.util.List;
import model.Usuario;
/**
 *
 * @author Melqui Erazo
 */
public class CtrlUsuario {

    private final UsuarioDAO usuarioDAO;

    public CtrlUsuario() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public List<Usuario> obtenerTodos() {
        return usuarioDAO.listar();
    }

    public boolean registrarUsuario(String nombre, String correo, String contrasena, int idRol) {
        if (nombre == null || nombre.trim().isEmpty() || 
            correo == null || correo.trim().isEmpty() || 
            contrasena == null || contrasena.trim().isEmpty()) {
            return false;
        }

        Usuario nuevoUsuario = new Usuario(nombre.trim(), correo.trim(), contrasena, idRol);
        return usuarioDAO.insertar(nuevoUsuario);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getIdUsuario() <= 0) {
            return false;
        }
        return usuarioDAO.actualizar(usuario);
    }

    public boolean inactivarUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            return false;
        }
        return usuarioDAO.inactivar(idUsuario);
    }
}