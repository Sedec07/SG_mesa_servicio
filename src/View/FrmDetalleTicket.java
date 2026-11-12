package View;

import com.formdev.flatlaf.FlatClientProperties;
import Controller.CtrlTicket;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import model.HistorialTicket;
import model.Ticket;

public class FrmDetalleTicket extends JDialog {

    private final String codigoTicket;
    private final CtrlTicket ctrlTicket;

    private JLabel lblCodigoGeneral;
    private JLabel lblSolicitante;
    private JLabel lblPrioridad;
    private JLabel lblEstado;
    private JLabel lblFechaCreacion;

    private JLabel lblTecnico;
    private JLabel lblCategoria;

    private JLabel lblTituloDetalle;
    private JTextArea txtDescripcion;

    private JPanel panelHistorialLista;
    private JTextArea txtUltimoComentario;

    public FrmDetalleTicket(Frame parent, String codigoTicket) {
        super(parent, "Mesa de Servicio TI — Detalle del Ticket", true);
        this.codigoTicket = codigoTicket;
        this.ctrlTicket = new CtrlTicket();

        initComponents();
        cargarInformacionTicket();
    }

    public FrmDetalleTicket(String codigoTicket) {
        this(null, codigoTicket);
    }

    private void initComponents() {
        setSize(960, 620);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel panelFondo = new JPanel(new BorderLayout(15, 15));
        panelFondo.setBackground(Color.decode("#3B4252"));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header Superior
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblTituloHeader = new JLabel("DETALLES DEL TICKET: " + codigoTicket);
        lblTituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloHeader.setForeground(Color.WHITE);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnCerrar.addActionListener(e -> dispose());

        panelHeader.add(lblTituloHeader, BorderLayout.WEST);
        panelHeader.add(btnCerrar, BorderLayout.EAST);

        // Grilla Principal de Tarjetas Redondeadas
        JPanel panelGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        panelGrid.setOpaque(false);

        // 1. Tarjeta Información General (Sin repeticiones)
        JPanel cardInfo = crearTarjeta("Información General");
        lblCodigoGeneral = crearLabelDato("Código Ticket: " + codigoTicket);
        lblSolicitante = crearLabelDato("Solicitante: -");
        lblPrioridad = crearLabelDato("Prioridad: -");
        lblEstado = crearLabelDato("Estado: -");
        lblFechaCreacion = crearLabelDato("Fecha Creación: -");
        cardInfo.add(lblCodigoGeneral);
        cardInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        cardInfo.add(lblSolicitante);
        cardInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        cardInfo.add(lblPrioridad);
        cardInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        cardInfo.add(lblEstado);
        cardInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        cardInfo.add(lblFechaCreacion);

        // 2. Tarjeta Asignación
        JPanel cardAsig = crearTarjeta("Asignación");
        lblTecnico = crearLabelDato("Técnico: Sin Asignar");
        lblCategoria = crearLabelDato("Categoría: -");
        cardAsig.add(lblTecnico);
        cardAsig.add(Box.createRigidArea(new Dimension(0, 6)));
        cardAsig.add(lblCategoria);

        // 3. Tarjeta Detalles del Incidente (Título + Descripción)
        JPanel cardDetalles = crearTarjeta("Detalles del Ticket");
        lblTituloDetalle = new JLabel("Título: -");
        lblTituloDetalle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTituloDetalle.setForeground(Color.WHITE);
        lblTituloDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtDescripcion = new JTextArea();
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setFocusable(false);
        txtDescripcion.setBackground(Color.decode("#3B4252"));
        txtDescripcion.setForeground(Color.decode("#E5E9F0"));
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescripcion.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(null);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardDetalles.add(lblTituloDetalle);
        cardDetalles.add(Box.createRigidArea(new Dimension(0, 8)));
        cardDetalles.add(scrollDesc);

        // 4. Tarjeta Registro de Actividad (Bitácora)
        JPanel cardRegistro = crearTarjeta("Registro de Actividad");
        panelHistorialLista = new JPanel();
        panelHistorialLista.setLayout(new BoxLayout(panelHistorialLista, BoxLayout.Y_AXIS));
        panelHistorialLista.setOpaque(false);

        JScrollPane scrollHistorial = new JScrollPane(panelHistorialLista);
        scrollHistorial.setOpaque(false);
        scrollHistorial.getViewport().setOpaque(false);
        scrollHistorial.setBorder(null);
        cardRegistro.add(scrollHistorial);

        // 5. Tarjeta Última Resolución / Comentario Técnico
        JPanel cardComentarios = crearTarjeta("Última Actualización");
        txtUltimoComentario = new JTextArea("Sin observaciones registradas.");
        txtUltimoComentario.setWrapStyleWord(true);
        txtUltimoComentario.setLineWrap(true);
        txtUltimoComentario.setEditable(false);
        txtUltimoComentario.setFocusable(false);
        txtUltimoComentario.setBackground(Color.decode("#3B4252"));
        txtUltimoComentario.setForeground(Color.WHITE);
        txtUltimoComentario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtUltimoComentario.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane scrollComentario = new JScrollPane(txtUltimoComentario);
        scrollComentario.setBorder(null);
        cardComentarios.add(scrollComentario);

        panelGrid.add(cardInfo);
        panelGrid.add(cardAsig);
        panelGrid.add(cardDetalles);
        panelGrid.add(cardRegistro);
        panelGrid.add(cardComentarios);

        panelFondo.add(panelHeader, BorderLayout.NORTH);
        panelFondo.add(panelGrid, BorderLayout.CENTER);

        add(panelFondo);
    }

