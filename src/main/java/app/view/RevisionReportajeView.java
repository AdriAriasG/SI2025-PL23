package app.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class RevisionReportajeView {

    private JFrame frame;

    // Tabla superior
    private JTable tablaReportajes;

    // Datos reportaje
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea txtCuerpo;
    private JLabel lblEstado;

    // Multimedia
    private JTable tablaMultimedia;

    // Mi revisión
    private JTextArea txtComentario;
    private JButton btnGuardarComentario;
    private JButton btnFinalizarRevision;
    private JLabel lblRevisionEstado;

    public RevisionReportajeView() {
        initialize();
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Revisión de Reportajes");
        frame.setBounds(100, 100, 950, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ===============================
        // TÍTULO
        // ===============================
        JLabel lblTitulo = new JLabel("REVISIÓN DE REPORTAJES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ===============================
        // TABLA REPORTAJES
        // ===============================
        panelPrincipal.add(new JLabel("Reportajes en revisión pendientes"));

        tablaReportajes = new JTable();
        JScrollPane scrollReportajes = new JScrollPane(tablaReportajes);
        scrollReportajes.setPreferredSize(new Dimension(850, 130));
        panelPrincipal.add(scrollReportajes);

        panelPrincipal.add(Box.createVerticalStrut(20));

        // ===============================
        // DATOS DEL REPORTAJE
        // ===============================
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBorder(BorderFactory.createTitledBorder("Datos del reportaje"));

        txtTitulo = new JTextField();
        txtSubtitulo = new JTextField();
        txtCuerpo = new JTextArea(6, 20);

        txtTitulo.setEditable(false);
        txtSubtitulo.setEditable(false);
        txtCuerpo.setEditable(false);

        panelDatos.add(new JLabel("Título"));
        panelDatos.add(txtTitulo);

        panelDatos.add(new JLabel("Subtítulo"));
        panelDatos.add(txtSubtitulo);

        panelDatos.add(new JLabel("Cuerpo"));
        panelDatos.add(new JScrollPane(txtCuerpo));

        lblEstado = new JLabel("Estado: -");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 13));
        lblEstado.setOpaque(true);
        lblEstado.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        panelDatos.add(Box.createVerticalStrut(8));
        panelDatos.add(lblEstado);

        panelPrincipal.add(panelDatos);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // ===============================
        // MULTIMEDIA
        // ===============================
        JPanel panelMultimedia = new JPanel();
        panelMultimedia.setLayout(new BoxLayout(panelMultimedia, BoxLayout.Y_AXIS));
        panelMultimedia.setBorder(BorderFactory.createTitledBorder("Contenido multimedia"));

        tablaMultimedia = new JTable();
        JScrollPane scrollMultimedia = new JScrollPane(tablaMultimedia);
        scrollMultimedia.setPreferredSize(new Dimension(850, 150));
        panelMultimedia.add(scrollMultimedia);

        panelPrincipal.add(panelMultimedia);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // ===============================
        // MI REVISIÓN
        // ===============================
        JPanel panelRevision = new JPanel();
        panelRevision.setLayout(new BoxLayout(panelRevision, BoxLayout.Y_AXIS));
        panelRevision.setBorder(BorderFactory.createTitledBorder("Mi revisión"));

        txtComentario = new JTextArea(5, 20);
        JScrollPane scrollComentario = new JScrollPane(txtComentario);

        panelRevision.add(new JLabel("Comentario"));
        panelRevision.add(scrollComentario);

        JPanel panelBotones = new JPanel(new FlowLayout());

        btnGuardarComentario = new JButton("Guardar comentario");
        btnFinalizarRevision = new JButton("Finalizar revisión");

        panelBotones.add(btnGuardarComentario);
        panelBotones.add(btnFinalizarRevision);

        panelRevision.add(Box.createVerticalStrut(10));
        panelRevision.add(panelBotones);

        lblRevisionEstado = new JLabel("");
        lblRevisionEstado.setFont(new Font("Arial", Font.BOLD, 13));
        lblRevisionEstado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelRevision.add(lblRevisionEstado);

        panelPrincipal.add(panelRevision);

        JScrollPane scrollGeneral = new JScrollPane(panelPrincipal);
        frame.add(scrollGeneral, BorderLayout.CENTER);
    }

    // ===============================
    // MÉTODOS VISUALES PROFESIONALES
    // ===============================

    public void actualizarEstadoReportaje(String estado) {

        lblEstado.setText("Estado: " + estado);

        if ("EN_REVISION".equals(estado)) {
            lblEstado.setBackground(new Color(255, 230, 230));
            lblEstado.setForeground(Color.RED);
        } else {
            lblEstado.setBackground(new Color(230, 255, 230));
            lblEstado.setForeground(new Color(0, 128, 0));
        }
    }

    public void mostrarRevisionFinalizada() {

        lblRevisionEstado.setText("Revisión finalizada");
        lblRevisionEstado.setForeground(new Color(0, 128, 0));

        txtComentario.setEditable(false);
        btnGuardarComentario.setEnabled(false);
        btnFinalizarRevision.setEnabled(false);
    }

    public void mostrarRevisionPendiente() {

        lblRevisionEstado.setText("Revisión pendiente");
        lblRevisionEstado.setForeground(Color.RED);

        txtComentario.setEditable(true);
        btnGuardarComentario.setEnabled(true);
        btnFinalizarRevision.setEnabled(true);
    }
    
    public void limpiarDetalle() {
        txtTitulo.setText("");
        txtSubtitulo.setText("");
        txtCuerpo.setText("");
        txtComentario.setText("");
        actualizarEstadoReportaje("");
        tablaMultimedia.setModel(new DefaultTableModel());
    }

    // ===============================
    // GETTERS
    // ===============================

    public JFrame getFrame() { return frame; }
    public JTable getTablaReportajes() { return tablaReportajes; }
    public JTable getTablaMultimedia() { return tablaMultimedia; }

    public JTextField getTxtTitulo() { return txtTitulo; }
    public JTextField getTxtSubtitulo() { return txtSubtitulo; }
    public JTextArea getTxtCuerpo() { return txtCuerpo; }
    public JTextArea getTxtComentario() { return txtComentario; }

    public JButton getBtnGuardarComentario() { return btnGuardarComentario; }
    public JButton getBtnFinalizarRevision() { return btnFinalizarRevision; }
}