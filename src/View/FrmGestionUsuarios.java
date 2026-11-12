package View;

import com.formdev.flatlaf.FlatClientProperties;
import Controller.CtrlUsuario;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Usuario;

public class FrmGestionUsuarios extends JDialog {

    private JTable tablaUsuarios;
    private DefaultTableModel modelTabla;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JComboBox<String> cbRol;
    private JComboBox<String> cbEstado;

    private JButton btnGuardar;
    private JButton btnInactivar;
    private JButton btnLimpiar;
    private JButton btnCerrar;

    private final CtrlUsuario ctrlUsuario;
    private Integer idUsuarioSeleccionado = null;

    public FrmGestionUsuarios(Frame parent) {
        super(parent, "Mesa de Servicio TI — Administración de Usuarios", true);
        this.ctrlUsuario = new CtrlUsuario();

        initComponents();
        configurarEstilosFigma();
        cargarUsuarios();
    }

    private void initComponents() {
        setSize(950, 600);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel panelFondo = new JPanel(new BorderLayout(15, 15));
        panelFondo.setBackground(Color.decode("#3B4252"));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // --- Header Superior ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Gestión de Usuarios y Roles");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(btnCerrar, BorderLayout.EAST);

        // --- Formulario Superior ---
        JPanel cardForm = new PanelRedondeado(16, Color.decode("#4C566A"));
        cardForm.setLayout(new GridBagLayout());
        cardForm.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNom = crearLabelCampo("Nombre Completo");
        txtNombre = new JTextField(15);

        JLabel lblEmail = crearLabelCampo("Correo Electrónico");
        txtCorreo = new JTextField(15);

        JLabel lblPass = crearLabelCampo("Contraseña");
        txtContrasena = new JPasswordField(12);

        JLabel lblRol = crearLabelCampo("Rol Asignado");
        cbRol = new JComboBox<>(new String[]{"Administrador", "Técnico", "Usuario"});

        JLabel lblEst = crearLabelCampo("Estado");
        cbEstado = new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});
        cbEstado.setEnabled(false); // Al crear siempre inicia ACTIVO

        btnGuardar = new JButton("Guardar Usuario");
        btnInactivar = new JButton("Inactivar");
        btnLimpiar = new JButton("Limpiar");

        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInactivar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnGuardar.addActionListener(e -> guardarUsuario());
        btnInactivar.addActionListener(e -> inactivarUsuarioSeleccionado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; cardForm.add(lblNom, gbc);
        gbc.gridx = 1; cardForm.add(txtNombre, gbc);
        gbc.gridx = 2; cardForm.add(lblEmail, gbc);
        gbc.gridx = 3; cardForm.add(txtCorreo, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; cardForm.add(lblPass, gbc);
        gbc.gridx = 1; cardForm.add(txtContrasena, gbc);
        gbc.gridx = 2; cardForm.add(lblRol, gbc);
        gbc.gridx = 3; cardForm.add(cbRol, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; cardForm.add(lblEst, gbc);
        gbc.gridx = 1; cardForm.add(cbEstado, gbc);

        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesForm.setOpaque(false);
        panelBotonesForm.add(btnLimpiar);
        panelBotonesForm.add(btnInactivar);
        panelBotonesForm.add(btnGuardar);

        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2;
        cardForm.add(panelBotonesForm, gbc);

        // --- Tabla de Usuarios ---
        String[] columnas = {"ID", "Nombre", "Correo Electrónico", "Rol", "Estado"};
        modelTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaUsuarios = new JTable(modelTabla);
        tablaUsuarios.setRowHeight(32);
        tablaUsuarios.setBackground(Color.decode("#434C5E"));
        tablaUsuarios.setForeground(Color.WHITE);
        tablaUsuarios.setSelectionBackground(Color.decode("#2E6B9E"));
        tablaUsuarios.getTableHeader().setBackground(Color.decode("#4C566A"));
        tablaUsuarios.getTableHeader().setForeground(Color.WHITE);
        tablaUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);
        scrollTabla.getViewport().setBackground(Color.decode("#3B4252"));
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());

        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaUsuarios.getSelectedRow() != -1) {
                cargarUsuarioSeleccionado();
            }
        });

        // Layout Principal
        JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
        panelCentro.setOpaque(false);
        panelCentro.add(cardForm, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        panelFondo.add(panelHeader, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);

        add(panelFondo);
    }

    private JLabel crearLabelCampo(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private void configurarEstilosFigma() {
        btnCerrar.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #4C566A; foreground: #D8DEE9; font: bold 12 Segoe UI");

        txtNombre.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3B4252; foreground: #FFFFFF; margin: 4,8,4,8");
        txtCorreo.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3B4252; foreground: #FFFFFF; margin: 4,8,4,8");
        txtContrasena.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3B4252; foreground: #FFFFFF; margin: 4,8,4,8");
        txtContrasena.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Dejar vacío para no cambiar");
        cbRol.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3B4252; foreground: #FFFFFF");
        cbEstado.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #3B4252; foreground: #FFFFFF");

        btnLimpiar.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #4C566A; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnInactivar.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #DC2626; foreground: #FFFFFF; font: bold 12 Segoe UI");
        btnGuardar.putClientProperty(FlatClientProperties.STYLE, "arc: 10; background: #1865C9; foreground: #FFFFFF; font: bold 12 Segoe UI");
    }

    private void cargarUsuarios() {
        modelTabla.setRowCount(0);
        List<Usuario> lista = ctrlUsuario.obtenerTodos();
        for (Usuario u : lista) {
            String rolStr = u.getIdRol() == 1 ? "Administrador" : (u.getIdRol() == 2 ? "Técnico" : "Usuario");
            modelTabla.addRow(new Object[]{
                u.getIdUsuario(),
                u.getNombre(),
                u.getCorreo(),
                rolStr,
                u.getEstado()
            });
        }
    }

    private void cargarUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila != -1) {
            idUsuarioSeleccionado = (Integer) modelTabla.getValueAt(fila, 0);
            txtNombre.setText((String) modelTabla.getValueAt(fila, 1));
            txtCorreo.setText((String) modelTabla.getValueAt(fila, 2));
            String rolStr = (String) modelTabla.getValueAt(fila, 3);
            String estadoStr = (String) modelTabla.getValueAt(fila, 4);

            if ("Administrador".equalsIgnoreCase(rolStr)) cbRol.setSelectedIndex(0);
            else if ("Técnico".equalsIgnoreCase(rolStr)) cbRol.setSelectedIndex(1);
            else cbRol.setSelectedIndex(2);

            cbEstado.setSelectedItem(estadoStr != null ? estadoStr : "ACTIVO");
            cbEstado.setEnabled(true);

            txtContrasena.setText("");
            btnGuardar.setText("Actualizar Usuario");
        }
    }

    private void guardarUsuario() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtContrasena.getPassword()).trim();
        int idRol = cbRol.getSelectedIndex() + 1;

        if (nombre.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y correo son obligatorios.", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean exito;
        if (idUsuarioSeleccionado == null) {
            if (pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese la contraseña para crear un nuevo usuario.", "Contraseña Requerida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            exito = ctrlUsuario.registrarUsuario(nombre, correo, pass, idRol);
        } else {
            Usuario uActualizar = new Usuario();
            uActualizar.setIdUsuario(idUsuarioSeleccionado);
            uActualizar.setNombre(nombre);
            uActualizar.setCorreo(correo);
            uActualizar.setContrasena(pass); // Si va vacío, CtrlUsuario conserva la actual
            uActualizar.setIdRol(idRol);
            uActualizar.setEstado((String) cbEstado.getSelectedItem());
            exito = ctrlUsuario.actualizarUsuario(uActualizar);
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, "Usuario guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo procesar el usuario en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inactivarUsuarioSeleccionado() {
        if (idUsuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla para inactivar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea inactivar al usuario seleccionado? No podrá iniciar sesión hasta ser reactivado.", "Confirmar Inactivación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (ctrlUsuario.inactivarUsuario(idUsuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Usuario marcado como INACTIVO.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Error al inactivar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        idUsuarioSeleccionado = null;
        txtNombre.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        cbRol.setSelectedIndex(0);
        cbEstado.setSelectedItem("ACTIVO");
        cbEstado.setEnabled(false);
        btnGuardar.setText("Guardar Usuario");
        tablaUsuarios.clearSelection();
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