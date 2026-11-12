package View;

import com.formdev.flatlaf.FlatClientProperties;
import controller.CtrlSessionManager;
import Controller.CtrlTicket;
import Controller.CtrlUsuario;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import model.Usuario;

public class FrmEscalarTicket extends JDialog {

    private final int idTicket;
    private final String codigoTicket;
    private final String solicitante;

    private JTextField txtCodigo;
    private JTextField txtSolicitante;
    private JComboBox<TecnicoItem> cbTecnicoEspecialista;
    private JTextArea txtMotivo;

    private JButton btnCancelar;
    private JButton btnConfirmar;

    private final CtrlTicket ctrlTicket;
    private final CtrlUsuario ctrlUsuario;
    private final Runnable callbackRefrescar;

    public FrmEscalarTicket(Frame parent, int idTicket, String codigoTicket, String solicitante, Runnable callbackRefrescar) {
        super(parent, "Mesa de Servicio TI - Panel Operativo del Técnico", true);
        this.idTicket = idTicket;
        this.codigoTicket = codigoTicket;
        this.solicitante = solicitante;
        this.ctrlTicket = new CtrlTicket();
        this.ctrlUsuario = new CtrlUsuario();
        this.callbackRefrescar = callbackRefrescar;

        initComponents();
        configurarEstilosFigma();
        cargarDatosIniciales();
        cargarTecnicosDisponibles();
    }

    private void initComponents() {
        setSize(860, 580);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel panelFondo = new JPanel(new BorderLayout(0, 20));
        panelFondo.setBackground(Color.decode("#3B4252"));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(25, 65, 30, 65));

