package app.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class InformeReportajeEmpresaView {
    private JFrame frame;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JButton btnGenerar;
    private JButton btnCerrar;
    private JTable tablaReportajes;
    private DefaultTableModel modeloReportajes;
    private JLabel lblPrecioTotal;

    public InformeReportajeEmpresaView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Informe de Reportajes con Acceso");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout(0, 10));
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // --- PANEL DE FILTROS SUPERIOR ---
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtro por Fechas de Evento (YYYY-MM-DD)"));

        panelFiltros.add(new JLabel("Fecha Inicio:"));
        txtFechaInicio = new JTextField(10);
        panelFiltros.add(txtFechaInicio);

        panelFiltros.add(new JLabel("Fecha Fin:"));
        txtFechaFin = new JTextField(10);
        panelFiltros.add(txtFechaFin);

        btnGenerar = new JButton("Generar Informe");
        panelFiltros.add(btnGenerar);

        mainPanel.add(panelFiltros, BorderLayout.NORTH);

        // --- TABLA CENTRAL ---
        modeloReportajes = new DefaultTableModel(new String[]{"Título Reportaje", "Evento", "Fecha Evento", "Precio (€)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaReportajes = new JTable(modeloReportajes);
        tablaReportajes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ajustar anchos de columna aproximados
        tablaReportajes.getColumnModel().getColumn(0).setPreferredWidth(250);
        tablaReportajes.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaReportajes.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaReportajes.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tablaReportajes);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- PANEL INFERIOR ---
        JPanel panelInferior = new JPanel(new BorderLayout());
        
        lblPrecioTotal = new JLabel("Precio Total: 0.00 €");
        lblPrecioTotal.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelInferior.add(lblPrecioTotal, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnCerrar);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        mainPanel.add(panelInferior, BorderLayout.SOUTH);
    }

    public JFrame getFrame() { return frame; }
    public JTextField getTxtFechaInicio() { return txtFechaInicio; }
    public JTextField getTxtFechaFin() { return txtFechaFin; }
    public JButton getBtnGenerar() { return btnGenerar; }
    public JButton getBtnCerrar() { return btnCerrar; }
    public DefaultTableModel getModeloReportajes() { return modeloReportajes; }
    public JLabel getLblPrecioTotal() { return lblPrecioTotal; }

    public void showError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
