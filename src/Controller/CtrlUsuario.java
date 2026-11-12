package Controller;

import DAO.UsuarioDAO;
import java.util.List;
import model.Usuario;

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
        nuevoUsuario.setEstado("ACTIVO");
        return usuarioDAO.insertar(nuevoUsuario);
    }

    public boolean actualizarUsuario(Usuario usuarioEditado) {
        if (usuarioEditado == null || usuarioEditado.getIdUsuario() <= 0 ||
            usuarioEditado.getNombre() == null || usuarioEditado.getNombre().trim().isEmpty() ||
            usuarioEditado.getCorreo() == null || usuarioEditado.getCorreo().trim().isEmpty()) {
            return false;
        }

        // Buscar datos actuales en la lista para no sobrescribir contraseña o estado con vacíos/null
        Usuario actual = null;
        for (Usuario u : usuarioDAO.listar()) {
            if (u.getIdUsuario() == usuarioEditado.getIdUsuario()) {
                actual = u;
                break;
            }
        }

        if (actual == null) return false;

        if (usuarioEditado.getContrasena() == null || usuarioEditado.getContrasena().trim().isEmpty()) {
            usuarioEditado.setContrasena(actual.getContrasena());
        }

        if (usuarioEditado.getEstado() == null || usuarioEditado.getEstado().trim().isEmpty()) {
            usuarioEditado.setEstado(actual.getEstado() != null ? actual.getEstado() : "ACTIVO");
        }

        return usuarioDAO.actualizar(usuarioEditado);
    }

    public boolean inactivarUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            return false;
        }
        return usuarioDAO.inactivar(idUsuario);
    }
}