        // --- Header Superior ---
        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 0, 5));
        panelHeader.setOpaque(false);

        JLabel lblTituloHeader = new JLabel("Escalamiento de Ticket: " + codigoTicket, SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloHeader.setForeground(Color.WHITE);

        JLabel lblSubtituloHeader = new JLabel("Reasignar requerimiento a otro técnico de soporte.", SwingConstants.CENTER);
        lblSubtituloHeader.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtituloHeader.setForeground(Color.decode("#D8DEE9"));

        panelHeader.add(lblTituloHeader);
        panelHeader.add(lblSubtituloHeader);

        // --- Tarjeta Principal ---
        JPanel cardForm = new PanelRedondeado(18, Color.decode("#4C566A"));
        cardForm.setLayout(new GridBagLayout());
        cardForm.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0-1: Código y Solicitante
        JLabel lblCod = crearLabelCampo("Código del Ticket");
        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);
        txtCodigo.setPreferredSize(new Dimension(0, 38));

        JLabel lblSol = crearLabelCampo("Solicitante");
        txtSolicitante = new JTextField();
        txtSolicitante.setEditable(false);
        txtSolicitante.setPreferredSize(new Dimension(0, 38));

        // Fila 2-3: Técnico Asignado
        JLabel lblTecnicoEsp = crearLabelCampo("Técnico Asignado para Escalamiento");
        cbTecnicoEspecialista = new JComboBox<>();
        cbTecnicoEspecialista.setPreferredSize(new Dimension(0, 38));

        // Fila 4-5: Motivo del Escalamiento
        JLabel lblMotivo = crearLabelCampo("Motivo del Escalamiento");
        txtMotivo = new JTextArea();
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        scrollMotivo.setBorder(BorderFactory.createEmptyBorder());

        // Fila 6: Botones
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(115, 38));

        btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.setPreferredSize(new Dimension(125, 38));

        btnCancelar.addActionListener((ActionEvent e) -> dispose());
        btnConfirmar.addActionListener((ActionEvent e) -> procesarEscalamiento());

        // Posicionamiento Columna Izquierda (weightx = 0.5)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 0, 5, 12);
        cardForm.add(lblCod, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 12);
        cardForm.add(txtCodigo, gbc);

        // Posicionamiento Columna Derecha (weightx = 0.5)
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.insets = new Insets(0, 12, 5, 0);
        cardForm.add(lblSol, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 12, 15, 0);
        cardForm.add(txtSolicitante, gbc);

        // Técnico (Ancho completo)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 5, 0);
        cardForm.add(lblTecnicoEsp, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        cardForm.add(cbTecnicoEspecialista, gbc);

        // Motivo (Ancho completo + expansión vertical weighty = 1.0)
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 5, 0);
        cardForm.add(lblMotivo, gbc);

        gbc.gridy = 5; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 18, 0);
        cardForm.add(scrollMotivo, gbc);

        // Botones Inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnConfirmar);

        gbc.gridy = 6; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardForm.add(panelBotones, gbc);

        panelFondo.add(panelHeader, BorderLayout.NORTH);
        panelFondo.add(cardForm, BorderLayout.CENTER);

        add(panelFondo);
    }

    private JLabel crearLabelCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private void configurarEstilosFigma() {
        txtCodigo.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #D8DEE9; margin: 0,10,0,10");
        txtSolicitante.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #D8DEE9; margin: 0,10,0,10");
        cbTecnicoEspecialista.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #FFFFFF");

        txtMotivo.setBackground(Color.decode("#3B4252"));
        txtMotivo.setForeground(Color.WHITE);
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMotivo.setMargin(new Insets(10, 12, 10, 12));
        txtMotivo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Describa detalladamente el motivo del escalamiento y el diagnóstico realizado...");

        btnCancelar.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnConfirmar.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #D97706; foreground: #FFFFFF; font: bold 12 Segoe UI");
    }

    private void cargarDatosIniciales() {
        txtCodigo.setText(codigoTicket);
        txtSolicitante.setText(solicitante);
    }

    private void cargarTecnicosDisponibles() {
        cbTecnicoEspecialista.removeAllItems();
        cbTecnicoEspecialista.addItem(new TecnicoItem(0, "Seleccione un técnico disponible..."));

        Usuario actual = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        List<Usuario> usuarios = ctrlUsuario.obtenerTodos();

        for (Usuario u : usuarios) {
            boolean esTecnicoActivo = (u.getIdRol() == 2) && "ACTIVO".equalsIgnoreCase(u.getEstado());
            boolean noEsElMismo = (actual == null) || (u.getIdUsuario() != actual.getIdUsuario());

            if (esTecnicoActivo && noEsElMismo) {
                cbTecnicoEspecialista.addItem(new TecnicoItem(u.getIdUsuario(), u.getNombre() + " (" + u.getCorreo() + ")"));
            }
        }
    }

    private void procesarEscalamiento() {
        TecnicoItem seleccionado = (TecnicoItem) cbTecnicoEspecialista.getSelectedItem();
        String motivo = txtMotivo.getText().trim();
        Usuario actual = CtrlSessionManager.getInstance().getUsuarioAutenticado();

        if (seleccionado == null || seleccionado.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione el técnico al que desea escalar el ticket.", "Técnico Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (motivo.isEmpty() || actual == null) {
            JOptionPane.showMessageDialog(this, "Por favor describa el motivo del escalamiento.", "Motivo Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String traza = "Escalado a " + seleccionado.getNombre() + ". Motivo: " + motivo;
        boolean reasignado = ctrlTicket.reasignarTecnico(idTicket, seleccionado.getId());
        boolean estadoActualizado = ctrlTicket.cambiarEstadoTicket(idTicket, 2, 3, actual.getIdUsuario(), traza);

        if (reasignado && estadoActualizado) {
            JOptionPane.showMessageDialog(this, "Ticket " + codigoTicket + " escalado exitosamente.", "Escalamiento Confirmado", JOptionPane.INFORMATION_MESSAGE);
            if (callbackRefrescar != null) {
                callbackRefrescar.run();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo completar el escalamiento en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class TecnicoItem {
        private final int id;
        private final String nombre;

        public TecnicoItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }

        @Override
        public String toString() { return nombre; }
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