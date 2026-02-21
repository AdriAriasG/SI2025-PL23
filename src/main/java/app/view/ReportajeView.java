package app.view;

import javax.swing.*;
import java.awt.*;

public class ReportajeView {

    private JFrame frame;
    private JTable tablaEventos;
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea txtCuerpo;
    private JButton btnEntregar;

    public ReportajeView() {
        initialize();
    }

    private void initialize() {

        frame = new JFrame();
        frame.setTitle("Entregar Reportaje");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tablaEventos = new JTable();
        frame.add(new JScrollPane(tablaEventos), BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(6,1));

        txtTitulo = new JTextField();
        txtSubtitulo = new JTextField();
        txtCuerpo = new JTextArea(5,20);

        panelCentro.add(new JLabel("Título"));
        panelCentro.add(txtTitulo);
        panelCentro.add(new JLabel("Subtítulo"));
        panelCentro.add(txtSubtitulo);
        panelCentro.add(new JLabel("Cuerpo"));
        panelCentro.add(new JScrollPane(txtCuerpo));

        frame.add(panelCentro, BorderLayout.CENTER);

        btnEntregar = new JButton("Entregar");
        frame.add(btnEntregar, BorderLayout.SOUTH);
    }

    // ======================
    // GETTERS
    // ======================

    public JFrame getFrame() { return frame; }
    public JTable getTablaEventos() { return tablaEventos; }
    public JTextField getTxtTitulo() { return txtTitulo; }
    public JTextField getTxtSubtitulo() { return txtSubtitulo; }
    public JTextArea getTxtCuerpo() { return txtCuerpo; }
    public JButton getBtnEntregar() { return btnEntregar; }

    public void limpiarFormulario() {
        txtTitulo.setText("");
        txtSubtitulo.setText("");
        txtCuerpo.setText("");
    }
}
