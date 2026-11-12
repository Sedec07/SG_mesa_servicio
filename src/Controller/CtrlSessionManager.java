package Controller;

import model.Usuario;
/**
 *
 * @author Melqui Erazo
 * Almacena el usuario autenticado durante la ejecución de la app.
 */
public class CtrlSessionManager {

    private static CtrlSessionManager instance;
    private Usuario usuarioAutenticado;

    private CtrlSessionManager() {}
    
    public static CtrlSessionManager getInstance() {
        if (instance == null) {
            instance = new CtrlSessionManager();
        }
        return instance;
    }

    public void iniciarSesion(Usuario usuario) {
        this.usuarioAutenticado = usuario;
    }

    public void cerrarSesion() {
        this.usuarioAutenticado = null;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public boolean isAutenticado() {
        return usuarioAutenticado != null;
    }
}