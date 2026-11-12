package View;

import com.formdev.flatlaf.FlatClientProperties;
import Controller.CtrlCategoria;
import Controller.CtrlSessionManager;
import Controller.CtrlTicket;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.Categoria;
import model.Ticket;
import model.Usuario;

public class FrmDashboardAdmin extends JFrame {

    private JLabel lblTotalTickets;
    private JLabel lblPendientes;
    private JLabel lblEnProceso;
    private JLabel lblResueltos;

    private JTextField txtBuscar;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbEstado;

    private JTable tablaTickets;
    private DefaultTableModel modelTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private JButton btnVerDetalle;
    private JButton btnGestionUsuarios;
    private JButton btnCerrarSesion;

    private final CtrlTicket ctrlTicket;
    private final CtrlCategoria ctrlCategoria;

    public FrmDashboardAdmin() {
        this.ctrlTicket = new CtrlTicket();
        this.ctrlCategoria = new CtrlCategoria();
        initComponents();
        configurarEstilosFigma();
        configurarEventosFiltros();
        cargarCategorias();
        cargarDatosDashboard();
    }

    private void initComponents() {
        setTitle("Mesa de Servicio TI — Panel de Administración Global");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 720);
        setLocationRelativeTo(null);

        JPanel panelFondo = new JPanel(new BorderLayout(15, 15));
        panelFondo.setBackground(Color.decode("#3B4252"));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // --- Header Superior ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JPanel panelTitulos = new JPanel(new GridLayout(2, 1, 2, 2));
        panelTitulos.setOpaque(false);

        JLabel lblSubHeader = new JLabel("Mesa de Servicio TI - Módulo de Administración");
        lblSubHeader.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubHeader.setForeground(Color.decode("#D8DEE9"));

        JLabel lblTitulo = new JLabel("Panel de Control General");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulos.add(lblSubHeader);
        panelTitulos.add(lblTitulo);

        btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelHeader.add(panelTitulos, BorderLayout.WEST);
        panelHeader.add(btnCerrarSesion, BorderLayout.EAST);

        // --- Grilla de Tarjetas de Métricas (4 Indicadores) ---
        JPanel panelMetricas = new JPanel(new GridLayout(1, 4, 15, 0));
        panelMetricas.setOpaque(false);

        lblTotalTickets = new JLabel("0");
        lblPendientes = new JLabel("0");
        lblEnProceso = new JLabel("0");
        lblResueltos = new JLabel("0");

        panelMetricas.add(crearCardMetrica("Total Tickets", lblTotalTickets, "#1865C9"));
        panelMetricas.add(crearCardMetrica("Pendientes (Nuevos)", lblPendientes, "#D97706"));
        panelMetricas.add(crearCardMetrica("En Proceso", lblEnProceso, "#0284C7"));
        panelMetricas.add(crearCardMetrica("Resueltos / Cerrados", lblResueltos, "#16A34A"));

        // --- Barra de Filtros ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.setOpaque(false);

        txtBuscar = new JTextField(20);
        cbCategoria = new JComboBox<>(new String[]{"Todas las Categorias"});
        cbEstado = new JComboBox<>(new String[]{"Todos los Estados", "NUEVO", "EN_PROCESO", "ESCALADO", "RESUELTO", "CERRADO"});

        panelFiltros.add(txtBuscar);
        panelFiltros.add(cbCategoria);
        panelFiltros.add(cbEstado);

        // --- Tabla de Tickets Global ---
        String[] columnas = {"Codigo", "Solicitante", "Categoria", "Tecnico Asignado", "Prioridad", "Estado", "Creacion"};
        modelTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTickets = new JTable(modelTabla);
        sorter = new TableRowSorter<>(modelTabla);
        tablaTickets.setRowSorter(sorter);
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

        // --- Botones Inferiores de Administración ---
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        panelAcciones.setOpaque(false);

        btnVerDetalle = new JButton("Ver Detalle");
        btnGestionUsuarios = new JButton("Gestionar Usuarios");

        btnVerDetalle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGestionUsuarios.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelAcciones.add(btnVerDetalle);
        panelAcciones.add(btnGestionUsuarios);

        // Eventos
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        btnVerDetalle.addActionListener(e -> abrirDetalle());
        btnGestionUsuarios.addActionListener(e -> abrirGestionUsuarios());

        // --- Estructura Central ---
        JPanel panelCentro = new JPanel(new BorderLayout(0, 10));
        panelCentro.setOpaque(false);

        JPanel panelTopCentro = new JPanel(new BorderLayout(0, 10));
        panelTopCentro.setOpaque(false);
        panelTopCentro.add(panelMetricas, BorderLayout.NORTH);
        panelTopCentro.add(panelFiltros, BorderLayout.SOUTH);

