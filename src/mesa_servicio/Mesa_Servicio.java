package mesa_servicio;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import View.FrmLogin;

public class Mesa_Servicio {

    public static void main(String[] args) {
        // Configuración global del Look and Feel (FlatLaf Dark)
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Error al aplicar el tema FlatLaf: " + ex.getMessage());
        }

        // Lanzamiento de la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}