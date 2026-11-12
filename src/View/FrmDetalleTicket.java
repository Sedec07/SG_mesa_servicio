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

    private JLabel lblSolicitante;
    private JLabel lblPrioridad;
    private JLabel lblFechaCreacion;
    private JLabel lblCodigoGeneral;

    private JLabel lblTecnico;
    private JLabel lblCategoria;

    private JLabel lblTituloDetalle;
    private JLabel lblPrioridadDetalle;
    private JLabel lblFechaDetalle;
    private JLabel lblCodigoDetalle;

    private JPanel panelHistorialLista;
    private JTextArea txtComentario;

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

        // Grilla Principal de 5 Tarjetas Redondeadas
        JPanel panelGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        panelGrid.setOpaque(false);

        // 1. Tarjeta Información General
        JPanel cardInfo = crearTarjeta("Informacion General");
        lblSolicitante = crearLabelDato("Solicitante: -");
        lblPrioridad = crearLabelDato("Prioridad: -");
        lblFechaCreacion = crearLabelDato("Fecha Creacion: -");
        lblCodigoGeneral = crearLabelDato("Codigo Ticket: " + codigoTicket);
        cardInfo.add(lblSolicitante);
        cardInfo.add(lblPrioridad);
        cardInfo.add(lblFechaCreacion);
        cardInfo.add(lblCodigoGeneral);

        // 2. Tarjeta Asignación
        JPanel cardAsig = crearTarjeta("Asignacion");
        lblTecnico = crearLabelDato("Tecnico: Sin Asignar");
        lblCategoria = crearLabelDato("Categoria: -");
        cardAsig.add(lblTecnico);
        cardAsig.add(lblCategoria);

        // 3. Tarjeta Detalles Adicionales
        JPanel cardDetalles = crearTarjeta("Detalles Adicionales");
        lblTituloDetalle = crearLabelDato("Titulo: -");
        lblPrioridadDetalle = crearLabelDato("Prioridad: -");
        lblFechaDetalle = crearLabelDato("Fecha Creacion: -");
        lblCodigoDetalle = crearLabelDato("Codigo Ticket: " + codigoTicket);
        cardDetalles.add(lblTituloDetalle);
        cardDetalles.add(lblPrioridadDetalle);
        cardDetalles.add(lblFechaDetalle);
        cardDetalles.add(lblCodigoDetalle);

        // 4. Tarjeta Registro de Actividad
        JPanel cardRegistro = crearTarjeta("Registro de Actividad");
        panelHistorialLista = new JPanel();
        panelHistorialLista.setLayout(new BoxLayout(panelHistorialLista, BoxLayout.Y_AXIS));
        panelHistorialLista.setOpaque(false);

        JScrollPane scrollHistorial = new JScrollPane(panelHistorialLista);
        scrollHistorial.setOpaque(false);
        scrollHistorial.getViewport().setOpaque(false);
        scrollHistorial.setBorder(null);
        cardRegistro.add(scrollHistorial);

        // 5. Tarjeta Comentarios con cuadro redondeado
        JPanel cardComentarios = crearTarjeta("Comentario");
        txtComentario = new JTextArea();
        txtComentario.setWrapStyleWord(true);
        txtComentario.setLineWrap(true);
        txtComentario.setEditable(false);
        txtComentario.setFocusable(false);
        txtComentario.setBackground(Color.decode("#3B4252"));
        txtComentario.setForeground(Color.WHITE);
        txtComentario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtComentario.putClientProperty(FlatClientProperties.STYLE, "arc: 12; margin: 8,8,8,8");

        JScrollPane scrollComentario = new JScrollPane(txtComentario);
        scrollComentario.setOpaque(false);
        scrollComentario.getViewport().setOpaque(false);
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
        // Panel personalizado con esquinas redondeadas
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
            lblSolicitante.setText("Solicitante: " + (ticket.getNombreSolicitante() != null ? ticket.getNombreSolicitante() : "N/A"));
            lblPrioridad.setText("Prioridad: " + ticket.getPrioridad());
            lblFechaCreacion.setText("Fecha Creacion: " + (ticket.getFechaCreacion() != null ? ticket.getFechaCreacion().toString().substring(0, 10) : "N/A"));
            lblCodigoGeneral.setText("Codigo Ticket: " + (ticket.getCodigoUnico() != null ? ticket.getCodigoUnico() : codigoTicket));

            lblTecnico.setText("Tecnico: " + (ticket.getNombreTecnico() != null ? ticket.getNombreTecnico() : "Sin Asignar"));
            lblCategoria.setText("Categoria: " + (ticket.getNombreCategoria() != null ? ticket.getNombreCategoria() : "N/A"));

            lblTituloDetalle.setText("Titulo: " + ticket.getTitulo());
            lblPrioridadDetalle.setText("Prioridad: " + ticket.getPrioridad());
            lblFechaDetalle.setText("Fecha Creacion: " + (ticket.getFechaCreacion() != null ? ticket.getFechaCreacion().toString().substring(0, 10) : "N/A"));
            lblCodigoDetalle.setText("Codigo Ticket: " + (ticket.getCodigoUnico() != null ? ticket.getCodigoUnico() : codigoTicket));

            txtComentario.setText(ticket.getDescripcion());

            // Cargar historial con el ID numérico
            List<HistorialTicket> historial = ctrlTicket.obtenerHistorial(ticket.getIdTicket());
            panelHistorialLista.removeAll();

            if (historial == null || historial.isEmpty()) {
                panelHistorialLista.add(crearLabelDato("Solicitud Recibida (Automatizado)"));
            } else {
                for (HistorialTicket h : historial) {
                    JLabel lblAccion = new JLabel(h.getComentario());
                    lblAccion.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    lblAccion.setForeground(Color.WHITE);

                    JLabel lblFecha = new JLabel(h.getFechaCambio() != null ? h.getFechaCambio().toString() : "");
                    lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    lblFecha.setForeground(Color.decode("#D8DEE9"));

                    panelHistorialLista.add(lblAccion);
                    panelHistorialLista.add(lblFecha);
                    panelHistorialLista.add(Box.createRigidArea(new Dimension(0, 6)));
                }
            }
            panelHistorialLista.revalidate();
            panelHistorialLista.repaint();
        }
    }

    // --- Componente de Panel con Esquinas Redondeadas Smooth ---
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