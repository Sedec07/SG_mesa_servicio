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

public class FrmTecnicoTickets extends JFrame {

    private JLabel lblContadorPendientes;
    private JLabel lblInfoTecnico;
    private JTextField txtBuscar;
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbEstado;
    private JComboBox<String> cbPrioridad;

    private JTable tablaTickets;
    private DefaultTableModel modelTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private JButton btnCerrarSesion;
    private JButton btnVerDetalle;
    private JButton btnAtender;
    private JButton btnEscalar;
    private JButton btnMarcarResultado;

    private final CtrlTicket ctrlTicket;
    private final CtrlCategoria ctrlCategoria;

    public FrmTecnicoTickets() {
        this.ctrlTicket = new CtrlTicket();
        this.ctrlCategoria = new CtrlCategoria();
        initComponents();
        configurarEstilosFigma();
        configurarEventosFiltros();
        cargarDatosHeader();
        cargarCategorias();
        cargarDatosTabla();
    }

    private void initComponents() {
        setTitle("Mesa de Servicio TI — Panel Operativo del Técnico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 700);
        setLocationRelativeTo(null);

        JPanel panelFondo = new JPanel(new BorderLayout(15, 15));
        panelFondo.setBackground(Color.decode("#3B4252"));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // --- Encabezado Superior ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JPanel panelTitulos = new JPanel(new GridLayout(2, 1, 2, 2));
        panelTitulos.setOpaque(false);

        JLabel lblSubHeader = new JLabel("Mesa de Servicio TI - Panel Operativo del Tecnico");
        lblSubHeader.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubHeader.setForeground(Color.decode("#D8DEE9"));

        JLabel lblTitulo = new JLabel("Bandeja de Soporte Operativo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulos.add(lblSubHeader);
        panelTitulos.add(lblTitulo);

        JPanel panelTecnicoInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelTecnicoInfo.setOpaque(false);

        lblInfoTecnico = new JLabel("Tecnico Asignado: Soporte L1");
        lblInfoTecnico.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfoTecnico.setForeground(Color.decode("#E5E9F0"));

        btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelTecnicoInfo.add(lblInfoTecnico);
        panelTecnicoInfo.add(btnCerrarSesion);

        panelHeader.add(panelTitulos, BorderLayout.WEST);
        panelHeader.add(panelTecnicoInfo, BorderLayout.EAST);

        // --- Panel de Métrica (Tickets Pendientes) ---
        JPanel panelMetrica = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelMetrica.setOpaque(false);

        JPanel cardMetrica = new PanelRedondeado(16, Color.decode("#4C566A"));
        cardMetrica.setLayout(new BoxLayout(cardMetrica, BoxLayout.Y_AXIS));
        cardMetrica.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 24));

        JLabel lblTituloPendientes = new JLabel("Tickets Pendientes");
        lblTituloPendientes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTituloPendientes.setForeground(Color.decode("#D8DEE9"));

        lblContadorPendientes = new JLabel("0");
        lblContadorPendientes.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblContadorPendientes.setForeground(Color.WHITE);

        cardMetrica.add(lblTituloPendientes);
        cardMetrica.add(Box.createRigidArea(new Dimension(0, 4)));
        cardMetrica.add(lblContadorPendientes);

        panelMetrica.add(cardMetrica);

        // --- Barra de Filtros ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.setOpaque(false);

        txtBuscar = new JTextField(18);
        cbCategoria = new JComboBox<>(new String[]{"Todas las Categorias"});
        cbEstado = new JComboBox<>(new String[]{"Todos los Estados", "NUEVO", "EN_PROCESO", "ESCALADO", "RESUELTO", "CERRADO"});
        cbPrioridad = new JComboBox<>(new String[]{"Todas las Prioridades", "CRITICA", "ALTA", "MEDIA", "BAJA"});

        panelFiltros.add(txtBuscar);
        panelFiltros.add(cbCategoria);
        panelFiltros.add(cbEstado);
        panelFiltros.add(cbPrioridad);

        // --- Tabla de Tickets ---
        String[] columnas = {"Codigo", "Solicitante", "Categoria", "Prioridades", "Estado", "Fecha Registro"};
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

        // --- Botones Inferiores ---
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        panelAcciones.setOpaque(false);

        btnVerDetalle = new JButton("Visualizar Detalle");
        btnAtender = new JButton("Atender (En Pro)");
        btnEscalar = new JButton("Escala Ticket");
        btnMarcarResultado = new JButton("Marcar Resultado");

        btnVerDetalle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtender.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEscalar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMarcarResultado.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelAcciones.add(btnVerDetalle);
        panelAcciones.add(btnAtender);
        panelAcciones.add(btnEscalar);
        panelAcciones.add(btnMarcarResultado);

        // --- Eventos ---
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        btnVerDetalle.addActionListener(e -> abrirDetalle());
        btnAtender.addActionListener(e -> marcarEnProceso());
        btnEscalar.addActionListener(e -> abrirEscalamiento());
        btnMarcarResultado.addActionListener(e -> marcarComoResuelto());

        // --- Contenedor Central ---
        JPanel panelCentro = new JPanel(new BorderLayout(0, 10));
        panelCentro.setOpaque(false);

        JPanel panelTopCentro = new JPanel(new BorderLayout(0, 10));
        panelTopCentro.setOpaque(false);
        panelTopCentro.add(panelMetrica, BorderLayout.NORTH);
        panelTopCentro.add(panelFiltros, BorderLayout.SOUTH);

        panelCentro.add(panelTopCentro, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);
        panelCentro.add(panelAcciones, BorderLayout.SOUTH);

        panelFondo.add(panelHeader, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);

        add(panelFondo);
    }

