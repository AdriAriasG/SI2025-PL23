package app.view;

import javax.swing.*;
import java.awt.*;

public class RestaurarVersionView extends JFrame {

    private JTable tablaEventos;
    private JTable tablaVersiones;

    private JTextArea txtSubtituloActual;
    private JTextArea txtCuerpoActual;

    private JTextArea txtSubtituloSeleccionado;
    private JTextArea txtCuerpoSeleccionado;

    private JCheckBox chkSubtitulo;
    private JCheckBox chkCuerpo;

    private JButton btnRestaurar;

    public RestaurarVersionView() {

        setTitle("Restaurar Versión de Reportaje");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // ====================================================
        // PANEL SUPERIOR: EVENTOS
        // ====================================================
        tablaEventos = new JTable();
        JScrollPane scrollEventos = new JScrollPane(tablaEventos);
        scrollEventos.setBorder(BorderFactory.createTitledBorder("Eventos con Reportaje"));
        scrollEventos.setPreferredSize(new Dimension(900, 120));
        add(scrollEventos, BorderLayout.NORTH);

        // ====================================================
        // PANEL CENTRAL PRINCIPAL
        // ====================================================
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        add(panelCentral, BorderLayout.CENTER);

        // ====================================================
        // TABLA VERSIONES
        // ====================================================
        tablaVersiones = new JTable();
        JScrollPane scrollVersiones = new JScrollPane(tablaVersiones);
        scrollVersiones.setBorder(BorderFactory.createTitledBorder("Historial de Versiones"));
        scrollVersiones.setPreferredSize(new Dimension(850, 150));
        panelCentral.add(scrollVersiones);

        panelCentral.add(Box.createVerticalStrut(10));

        // ====================================================
        // VERSIÓN ACTUAL
        // ====================================================
        JPanel panelActual = new JPanel(new BorderLayout());
        panelActual.setBorder(BorderFactory.createTitledBorder("Versión Actual"));

        JPanel panelActualCampos = new JPanel();
        panelActualCampos.setLayout(new BoxLayout(panelActualCampos, BoxLayout.Y_AXIS));

        txtSubtituloActual = new JTextArea(2, 50);
        txtSubtituloActual.setLineWrap(true);
        txtSubtituloActual.setWrapStyleWord(true);
        txtSubtituloActual.setEditable(false);

        txtCuerpoActual = new JTextArea(5, 50);
        txtCuerpoActual.setLineWrap(true);
        txtCuerpoActual.setWrapStyleWord(true);
        txtCuerpoActual.setEditable(false);

        panelActualCampos.add(new JLabel("Subtítulo"));
        panelActualCampos.add(new JScrollPane(txtSubtituloActual));
        panelActualCampos.add(Box.createVerticalStrut(5));
        panelActualCampos.add(new JLabel("Cuerpo"));
        panelActualCampos.add(new JScrollPane(txtCuerpoActual));

        panelActual.add(panelActualCampos, BorderLayout.CENTER);
        panelCentral.add(panelActual);

        panelCentral.add(Box.createVerticalStrut(10));

        // ====================================================
        // VERSIÓN SELECCIONADA
        // ====================================================
        JPanel panelSeleccionada = new JPanel(new BorderLayout());
        panelSeleccionada.setBorder(BorderFactory.createTitledBorder("Versión Seleccionada"));

        JPanel panelSeleccionadaCampos = new JPanel();
        panelSeleccionadaCampos.setLayout(new BoxLayout(panelSeleccionadaCampos, BoxLayout.Y_AXIS));

        txtSubtituloSeleccionado = new JTextArea(2, 50);
        txtSubtituloSeleccionado.setLineWrap(true);
        txtSubtituloSeleccionado.setWrapStyleWord(true);
        txtSubtituloSeleccionado.setEditable(false);

        txtCuerpoSeleccionado = new JTextArea(5, 50);
        txtCuerpoSeleccionado.setLineWrap(true);
        txtCuerpoSeleccionado.setWrapStyleWord(true);
        txtCuerpoSeleccionado.setEditable(false);

        panelSeleccionadaCampos.add(new JLabel("Subtítulo"));
        panelSeleccionadaCampos.add(new JScrollPane(txtSubtituloSeleccionado));
        panelSeleccionadaCampos.add(Box.createVerticalStrut(5));
        panelSeleccionadaCampos.add(new JLabel("Cuerpo"));
        panelSeleccionadaCampos.add(new JScrollPane(txtCuerpoSeleccionado));

        panelSeleccionada.add(panelSeleccionadaCampos, BorderLayout.CENTER);
        panelCentral.add(panelSeleccionada);

        panelCentral.add(Box.createVerticalStrut(10));

        // ====================================================
        // PANEL INFERIOR: CHECKBOX + BOTÓN
        // ====================================================
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER));

        chkSubtitulo = new JCheckBox("Restaurar subtítulo");
        chkCuerpo = new JCheckBox("Restaurar cuerpo");

        btnRestaurar = new JButton("Restaurar");

        panelInferior.add(chkSubtitulo);
        panelInferior.add(chkCuerpo);
        panelInferior.add(btnRestaurar);

        panelCentral.add(panelInferior);
    }

    // ====================================================
    // GETTERS
    // ====================================================

    public JTable getTablaEventos() { return tablaEventos; }
    public JTable getTablaVersiones() { return tablaVersiones; }

    public JTextArea getTxtSubtituloActual() { return txtSubtituloActual; }
    public JTextArea getTxtCuerpoActual() { return txtCuerpoActual; }

    public JTextArea getTxtSubtituloSeleccionado() { return txtSubtituloSeleccionado; }
    public JTextArea getTxtCuerpoSeleccionado() { return txtCuerpoSeleccionado; }

    public JCheckBox getChkSubtitulo() { return chkSubtitulo; }
    public JCheckBox getChkCuerpo() { return chkCuerpo; }

    public JButton getBtnRestaurar() { return btnRestaurar; }

    public JFrame getFrame() { return this; }
}