        panelCentro.add(panelTopCentro, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);
        panelCentro.add(panelAcciones, BorderLayout.SOUTH);

        panelFondo.add(panelHeader, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);

        add(panelFondo);
    }

    private JPanel crearCardMetrica(String titulo, JLabel lblValor, String hexColorAccento) {
        JPanel card = new PanelRedondeado(16, Color.decode("#4C566A"));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTit.setForeground(Color.decode("#D8DEE9"));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(Color.decode(hexColorAccento));

        card.add(lblTit);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(lblValor);

        return card;
    }

    private void configurarEstilosFigma() {
        btnCerrarSesion.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #4C566A; foreground: #D8DEE9; font: bold 12 Segoe UI");

        txtBuscar.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #434C5E; foreground: #FFFFFF; margin: 5,8,5,8");
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar ticket, solicitante o técnico...");

        cbCategoria.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #434C5E; foreground: #D8DEE9");
        cbEstado.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #434C5E; foreground: #D8DEE9");

        btnVerDetalle.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #4C566A; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnGestionUsuarios.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 12 Segoe UI");
    }

    private void configurarEventosFiltros() {
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override
            public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override
            public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        cbCategoria.addActionListener(e -> aplicarFiltros());
        cbEstado.addActionListener(e -> aplicarFiltros());
    }

    private void aplicarFiltros() {
        if (sorter == null) return;

        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        String texto = txtBuscar.getText().trim();
        if (!texto.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + Pattern.quote(texto), 0, 1, 3));
        }

        if (cbCategoria.getSelectedIndex() > 0 && cbCategoria.getSelectedItem() != null) {
            String cat = cbCategoria.getSelectedItem().toString();
            filtros.add(RowFilter.regexFilter("^" + Pattern.quote(cat) + "$", 2));
        }

        if (cbEstado.getSelectedIndex() > 0 && cbEstado.getSelectedItem() != null) {
            String est = cbEstado.getSelectedItem().toString();
            filtros.add(RowFilter.regexFilter("^" + Pattern.quote(est) + "$", 5));
        }

        sorter.setRowFilter(filtros.isEmpty() ? null : RowFilter.andFilter(filtros));
    }

    private void cargarCategorias() {
        List<Categoria> lista = ctrlCategoria.obtenerCategorias();
        for (Categoria c : lista) {
            cbCategoria.addItem(c.getNombre());
        }
    }

    public void cargarDatosDashboard() {
        modelTabla.setRowCount(0);

        Usuario adminActual = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        if (adminActual == null) return;

        List<Ticket> lista = ctrlTicket.obtenerTicketsPorRol(adminActual);

        int total = lista.size();
        int pendientes = 0;
        int enProceso = 0;
        int resueltos = 0;

        for (Ticket t : lista) {
            String estado = t.getNombreEstado() != null ? t.getNombreEstado().toUpperCase() : "";

            if ("NUEVO".equals(estado)) pendientes++;
            else if ("EN_PROCESO".equals(estado) || "ESCALADO".equals(estado)) enProceso++;
            else if ("RESUELTO".equals(estado) || "CERRADO".equals(estado)) resueltos++;

            modelTabla.addRow(new Object[]{
                t.getCodigoUnico(),
                t.getNombreSolicitante(),
                t.getNombreCategoria(),
                t.getNombreTecnico() != null ? t.getNombreTecnico() : "Sin Asignar",
                t.getPrioridad(),
                t.getNombreEstado(),
                t.getFechaCreacion() != null ? t.getFechaCreacion().toString().substring(0, 10) : ""
            });
        }

        lblTotalTickets.setText(String.valueOf(total));
        lblPendientes.setText(String.valueOf(pendientes));
        lblEnProceso.setText(String.valueOf(enProceso));
        lblResueltos.setText(String.valueOf(resueltos));
    }

    private void abrirDetalle() {
        int fila = tablaTickets.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ticket para visualizar su detalle.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int filaModelo = tablaTickets.convertRowIndexToModel(fila);
        String codigo = (String) modelTabla.getValueAt(filaModelo, 0);
        new FrmDetalleTicket(this, codigo).setVisible(true);
    }

    private void abrirGestionUsuarios() {
        new FrmGestionUsuarios(this).setVisible(true);
    }

    private void cerrarSesion() {
        CtrlSessionManager.getInstance().cerrarSesion();
        this.dispose();
        new FrmLogin().setVisible(true);
    }

    private static class PanelRedondeado extends JPanel {
        private final int radio;
        private final Color colorFondo;

        public PanelRedondeado(int radio, Color colorFondo) {
            this.radio = radio;
            this.colorFondo = colorFondo;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}