package View;

import com.formdev.flatlaf.FlatClientProperties;
import Controller.CtrlLogin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import model.Usuario;

public class FrmLogin extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private final CtrlLogin ctrlLogin;

    public FrmLogin() {
        this.ctrlLogin = new CtrlLogin();
        initComponents();
        configurarEstilosFigma();
    }

    private void initComponents() {
        setTitle("Mesa de Servicio TI — Módulo de Autenticación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Fondo principal Slate
        JPanel panelFondo = new JPanel(new GridBagLayout());
        panelFondo.setBackground(Color.decode("#3B4252"));

        // Tarjeta central de Login
        JPanel cardLogin = new JPanel();
        cardLogin.setLayout(new BoxLayout(cardLogin, BoxLayout.Y_AXIS));
        cardLogin.setBackground(Color.decode("#4C566A"));
        cardLogin.setBorder(BorderFactory.createEmptyBorder(30, 40, 35, 40));

        // Título principal superior
        JLabel lblHeader = new JLabel("Sistema de Gestion de Mesa de Servicio de TI");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Componentes internos centrado horizontal estricto
        JLabel lblCardTitle = new JLabel("Iniciar Sesion");
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblCardTitle.setForeground(Color.WHITE);
        lblCardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCardSub = new JLabel("Ingresa tus Datos de Usuario");
        lblCardSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCardSub.setForeground(Color.decode("#D8DEE9"));
        lblCardSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtCorreo = new JTextField(20);
        txtCorreo.setMaximumSize(new Dimension(340, 40));
        txtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(Color.WHITE);
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtContrasena = new JPasswordField(20);
        txtContrasena.setMaximumSize(new Dimension(340, 40));
        txtContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnIngresar = new JButton("Ingresa al Sistema");
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setMaximumSize(new Dimension(340, 42));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Eventos
        btnIngresar.addActionListener((ActionEvent e) -> ejecutarLogin());
        txtContrasena.addActionListener((ActionEvent e) -> ejecutarLogin());

        // Ensamblado en tarjeta
        cardLogin.add(lblCardTitle);
        cardLogin.add(Box.createRigidArea(new Dimension(0, 5)));
        cardLogin.add(lblCardSub);
        cardLogin.add(Box.createRigidArea(new Dimension(0, 20)));
        cardLogin.add(lblUsuario);
        cardLogin.add(Box.createRigidArea(new Dimension(0, 5)));
        cardLogin.add(txtCorreo);
        cardLogin.add(Box.createRigidArea(new Dimension(0, 15)));
        cardLogin.add(lblPass);
        cardLogin.add(Box.createRigidArea(new Dimension(0, 5)));
        cardLogin.add(txtContrasena);
        cardLogin.add(Box.createRigidArea(new Dimension(0, 25)));
        cardLogin.add(btnIngresar);

        // Layout global centrado
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        panelFondo.add(lblHeader, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelFondo.add(cardLogin, gbc);

        add(panelFondo);
    }

    private void configurarEstilosFigma() {
        txtCorreo.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #FFFFFF; margin: 5,10,5,10");
        txtContrasena.putClientProperty(FlatClientProperties.STYLE, "arc: 12; background: #3B4252; foreground: #FFFFFF; showRevealButton: true; margin: 5,10,5,10");
        btnIngresar.putClientProperty(FlatClientProperties.STYLE, "arc: 14; background: #1865C9; foreground: #FFFFFF; font: bold 14 Segoe UI");
    }

    private void ejecutarLogin() {
    String correo = txtCorreo.getText().trim();
    String contrasena = new String(txtContrasena.getPassword());

    if (correo.isEmpty() || contrasena.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor ingrese usuario y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        Usuario usuario = ctrlLogin.autenticar(correo, contrasena);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Acceso exitoso. Bienvenido/a " + usuario.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Enrutamiento por Rol (RBAC)
            switch (usuario.getIdRol()) {
                case 1: // ADMINISTRADOR
                     new FrmDashboardAdmin().setVisible(true);
                    break;

                case 2: // TÉCNICO
                    new FrmTecnicoTickets().setVisible(true);
                    break;

                case 3: // SOLICITANTE
                    new FrmDashboardUsuario().setVisible(true);
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "El rol del usuario no está reconocido en el sistema.", "Error de Rol", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña no válidos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al instanciar la vista principal: " + ex.getMessage(), "Error de Ejecución", JOptionPane.ERROR_MESSAGE);
    }
}
}