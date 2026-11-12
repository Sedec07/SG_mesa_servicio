package View;

import Controller.CtrlTicket;
import com.formdev.flatlaf.FlatClientProperties;
import Controller.CtrlSessionManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Ticket;
import model.Usuario;

public class FrmDashboardUsuario extends JFrame {

    private JTable tablaTickets;
    private DefaultTableModel modelTabla;
    private JButton btnVerDetalles;
    private JButton btnCerrarTicket;
    private JButton btnAgregarNuevo;
    private final CtrlTicket ctrlTicket;

    public FrmDashboardUsuario() {
        this.ctrlTicket = new CtrlTicket();
        initComponents();
        configurarEstilosFigma();
        cargarDatosTabla();
    }

    private void initComponents() {
        setTitle("Mesa de Servicio TI — Módulo de Gestión de Tickets Individuales");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JPanel panelFondo = new JPanel(new BorderLayout(20, 20));
        panelFondo.setBackground(Color.decode("#3B4252"));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Encabezado Superior (Figma)
        JPanel panelHeader = new JPanel(new GridLayout(3, 1, 3, 3));
        panelHeader.setOpaque(false);

        JLabel lblTitle = new JLabel("¡Bienvenido!");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub1 = new JLabel("Modulo de Gestion de Tickets Individuales");
        lblSub1.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub1.setForeground(Color.decode("#D8DEE9"));

        JLabel lblSub2 = new JLabel("Detalle y creacion de nuevos solicitudes de apoyo de TI");
        lblSub2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub2.setForeground(Color.decode("#E5E9F0"));

        panelHeader.add(lblTitle);
        panelHeader.add(lblSub1);
        panelHeader.add(lblSub2);

        // Barra de Acción (Botones superiores)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelBotones.setOpaque(false);

        btnVerDetalles = new JButton("Ver Detalles del Ticket");
        btnVerDetalles.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerDetalles.setPreferredSize(new Dimension(170, 38));

        btnCerrarTicket = new JButton("Cerrar Ticket");
        btnCerrarTicket.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarTicket.setPreferredSize(new Dimension(140, 38));

        btnAgregarNuevo = new JButton("Agregar Nuevo Ticket");
        btnAgregarNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAgregarNuevo.setPreferredSize(new Dimension(170, 38));

        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnCerrarTicket);

        JPanel panelAccionesHeader = new JPanel(new BorderLayout());
        panelAccionesHeader.setOpaque(false);
        panelAccionesHeader.add(panelBotones, BorderLayout.WEST);
        panelAccionesHeader.add(btnAgregarNuevo, BorderLayout.EAST);

        // Tabla de Tickets
        String[] columnas = {"Codigo", "Solicitante", "Categoria", "Prioridad", "Estado", "Creacion"};
        modelTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaTickets = new JTable(modelTabla);
        tablaTickets.setRowHeight(35);
        tablaTickets.setBackground(Color.decode("#434C5E"));
        tablaTickets.setForeground(Color.WHITE);
        tablaTickets.setSelectionBackground(Color.decode("#2E6B9E"));
        tablaTickets.getTableHeader().setBackground(Color.decode("#4C566A"));
        tablaTickets.getTableHeader().setForeground(Color.WHITE);
        tablaTickets.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollTabla = new JScrollPane(tablaTickets);
        scrollTabla.getViewport().setBackground(Color.decode("#3B4252"));
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        // Eventos
        btnVerDetalles.addActionListener(e -> abrirDetallesTicket());
        btnCerrarTicket.addActionListener(e -> abrirCierreTicket());
        btnAgregarNuevo.addActionListener(e -> abrirNuevoTicket());

        // Contenedor Central
        JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
        panelCentro.setOpaque(false);
        panelCentro.add(panelAccionesHeader, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        panelFondo.add(panelHeader, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);

        add(panelFondo);
    }

    private void configurarEstilosFigma() {
        btnVerDetalles.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 13 Segoe UI");
        btnCerrarTicket.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 13 Segoe UI");
        btnAgregarNuevo.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 13 Segoe UI");
    }

    public void cargarDatosTabla() {
        modelTabla.setRowCount(0);
        Usuario usuarioActual = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        if (usuarioActual == null) return;

        List<Ticket> lista = ctrlTicket.obtenerTicketsPorRol(usuarioActual);
        for (Ticket t : lista) {
            modelTabla.addRow(new Object[]{
                t.getCodigoUnico(),
                t.getNombreSolicitante(),
                t.getNombreCategoria(),
                t.getPrioridad(),
                t.getNombreEstado(),
                t.getFechaCreacion() != null ? t.getFechaCreacion().toString().substring(0, 10) : ""
            });
        }
    }

    private void abrirDetallesTicket() {
        int fila = tablaTickets.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ticket de la tabla para ver el detalle.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String codigo = (String) modelTabla.getValueAt(fila, 0);
        new FrmDetalleTicket(this, codigo).setVisible(true);
    }

    private void abrirCierreTicket() {
    int fila = tablaTickets.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un ticket para realizar el cierre.", "Atención", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Validar que el estado de la fila seleccionada sea 'RESUELTO'
    String estado = modelTabla.getValueAt(fila, 4).toString();
    if (!"RESUELTO".equalsIgnoreCase(estado)) {
        JOptionPane.showMessageDialog(
            this, 
            "El ticket actualmente se encuentra en estado '" + estado + "'.\nSolo se permite cerrar tickets que hayan sido marcados como 'RESUELTO' por el soporte técnico.", 
            "Validación de Cierre", 
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    String codigo = (String) modelTabla.getValueAt(fila, 0);
    Ticket t = ctrlTicket.obtenerPorCodigo(codigo);
    if (t != null) {
        new FrmCierreTicket(this, t.getIdTicket(), t.getCodigoUnico(), t.getTitulo(), this::cargarDatosTabla).setVisible(true);
        }
    }

    private void abrirNuevoTicket() {
        new FrmNuevoTicket(this, this::cargarDatosTabla).setVisible(true);
    }
}