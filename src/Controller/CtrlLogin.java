package Controller;

import DAO.UsuarioDAO;
import model.Usuario;
/**
 *
 * @author Melqui Erazo
 */
public class CtrlLogin {

    private final UsuarioDAO usuarioDAO;

    public CtrlLogin() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Autentica al usuario y guarda la sesión si las credenciales son válidas.
     * @return El objeto Usuario si el login es exitoso, null si falla.
     */
    public Usuario autenticar(String correo, String contrasena) {
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            return null;
        }

        Usuario usuario = usuarioDAO.validarLogin(correo.trim(), contrasena);
        
        if (usuario != null) {
            CtrlSessionManager.getInstance().iniciarSesion(usuario);
        }
        
        return usuario;
    }
}