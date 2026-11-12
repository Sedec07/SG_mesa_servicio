package View;

import Controller.CtrlTicket;
import com.formdev.flatlaf.FlatClientProperties;
import controller.CtrlSessionManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import model.Usuario;

public class FrmCierreTicket extends JDialog {

    private final int idTicket;
    private final String codigoTicket;
    private final String tituloTicket;

    private JTextField txtCodigo;
    private JTextField txtSolicitante;
    private JTextField txtTitulo;
    private JTextArea txtComentario;
    private JButton btnCancelar;
    private JButton btnCerrarTicket;

    private final CtrlTicket ctrlTicket;
    private final Runnable callbackRefrescarTabla;

    public FrmCierreTicket(Frame parent, int idTicket, String codigoTicket, String tituloTicket, Runnable callbackRefrescarTabla) {
        super(parent, "Mesa de Servicio TI — Gestión de Tickets Individuales", true);
        this.idTicket = idTicket;
        this.codigoTicket = codigoTicket;
        this.tituloTicket = tituloTicket;
        this.ctrlTicket = new CtrlTicket();
        this.callbackRefrescarTabla = callbackRefrescarTabla;

        initComponents();
        configurarEstilosFigma();
        cargarDatos();
    }

    private void initComponents() {
        setSize(850, 520);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel panelFondo = new JPanel(new GridBagLayout());
        panelFondo.setBackground(Color.decode("#3B4252"));

        // Header Superior
        JLabel lblTituloHeader = new JLabel("CIERRE DE TICKET: " + codigoTicket, SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloHeader.setForeground(Color.WHITE);

        JLabel lblSubtituloHeader = new JLabel("Validación de solución y finalización del requerimiento.", SwingConstants.CENTER);
        lblSubtituloHeader.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtituloHeader.setForeground(Color.decode("#D8DEE9"));

        // Tarjeta Principal Slate
        JPanel cardForm = new JPanel(new GridBagLayout());
        cardForm.setBackground(Color.decode("#4C566A"));
        cardForm.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Columna Izquierda (Solo lectura) ---
        JLabel lblCod = crearLabelCampo("Código del Ticket (Auto-generado)");
        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);
        txtCodigo.setPreferredSize(new Dimension(300, 38));

        JLabel lblSolicitante = crearLabelCampo("Solicitante");
        txtSolicitante = new JTextField();
        txtSolicitante.setEditable(false);
        txtSolicitante.setPreferredSize(new Dimension(300, 38));

        JLabel lblTituloInc = crearLabelCampo("Titulo del Incidente");
        txtTitulo = new JTextField();
        txtTitulo.setEditable(false);
        txtTitulo.setPreferredSize(new Dimension(300, 38));

        // --- Columna Derecha ---
        JLabel lblComentario = crearLabelCampo("Comentarios de Conformidad");
        txtComentario = new JTextArea(6, 20);
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        JScrollPane scrollComentario = new JScrollPane(txtComentario);
        scrollComentario.setPreferredSize(new Dimension(340, 140));

        // --- Botones Inferiores ---
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(110, 36));

        btnCerrarTicket = new JButton("Cerrar Ticket");
        btnCerrarTicket.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarTicket.setPreferredSize(new Dimension(130, 36));

        btnCancelar.addActionListener((ActionEvent e) -> dispose());
        btnCerrarTicket.addActionListener((ActionEvent e) -> procesarCierreTicket());

        // Layout Columna Izquierda
        gbc.gridx = 0; gbc.gridy = 0; cardForm.add(lblCod, gbc);
        gbc.gridy = 1; cardForm.add(txtCodigo, gbc);

        gbc.gridy = 2; cardForm.add(lblSolicitante, gbc);
        gbc.gridy = 3; cardForm.add(txtSolicitante, gbc);

        gbc.gridy = 4; cardForm.add(lblTituloInc, gbc);
        gbc.gridy = 5; cardForm.add(txtTitulo, gbc);

        // Layout Columna Derecha
        gbc.gridx = 1; gbc.gridy = 0; cardForm.add(lblComentario, gbc);
        gbc.gridy = 1; gbc.gridheight = 5; gbc.fill = GridBagConstraints.BOTH;
        cardForm.add(scrollComentario, gbc);

        // Layout Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnCerrarTicket);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.gridheight = 1;
        gbc.insets = new Insets(15, 10, 0, 10);
        cardForm.add(panelBotones, gbc);

        // Layout General
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0; gbcMain.gridy = 0;
        gbcMain.insets = new Insets(15, 0, 5, 0);
        panelFondo.add(lblTituloHeader, gbcMain);

        gbcMain.gridy = 1;
        gbcMain.insets = new Insets(0, 0, 20, 0);
        panelFondo.add(lblSubtituloHeader, gbcMain);

        gbcMain.gridy = 2;
        gbcMain.insets = new Insets(0, 20, 20, 20);
        panelFondo.add(cardForm, gbcMain);

        add(panelFondo);
    }

    private JLabel crearLabelCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private void configurarEstilosFigma() {
        txtCodigo.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #D8DEE9");
        txtSolicitante.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #D8DEE9");
        txtTitulo.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #D8DEE9");
        txtComentario.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #FFFFFF; margin: 8,10,8,10");

        txtComentario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese los comentarios o detalles finales sobre la conformidad del servicio recibido...");

        btnCancelar.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnCerrarTicket.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 12 Segoe UI");
    }

    private void cargarDatos() {
        txtCodigo.setText(codigoTicket);
        txtTitulo.setText(tituloTicket);
        Usuario solicitante = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        if (solicitante != null) {
            txtSolicitante.setText(solicitante.getNombre());
        }
    }

    private void procesarCierreTicket() {
    String comentario = txtComentario.getText().trim();
    Usuario usuarioAccion = CtrlSessionManager.getInstance().getUsuarioAutenticado();

    if (comentario.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor ingrese un comentario de conformidad para cerrar el ticket.", "Comentario Requerido", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (usuarioAccion == null) return;

    // Estado origen 3 = RESUELTO -> Estado destino 5 = CERRADO
    boolean actualizado = ctrlTicket.cambiarEstadoTicket(
        idTicket, 
        3, // Estado anterior esperado: RESUELTO
        5, // Estado nuevo: CERRADO
        usuarioAccion.getIdUsuario(), 
        "Cierre por conformidad del usuario: " + comentario
    );

    if (actualizado) {
        JOptionPane.showMessageDialog(this, "El ticket " + codigoTicket + " ha sido cerrado con éxito.", "Ticket Finalizado", JOptionPane.INFORMATION_MESSAGE);
        if (callbackRefrescarTabla != null) {
            callbackRefrescarTabla.run();
        }
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "No se puede cerrar un ticket que no esté en estado RESUELTO.", "Operación No Permitida", JOptionPane.ERROR_MESSAGE);
    }
}
}