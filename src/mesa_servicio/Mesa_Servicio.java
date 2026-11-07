package mesa_servicio;

import View.FrmReportesAdmin;
import View.FrmTecnicoTickets;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;

public class Mesa_Servicio {

    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
        } catch (Exception ex) {
            System.err.println("Error al cargar FlatLaf: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            FrmReportesAdmin.getInstancia().setVisible(true);
            FrmTecnicoTickets.getInstancia().setVisible(true);
        });
    }
}