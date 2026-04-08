package app.view;

import javax.swing.*;
import java.awt.*;

public class FinalizarReportajeView {

    private JFrame frame;

    private JTable tablaEventos;
    private JTable tablaRevisiones;

    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea txtCuerpo;

    private JButton btnGuardarCambios;
    private JButton btnFinalizar;

    public FinalizarReportajeView() {
        initialize();
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Finalizar Reportaje");
        frame.setBounds(100, 100, 1100, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // =========================
        // PANEL IZQUIERDO - EVENTOS
        // =========================

        JPanel panelIzq = new JPanel(new BorderLayout());
        panelIzq.setBorder(BorderFactory.createTitledBorder("Reportajes en revisión"));

        tablaEventos = new JTable();
        panelIzq.add(new JScrollPane(tablaEventos), BorderLayout.CENTER);

        frame.getContentPane().add(panelIzq, BorderLayout.WEST);
        panelIzq.setPreferredSize(new Dimension(300, 600));

        // =========================
        // PANEL CENTRAL - CONTENIDO
        // =========================

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createTitledBorder("Contenido del reportaje"));

        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(3, 1));

        txtTitulo = new JTextField();
        txtSubtitulo = new JTextField();
        txtCuerpo = new JTextArea(10, 20);

        panelCampos.add(crearCampo("Título:", txtTitulo));
        panelCampos.add(crearCampo("Subtítulo:", txtSubtitulo));
        panelCampos.add(new JScrollPane(txtCuerpo));

        panelCentro.add(panelCampos, BorderLayout.CENTER);

        btnGuardarCambios = new JButton("Guardar cambios");
        panelCentro.add(btnGuardarCambios, BorderLayout.SOUTH);

        frame.getContentPane().add(panelCentro, BorderLayout.CENTER);

        // =========================
        // PANEL DERECHO - REVISIONES
        // =========================

        JPanel panelDer = new JPanel(new BorderLayout());
        panelDer.setBorder(BorderFactory.createTitledBorder("Revisiones"));

        tablaRevisiones = new JTable();
        panelDer.add(new JScrollPane(tablaRevisiones), BorderLayout.CENTER);

        btnFinalizar = new JButton("FINALIZAR REPORTAJE");
        panelDer.add(btnFinalizar, BorderLayout.SOUTH);

        frame.getContentPane().add(panelDer, BorderLayout.EAST);
        panelDer.setPreferredSize(new Dimension(350, 600));
    }

    private JPanel crearCampo(String label, JTextField field) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    // =========================
    // GETTERS (usados por el controlador)
    // =========================

    public JFrame getFrame() {
        return frame;
    }

    public JTable getTablaEventos() {
        return tablaEventos;
    }

    public JTable getTablaRevisiones() {
        return tablaRevisiones;
    }

    public JTextField getTxtTitulo() {
        return txtTitulo;
    }

    public JTextField getTxtSubtitulo() {
        return txtSubtitulo;
    }

    public JTextArea getTxtCuerpo() {
        return txtCuerpo;
    }

    public JButton getBtnGuardarCambios() {
        return btnGuardarCambios;
    }

    public JButton getBtnFinalizar() {
        return btnFinalizar;
    }
}