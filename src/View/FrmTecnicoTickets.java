package View;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel de Gestión Operativa para el Técnico de Soporte - Mesa de Servicio TI
 * Muestra la bandeja de entrada, acciones rápidas de cambio de estado y filtrado.
 * @author Table Code
 */
public class FrmTecnicoTickets extends JFrame {

    private static FrmTecnicoTickets instancia;

    // Componentes principales
    private JTextField txtBuscar;
    private JComboBox<String> cmbCategoria, cmbEstado, cmbPrioridad;
    private JButton btnAtender, btnEscalar, btnResolver, btnVerDetalle, btnActualizar, btnCerrarSesion;
    private JTable tblTickets;
    private DefaultTableModel modeloTabla;
    private JLabel lblAsignados, lblEnProceso, lblCriticos, lblResueltosHoy;

    private FrmTecnicoTickets() {
        setTitle("Mesa de Servicio TI - Panel Operativo del Técnico");
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        aplicarEstilosFlatLaf();
        cargarDatosPrueba();
    }

    public static synchronized FrmTecnicoTickets getInstancia() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new FrmTecnicoTickets();
        }
        return instancia;
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));

        // 1. Panel Superior (Header + Tarjetas de Estado)
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        // Barra de Título y Botón de Salida
        JPanel pnlTituloBar = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Bandeja de Soporte Operativo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        btnCerrarSesion = new JButton("Cerrar Sesión");
        pnlTituloBar.add(lblTitulo, BorderLayout.WEST);
        pnlTituloBar.add(btnCerrarSesion, BorderLayout.EAST);

        JLabel lblSubtitulo = new JLabel("Técnico Asignado: Técnico Soporte L1 (tecnico@usc.edu.co)");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pnlHeader.add(pnlTituloBar);
        pnlHeader.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlHeader.add(lblSubtitulo);
        pnlHeader.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tarjetas resumen del técnico
        JPanel pnlTarjetas = new JPanel(new GridLayout(1, 4, 15, 0));

        lblAsignados = new JLabel("8", SwingConstants.CENTER);
        lblEnProceso = new JLabel("4", SwingConstants.CENTER);
        lblCriticos = new JLabel("2", SwingConstants.CENTER);
        lblResueltosHoy = new JLabel("5", SwingConstants.CENTER);

        pnlTarjetas.add(crearTarjetaMétrica("Tickets Pendientes", lblAsignados, "#2563EB"));
        pnlTarjetas.add(crearTarjetaMétrica("En Atención", lblEnProceso, "#D97706"));
        pnlTarjetas.add(crearTarjetaMétrica("Prioridad Crítica", lblCriticos, "#DC2626"));
        pnlTarjetas.add(crearTarjetaMétrica("Resueltos Hoy", lblResueltosHoy, "#16A34A"));

        pnlHeader.add(pnlTarjetas);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Panel Central (Filtros + Tabla)
        JPanel pnlCentro = new JPanel(new BorderLayout(10, 10));
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        // Barra de Búsqueda y Filtros
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        txtBuscar = new JTextField(18);
        cmbCategoria = new JComboBox<>(new String[]{"Todas las Categorías", "Hardware", "Software", "Redes y Conectividad", "Cuentas y Accesos"});
        cmbEstado = new JComboBox<>(new String[]{"Todos los Estados", "NUEVO", "EN_PROCESO", "ESCALADO", "RESUELTO"});
        cmbPrioridad = new JComboBox<>(new String[]{"Todas las Prioridades", "BAJA", "MEDIA", "ALTA", "CRITICA"});
        btnActualizar = new JButton("Refrescar");

        pnlFiltros.add(txtBuscar);
        pnlFiltros.add(cmbCategoria);
        pnlFiltros.add(cmbEstado);
        pnlFiltros.add(cmbPrioridad);
        pnlFiltros.add(btnActualizar);

        pnlCentro.add(pnlFiltros, BorderLayout.NORTH);

        // Tabla de Tickets del Técnico
        String[] columnas = {"Código", "Solicitante", "Categoría", "Prioridad", "Estado", "Fecha Registro"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTickets = new JTable(modeloTabla);
        tblTickets.setRowHeight(32);
        tblTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tblTickets);
        pnlCentro.add(scrollTabla, BorderLayout.CENTER);

        add(pnlCentro, BorderLayout.CENTER);

        // 3. Panel Inferior (Acciones de Flujo de Trabajo)
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        btnVerDetalle = new JButton("Visualizar Detalle");
        btnAtender = new JButton("Atender (En Proceso)");
        btnEscalar = new JButton("Escalar Ticket");
        btnResolver = new JButton("Marcar Resuelto");

        pnlFooter.add(btnVerDetalle);
        pnlFooter.add(btnAtender);
        pnlFooter.add(btnEscalar);
        pnlFooter.add(btnResolver);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    private JPanel crearTarjetaMétrica(String titulo, JLabel lblValor, String colorBorderHex) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(Color.decode("#9CA3AF"));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 20));

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);

        tarjeta.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 14; "
                + "background: darken($Panel.background, 3%); "
                + "border: 1," + colorBorderHex + ",1,14");

        return tarjeta;
    }

    private void aplicarEstilosFlatLaf() {
        // Redondeo y placeholders
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar ticket o usuario...");
        txtBuscar.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");

        cmbCategoria.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
        cmbEstado.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
        cmbPrioridad.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");

        // Botones de acción del flujo de trabajo
        btnVerDetalle.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
        btnActualizar.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
        btnCerrarSesion.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");

        btnAtender.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 12; "
                + "background: #2563EB; "
                + "foreground: #FFFFFF; "
                + "hoverBackground: #1D4ED8");

        btnEscalar.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 12; "
                + "background: #D97706; "
                + "foreground: #FFFFFF; "
                + "hoverBackground: #B45309");

        btnResolver.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 12; "
                + "background: #16A34A; "
                + "foreground: #FFFFFF; "
                + "hoverBackground: #15803D");

        // Tabla estilizada
        tblTickets.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 12; "
                + "showHorizontalLines: true; "
                + "showVerticalLines: false; "
                + "rowHeight: 32");
    }

    private void cargarDatosPrueba() {
        modeloTabla.setRowCount(0);

        Object[][] datos = {
            {"TCK-1001", "Melquisedec Erazo", "Hardware", "CRITICA", "NUEVO", "2026-10-14 08:15"},
            {"TCK-1002", "Juan Mejía", "Redes y Conectividad", "ALTA", "EN_PROCESO", "2026-10-14 08:45"},
            {"TCK-1003", "Jhon Maya", "Software", "MEDIA", "EN_PROCESO", "2026-10-14 09:10"},
            {"TCK-1004", "Jean Cabrera", "Cuentas y Accesos", "BAJA", "NUEVO", "2026-10-14 09:30"},
            {"TCK-1005", "Andrés López", "Hardware", "CRITICA", "ESCALADO", "2026-10-14 09:55"}
        };

        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
        } catch (Exception ex) {
            System.err.println("Error al cargar FlatLaf: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            FrmTecnicoTickets.getInstancia().setVisible(true);
        });
    }
}