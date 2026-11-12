package View;

import Controller.CtrlTicket;
import com.formdev.flatlaf.FlatClientProperties;
import controller.CtrlCategoria;
import controller.CtrlSessionManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import model.Categoria;
import model.Usuario;

public class FrmNuevoTicket extends JDialog {

    private JComboBox<CategoriaItem> cbCategoria;
    private JTextField txtSolicitante;
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JButton btnCerrar;
    private JButton btnCrearTicket;

    private final CtrlCategoria ctrlCategoria;
    private final CtrlTicket ctrlTicket;
    private final Runnable callbackRefrescarTabla;

    public FrmNuevoTicket(Frame parent, Runnable callbackRefrescarTabla) {
        super(parent, "Mesa de Servicio TI — Gestión de Tickets Individuales", true);
        this.ctrlCategoria = new CtrlCategoria();
        this.ctrlTicket = new CtrlTicket();
        this.callbackRefrescarTabla = callbackRefrescarTabla;

        initComponents();
        configurarEstilosFigma();
        cargarCategorias();
        cargarDatosSolicitante();
    }

    private void initComponents() {
        setSize(850, 520);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel panelFondo = new JPanel(new GridBagLayout());
        panelFondo.setBackground(Color.decode("#3B4252"));

        // Encabezado Superior
        JLabel lblTituloHeader = new JLabel("Nuevo Requerimiento de TI", SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloHeader.setForeground(Color.WHITE);

        JLabel lblSubtituloHeader = new JLabel("Complete los campos para registrar un nuevo incidente o solicitud en el sistema.", SwingConstants.CENTER);
        lblSubtituloHeader.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtituloHeader.setForeground(Color.decode("#D8DEE9"));

        // Tarjeta Principal Slate
        JPanel cardForm = new JPanel(new GridBagLayout());
        cardForm.setBackground(Color.decode("#4C566A"));
        cardForm.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Columna Izquierda ---
        JLabel lblCat = crearLabelCampo("Categoria");
        cbCategoria = new JComboBox<>();
        cbCategoria.setPreferredSize(new Dimension(300, 38));

        JLabel lblSolicitante = crearLabelCampo("Solicitante 🔒");
        txtSolicitante = new JTextField();
        txtSolicitante.setEditable(false);
        txtSolicitante.setPreferredSize(new Dimension(300, 38));

        JLabel lblTituloInc = crearLabelCampo("Titulo del Incidente");
        txtTitulo = new JTextField();
        txtTitulo.setPreferredSize(new Dimension(300, 38));

        // --- Columna Derecha ---
        JLabel lblDesc = crearLabelCampo("Descripción Detallada");
        txtDescripcion = new JTextArea(6, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(340, 140));

        // --- Botones de Acción ---
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(100, 36));

        btnCrearTicket = new JButton("Crear Ticket");
        btnCrearTicket.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCrearTicket.setPreferredSize(new Dimension(130, 36));

        btnCerrar.addActionListener((ActionEvent e) -> dispose());
        btnCrearTicket.addActionListener((ActionEvent e) -> guardarTicket());

        // Posicionamiento Columna Izquierda
        gbc.gridx = 0; gbc.gridy = 0; cardForm.add(lblCat, gbc);
        gbc.gridy = 1; cardForm.add(cbCategoria, gbc);

        gbc.gridy = 2; cardForm.add(lblSolicitante, gbc);
        gbc.gridy = 3; cardForm.add(txtSolicitante, gbc);

        gbc.gridy = 4; cardForm.add(lblTituloInc, gbc);
        gbc.gridy = 5; cardForm.add(txtTitulo, gbc);

        // Posicionamiento Columna Derecha
        gbc.gridx = 1; gbc.gridy = 0; cardForm.add(lblDesc, gbc);
        gbc.gridy = 1; gbc.gridheight = 5; gbc.fill = GridBagConstraints.BOTH;
        cardForm.add(scrollDesc, gbc);

        // Panel de Botones Inferior Derecha
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);
        panelBotones.add(btnCerrar);
        panelBotones.add(btnCrearTicket);

        gbc.gridx = 1; gbc.gridy = 6; gbc.gridheight = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
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
        cbCategoria.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #FFFFFF");
        txtSolicitante.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #D8DEE9");
        txtTitulo.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #FFFFFF; margin: 5,10,5,10");
        txtDescripcion.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #FFFFFF; margin: 8,10,8,10");

        txtTitulo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ej: Error de conexión a la base de datos MariaDB");
        txtDescripcion.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Describa detalladamente el inconveniente presentado, pasos para reproducirlo y cualquier detalle técnico relevante para el soporte...");

        btnCerrar.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnCrearTicket.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #1865C9; foreground: #FFFFFF; font: bold 12 Segoe UI");
    }

    private void cargarCategorias() {
        cbCategoria.removeAllItems();
        cbCategoria.addItem(new CategoriaItem(0, "Seleccione la Categoria"));
        List<Categoria> lista = ctrlCategoria.obtenerCategorias();
        for (Categoria c : lista) {
            cbCategoria.addItem(new CategoriaItem(c.getIdCategoria(), c.getNombre()));
        }
    }

    private void cargarDatosSolicitante() {
        Usuario solicitante = CtrlSessionManager.getInstance().getUsuarioAutenticado();
        if (solicitante != null) {
            txtSolicitante.setText(solicitante.getNombre());
        }
    }

    private void guardarTicket() {
        CategoriaItem itemCat = (CategoriaItem) cbCategoria.getSelectedItem();
        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        Usuario solicitante = CtrlSessionManager.getInstance().getUsuarioAutenticado();

        if (itemCat == null || itemCat.getId() == 0 || titulo.isEmpty() || descripcion.isEmpty() || solicitante == null) {
            JOptionPane.showMessageDialog(this, "Por favor complete la categoría, el título y la descripción del incidente.", "Campos obligatorios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean exito = ctrlTicket.crearTicketConBalanceo(titulo, descripcion, "Media", itemCat.getId(), solicitante.getIdUsuario());

        if (exito) {
            JOptionPane.showMessageDialog(this, "El ticket ha sido registrado correctamente con asignación automática.", "Ticket Creado", JOptionPane.INFORMATION_MESSAGE);
            if (callbackRefrescarTabla != null) {
                callbackRefrescarTabla.run();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar el ticket en la base de datos.", "Error de Guardado", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clase auxiliar para los items del Combobox
    private static class CategoriaItem {
        private final int id;
        private final String nombre;

        public CategoriaItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() { return id; }

        @Override
        public String toString() { return nombre; }
    }
}