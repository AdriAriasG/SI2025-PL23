package app.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ModificacionOfrecimientosRecibidosView {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbTipoFiltro;
    private JComboBox<CheckItem> cbTematicas; 
    private JButton btnFiltrar, btnAceptar, btnRechazar, btnEliminar;
    private JScrollPane scrollTabla;
    private JLabel lblMensajeVacio;

    public ModificacionOfrecimientosRecibidosView() { initialize(); }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestión y Modificación de Decisiones");
        frame.setBounds(100, 100, 900, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.setContentPane(contentPane);

        // --- PANEL DE FILTROS ALINEADO ---
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Mostrar ofrecimientos
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelFiltros.add(new JLabel("Mostrar ofrecimientos:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        gbc.gridwidth = 2; // Que ocupe el espacio del combo + botón de abajo
        cbTipoFiltro = new JComboBox<>(new String[] { 
            "Todos los pendientes", "Solo de mis temáticas coincidentes", "Ya decididos (Historial)" 
        });
        panelFiltros.add(cbTipoFiltro, gbc);

        // Fila 2: Temáticas y Botón Filtrar
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        panelFiltros.add(new JLabel("Temáticas:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8;
        cbTematicas = new JComboBox<>();
        cbTematicas.setRenderer(new ElegantRenderer());
        cbTematicas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object item = cbTematicas.getSelectedItem();
                if (item instanceof CheckItem) {
                    CheckItem current = (CheckItem) item;
                    current.toggle();
                    if (current.toString().equals("--- Todas ---")) {
                        boolean state = current.isSelected();
                        for (int i = 0; i < cbTematicas.getItemCount(); i++) cbTematicas.getItemAt(i).setSelected(state);
                    } else if (!current.isSelected()) {
                        cbTematicas.getItemAt(0).setSelected(false);
                    }
                    cbTematicas.repaint();
                    if (cbTematicas.isShowing()) cbTematicas.setPopupVisible(true);
                }
            }
        });
        panelFiltros.add(cbTematicas, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.2;
        btnFiltrar = new JButton("Filtrar Resultados");
        panelFiltros.add(btnFiltrar, gbc);

        contentPane.add(panelFiltros, BorderLayout.NORTH);

        // --- TABLA ---
        model = new DefaultTableModel(new String[]{"Evento", "Fecha", "Estado Actual"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        scrollTabla = new JScrollPane(table);
        contentPane.add(scrollTabla, BorderLayout.CENTER);

        lblMensajeVacio = new JLabel("No hay registros para este filtro");
        lblMensajeVacio.setHorizontalAlignment(JLabel.CENTER);

        // --- BOTONES SUR ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnAceptar = new JButton("Aceptar");
        btnRechazar = new JButton("Rechazar");
        btnEliminar = new JButton("Eliminar Decisión");
        btnEliminar.setForeground(new Color(180, 0, 0));
        panelSur.add(btnAceptar); panelSur.add(btnRechazar); panelSur.add(btnEliminar);
        contentPane.add(panelSur, BorderLayout.SOUTH);
    }

    public static class CheckItem {
        private String label;
        private boolean selected = false;
        public CheckItem(String label) { this.label = label; }
        public void toggle() { selected = !selected; }
        public void setSelected(boolean s) { selected = s; }
        public boolean isSelected() { return selected; }
        @Override public String toString() { return label; }
    }

    class ElegantRenderer extends JPanel implements ListCellRenderer<Object> {
        private JLabel labelNombre = new JLabel();
        private JLabel labelCheck = new JLabel();
        public ElegantRenderer() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(3, 8, 3, 8));
            add(labelNombre, BorderLayout.CENTER);
            add(labelCheck, BorderLayout.EAST);
        }
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof CheckItem) {
                CheckItem item = (CheckItem) value;
                labelNombre.setText(item.toString());
                labelCheck.setText(item.isSelected() ? "  ✓ " : "    "); 
                labelCheck.setFont(labelCheck.getFont().deriveFont(Font.BOLD, 14f));
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                labelNombre.setForeground(list.getSelectionForeground());
                labelCheck.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                labelNombre.setForeground(list.getForeground());
                labelCheck.setForeground(new Color(0, 150, 0)); 
            }
            return this;
        }
    }

    public void mostrarMensajeVacio(boolean m) {
        scrollTabla.setViewportView(m ? lblMensajeVacio : table);
    }

    public JFrame getFrame() { return frame; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JComboBox<String> getCbTipoFiltro() { return cbTipoFiltro; }
    public JComboBox<CheckItem> getCbTematicas() { return cbTematicas; }
    public JButton getBtnFiltrar() { return btnFiltrar; }
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnRechazar() { return btnRechazar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}