    private void configurarEstilosFigma() {
        btnCerrarSesion.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #4C566A; foreground: #D8DEE9; font: bold 12 Segoe UI");

        txtBuscar.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #434C5E; foreground: #FFFFFF; margin: 5,8,5,8");
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar ticket o usuario...");

        cbCategoria.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #434C5E; foreground: #D8DEE9");
        cbEstado.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #434C5E; foreground: #D8DEE9");
        cbPrioridad.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #434C5E; foreground: #D8DEE9");

        btnVerDetalle.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #4C566A; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnAtender.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnEscalar.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #D97706; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnMarcarResultado.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #16A34A; foreground: #FFFFFF; font: bold 12 Segoe UI");
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
        cbPrioridad.addActionListener(e -> aplicarFiltros());
    }

    private void aplicarFiltros() {
        if (sorter == null) return;

        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        String texto = txtBuscar.getText().trim();
        if (!texto.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + Pattern.quote(texto), 0, 1));
        }

        if (cbCategoria.getSelectedIndex() > 0 && cbCategoria.getSelectedItem() != null) {
            String cat = cbCategoria.getSelectedItem().toString();
            filtros.add(RowFilter.regexFilter("^" + Pattern.quote(cat) + "$", 2));
        }

        if (cbPrioridad.getSelectedIndex() > 0 && cbPrioridad.getSelectedItem() != null) {
            String prio = cbPrioridad.getSelectedItem().toString();
            filtros.add(RowFilter.regexFilter("(?i)^" + Pattern.quote(prio) + "$", 3));
        }

        if (cbEstado.getSelectedIndex() > 0 && cbEstado.getSelectedItem() != null) {
            String est = cbEstado.getSelectedItem().toString();
            filtros.add(RowFilter.regexFilter("^" + Pattern.quote(est) + "$", 4));
        }

        sorter.setRowFilter(filtros.isEmpty() ? null : RowFilter.andFilter(filtros));
    }

    private void cargarDatosHeader() {
        Usuario tecnico = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        if (tecnico != null) {
            lblInfoTecnico.setText("Tecnico Asignado: " + tecnico.getNombre() + " (" + tecnico.getCorreo() + ")");
        }
    }

    private void cargarCategorias() {
        List<Categoria> lista = ctrlCategoria.obtenerCategorias();
        for (Categoria c : lista) {
            cbCategoria.addItem(c.getNombre());
        }
    }

    public void cargarDatosTabla() {
        modelTabla.setRowCount(0);
        Usuario tecnico = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        if (tecnico == null) return;

        List<Ticket> lista = ctrlTicket.obtenerTicketsPorRol(tecnico);
        int pendientes = 0;

        for (Ticket t : lista) {
            String estado = t.getNombreEstado() != null ? t.getNombreEstado() : "";
            if (!"RESUELTO".equalsIgnoreCase(estado) && !"CERRADO".equalsIgnoreCase(estado)) {
                pendientes++;
            }
            modelTabla.addRow(new Object[]{
                t.getCodigoUnico(),
                t.getNombreSolicitante(),
                t.getNombreCategoria(),
                t.getPrioridad(),
                estado,
                t.getFechaCreacion() != null ? t.getFechaCreacion().toString().substring(0, 10) : ""
            });
        }
        lblContadorPendientes.setText(String.valueOf(pendientes));
    }

    private Ticket obtenerTicketSeleccionado() {
        int fila = tablaTickets.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ticket de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int filaModelo = tablaTickets.convertRowIndexToModel(fila);
        String codigo = modelTabla.getValueAt(filaModelo, 0).toString();
        return ctrlTicket.obtenerPorCodigo(codigo);
    }

    private void abrirDetalle() {
        Ticket t = obtenerTicketSeleccionado();
        if (t != null) {
            new FrmDetalleTicket(this, t.getCodigoUnico()).setVisible(true);
        }
    }

    private void marcarEnProceso() {
        Ticket t = obtenerTicketSeleccionado();
        Usuario tecnico = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        if (t == null || tecnico == null) return;

        if ("CERRADO".equalsIgnoreCase(t.getNombreEstado()) || "RESUELTO".equalsIgnoreCase(t.getNombreEstado())) {
            JOptionPane.showMessageDialog(this, "No se puede pasar a EN_PROCESO un ticket ya resuelto o cerrado.", "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Estado 2 = EN_PROCESO
        boolean ok = ctrlTicket.cambiarEstadoTicket(t.getIdTicket(), t.getIdEstado(), 2, tecnico.getIdUsuario(), "Ticket tomado en atención por " + tecnico.getNombre());
        if (ok) {
            JOptionPane.showMessageDialog(this, "El ticket " + t.getCodigoUnico() + " ahora está EN_PROCESO.", "Estado Actualizado", JOptionPane.INFORMATION_MESSAGE);
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado del ticket.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirEscalamiento() {
        Ticket t = obtenerTicketSeleccionado();
        if (t == null) return;

        if ("CERRADO".equalsIgnoreCase(t.getNombreEstado()) || "RESUELTO".equalsIgnoreCase(t.getNombreEstado())) {
            JOptionPane.showMessageDialog(this, "No se puede escalar un ticket ya resuelto o cerrado.", "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new FrmEscalarTicket(this, t.getIdTicket(), t.getCodigoUnico(), t.getNombreSolicitante(), this::cargarDatosTabla).setVisible(true);
    }

    private void marcarComoResuelto() {
        Ticket t = obtenerTicketSeleccionado();
        Usuario tecnico = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        if (t == null || tecnico == null) return;

        if ("CERRADO".equalsIgnoreCase(t.getNombreEstado())) {
            JOptionPane.showMessageDialog(this, "Este ticket ya se encuentra CERRADO.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String diagnostico = JOptionPane.showInputDialog(this, "Ingrese el detalle de la solución aplicada para el ticket " + t.getCodigoUnico() + ":", "Marcar Ticket como Resuelto", JOptionPane.PLAIN_MESSAGE);
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            return;
        }

        // Estado 4 = RESUELTO (según TicketDAO.actualizarEstado)
        boolean ok = ctrlTicket.cambiarEstadoTicket(t.getIdTicket(), t.getIdEstado(), 4, tecnico.getIdUsuario(), "Solución técnica: " + diagnostico.trim());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Ticket " + t.getCodigoUnico() + " marcado como RESUELTO. El usuario ya puede validarlo y cerrarlo.", "Solución Registrada", JOptionPane.INFORMATION_MESSAGE);
            cargarDatosTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la solución del ticket.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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