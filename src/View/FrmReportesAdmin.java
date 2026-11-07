package View;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Módulo de Reportes de Administración - Mesa de Servicio TI
 * Muestra el catálogo de componentes modernos y estilos FlatLaf.
 * @author Table Code
 */
public class FrmReportesAdmin extends JFrame {

    private static FrmReportesAdmin instancia;

    // Componentes principales de la vista
    private JTextField txtBuscar;
    private JComboBox<String> cmbCategoria, cmbEstado, cmbPrioridad, cmbTecnico;
    private JTextField txtFechaInicio, txtFechaFin;
    private JButton btnFiltrar, btnExportarPDF, btnExportarExcel, btnLimpiar;
    private JTable tblReporte;
    private DefaultTableModel modeloTabla;
    private JProgressBar barCarga;
    private JLabel lblTotalTickets, lblResueltos, lblPendientes, lblTiempoPromedio;

    private FrmReportesAdmin() {
        setTitle("Mesa de Servicio TI - Reportes y Métricas Gerenciales");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initUI();
        aplicarEstilosFlatLaf();
        cargarDatosPrueba();
    }

    public static synchronized FrmReportesAdmin getInstancia() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new FrmReportesAdmin();
        }
        return instancia;
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));

        // 1. Panel Superior (Header + Tarjetas KPI)
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        // Título del Módulo
        JLabel lblTitulo = new JLabel("Módulo de Reportes y Trazabilidad de TI");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Consolidado analítico de incidentes, SLA y gestión de técnicos");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlHeader.add(lblTitulo);
        pnlHeader.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlHeader.add(lblSubtitulo);
        pnlHeader.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tarjetas Métricas KPI
        JPanel pnlTarjetas = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlTarjetas.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTotalTickets = new JLabel("142", SwingConstants.CENTER);
        lblResueltos = new JLabel("118", SwingConstants.CENTER);
        lblPendientes = new JLabel("24", SwingConstants.CENTER);
        lblTiempoPromedio = new JLabel("2.4 hrs", SwingConstants.CENTER);

        pnlTarjetas.add(crearTarjetaKPI("Total Tickets", lblTotalTickets, "#1E3A8A"));
        pnlTarjetas.add(crearTarjetaKPI("Resueltos (SLA)", lblResueltos, "#15803D"));
        pnlTarjetas.add(crearTarjetaKPI("En Proceso / Pend.", lblPendientes, "#B45309"));
        pnlTarjetas.add(crearTarjetaKPI("Tiempo Prom. Resolución", lblTiempoPromedio, "#6B21A8"));

        pnlHeader.add(pnlTarjetas);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Panel Central (Filtros + Tabla)
        JPanel pnlCentro = new JPanel(new BorderLayout(10, 10));
        pnlCentro.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        // Barra de Filtros Avanzados
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        txtBuscar = new JTextField(15);
        cmbCategoria = new JComboBox<>(new String[]{"Todas las Categorías", "Hardware", "Software", "Redes y Conectividad", "Cuentas y Accesos"});
        cmbEstado = new JComboBox<>(new String[]{"Todos los Estados", "NUEVO", "EN_PROCESO", "ESCALADO", "RESUELTO", "CERRADO"});
        cmbPrioridad = new JComboBox<>(new String[]{"Todas las Prioridades", "BAJA", "MEDIA", "ALTA", "CRITICA"});
        cmbTecnico = new JComboBox<>(new String[]{"Todos los Técnicos", "Técnico Soporte L1", "Juan Mejía", "Unassigned"});

        txtFechaInicio = new JTextField(8);
        txtFechaFin = new JTextField(8);

        btnFiltrar = new JButton("Aplicar Filtro");
        btnLimpiar = new JButton("Limpiar");

        pnlFiltros.add(txtBuscar);
        pnlFiltros.add(cmbCategoria);
        pnlFiltros.add(cmbEstado);
        pnlFiltros.add(cmbPrioridad);
        pnlFiltros.add(cmbTecnico);
        pnlFiltros.add(new JLabel("Desde:"));
        pnlFiltros.add(txtFechaInicio);
        pnlFiltros.add(new JLabel("Hasta:"));
        pnlFiltros.add(txtFechaFin);
        pnlFiltros.add(btnFiltrar);
        pnlFiltros.add(btnLimpiar);

        pnlCentro.add(pnlFiltros, BorderLayout.NORTH);

        // Tabla de Datos
        String[] columnas = {"Código", "Solicitante", "Categoría", "Prioridad", "Técnico Asignado", "Estado", "Fecha Creación", "Tiempo (hrs)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReporte = new JTable(modeloTabla);
        tblReporte.setRowHeight(32);
        tblReporte.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tblReporte);
        pnlCentro.add(scrollTabla, BorderLayout.CENTER);

        add(pnlCentro, BorderLayout.CENTER);

        // 3. Panel Inferior (Acciones de Descarga + Estado)
        JPanel pnlFooter = new JPanel(new BorderLayout(15, 10));
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        JPanel pnlAccionesExportar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnExportarPDF = new JButton("Descargar PDF");
        btnExportarExcel = new JButton("Exportar Excel");

        pnlAccionesExportar.add(btnExportarPDF);
        pnlAccionesExportar.add(btnExportarExcel);

        barCarga = new JProgressBar();
        barCarga.setValue(100);
        barCarga.setStringPainted(true);
        barCarga.setString("Datos cargados correctamente");

        pnlFooter.add(barCarga, BorderLayout.WEST);
        pnlFooter.add(pnlAccionesExportar, BorderLayout.EAST);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    private JPanel crearTarjetaKPI(String titulo, JLabel lblValor, String colorHex) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(Color.decode("#9CA3AF"));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 22));

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);

        tarjeta.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 16; "
                + "background: darken($Panel.background, 3%); "
                + "border: 1,#374151,1,16");

        return tarjeta;
    }

    private void aplicarEstilosFlatLaf() {
        // Redondeo y placeholders en campos de texto
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar por código o solicitante...");
        txtBuscar.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");

        txtFechaInicio.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "AAAA-MM-DD");
        txtFechaInicio.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");

        txtFechaFin.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "AAAA-MM-DD");
        txtFechaFin.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");

        // Estilos para ComboBoxes
        cmbCategoria.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
        cmbEstado.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
        cmbPrioridad.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");
        cmbTecnico.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");

        // Botones de acción principales
        btnFiltrar.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 12; "
                + "background: #2563EB; "
                + "foreground: #FFFFFF; "
                + "hoverBackground: #1D4ED8");

        btnLimpiar.putClientProperty(FlatClientProperties.STYLE, "arc: 12;");

        // Botones de exportación e informes
        btnExportarPDF.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 12; "
                + "background: #DC2626; "
                + "foreground: #FFFFFF; "
                + "hoverBackground: #B91C1C");

        btnExportarExcel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 12; "
                + "background: #16A34A; "
                + "foreground: #FFFFFF; "
                + "hoverBackground: #15803D");

        // Estilo de la tabla de reportes
        tblReporte.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 12; "
                + "showHorizontalLines: true; "
                + "showVerticalLines: false; "
                + "rowHeight: 32; "
                + "intercellSpacing: 0,1");
    }

    private void cargarDatosPrueba() {
        modeloTabla.setRowCount(0);
        
        Object[][] datos = {
            {"TCK-1001", "Melquisedec Erazo", "Hardware", "CRITICA", "Técnico Soporte L1", "RESUELTO", "2026-03-01 09:15", "1.5"},
            {"TCK-1002", "Juan Mejía", "Redes y Conectividad", "ALTA", "Técnico Soporte L1", "EN_PROCESO", "2026-03-02 10:30", "3.0"},
            {"TCK-1003", "Jhon Maya", "Software", "MEDIA", "Juan Mejía", "ESCALADO", "2026-03-02 11:45", "4.2"},
            {"TCK-1004", "Jean Cabrera", "Cuentas y Accesos", "BAJA", "Técnico Soporte L1", "CERRADO", "2026-03-03 08:20", "0.8"},
            {"TCK-1005", "Carlos Pérez", "Hardware", "ALTA", "Sin Asignar", "NUEVO", "2026-03-03 14:10", "0.0"},
            {"TCK-1006", "Ana Martínez", "Software", "CRITICA", "Juan Mejía", "RESUELTO", "2026-03-04 15:00", "2.1"}
        };

        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }

    
}