    private JPanel crearTarjeta(String titulo) {
        JPanel card = new PanelRedondeado(18, Color.decode("#4C566A"));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        return card;
    }

    private JLabel crearLabelDato(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(Color.decode("#E5E9F0"));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void cargarInformacionTicket() {
        if (codigoTicket == null || codigoTicket.trim().isEmpty()) return;

        Ticket ticket = ctrlTicket.obtenerPorCodigo(codigoTicket);

        if (ticket != null) {
            lblCodigoGeneral.setText("Código Ticket: " + (ticket.getCodigoUnico() != null ? ticket.getCodigoUnico() : codigoTicket));
            lblSolicitante.setText("Solicitante: " + (ticket.getNombreSolicitante() != null ? ticket.getNombreSolicitante() : "N/A"));
            lblPrioridad.setText("Prioridad: " + ticket.getPrioridad());
            lblEstado.setText("Estado: " + (ticket.getNombreEstado() != null ? ticket.getNombreEstado() : "N/A"));
            lblFechaCreacion.setText("Fecha Creación: " + (ticket.getFechaCreacion() != null ? ticket.getFechaCreacion().toString().substring(0, 10) : "N/A"));

            lblTecnico.setText("Técnico: " + (ticket.getNombreTecnico() != null ? ticket.getNombreTecnico() : "Sin Asignar"));
            lblCategoria.setText("Categoría: " + (ticket.getNombreCategoria() != null ? ticket.getNombreCategoria() : "N/A"));

            lblTituloDetalle.setText("Título: " + (ticket.getTitulo() != null ? ticket.getTitulo() : "Sin título"));
            txtDescripcion.setText(ticket.getDescripcion() != null ? ticket.getDescripcion() : "Sin descripción detallada.");
            txtDescripcion.setCaretPosition(0);

            // Cargar historial
            List<HistorialTicket> historial = ctrlTicket.obtenerHistorial(ticket.getIdTicket());
            panelHistorialLista.removeAll();

            if (historial == null || historial.isEmpty()) {
                panelHistorialLista.add(crearLabelDato("Solicitud Recibida (Automatizado)"));
            } else {
                for (HistorialTicket h : historial) {
                    JLabel lblAccion = new JLabel("• " + h.getComentario());
                    lblAccion.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    lblAccion.setForeground(Color.WHITE);

                    String fechaStr = h.getFechaCambio() != null ? h.getFechaCambio().toString().substring(0, 10) : "";
                    JLabel lblFecha = new JLabel("   " + fechaStr);
                    lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    lblFecha.setForeground(Color.decode("#D8DEE9"));

                    panelHistorialLista.add(lblAccion);
                    panelHistorialLista.add(lblFecha);
                    panelHistorialLista.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                // Mostrar el último comentario registrado en la tarjeta de Última Actualización
                HistorialTicket ultimo = historial.get(historial.size() - 1);
                txtUltimoComentario.setText(ultimo.getComentario());
            }
            panelHistorialLista.revalidate();
            panelHistorialLista.repaint();
        }
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