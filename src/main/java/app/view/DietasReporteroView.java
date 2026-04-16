package app.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.dto.EventoDTO;
import app.dto.DietaDTO;

public class DietasReporteroView {

    private JFrame frame;

    // Tabla eventos
    private JTable tablaEventos;
    private DefaultTableModel tableModel;

    // Detalle evento
    private JTextField tfEvento;
    private JTextField tfProvincia;
    private JTextField tfPais;

    // Dietas
    private JTextField tfAlojamiento;
    private JTextField tfManutencion;
    private JTextField tfDias;
    private JTextField tfTotal;

    public DietasReporteroView() {
        initialize();
    }

    private void initialize() {

        frame = new JFrame("Dietas del Reportero");
        frame.setSize(750, 550);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));

        // =======================
        // TABLA EVENTOS
        // =======================

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Eventos asignados"));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Fecha inicio", "Fecha fin"}, 0
        );
        tablaEventos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablaEventos);

        scrollPane.setPreferredSize(new Dimension(700, 120));
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        frame.add(panelTabla, BorderLayout.NORTH);

        // =======================
        // PANEL DETALLE
        // =======================

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel titulo = crearTitulo("Detalle de Dietas");
        panelCentro.add(titulo);

        tfEvento = crearCampo(panelCentro, "Evento seleccionado:");
        panelCentro.add(Box.createVerticalStrut(15));

        tfProvincia = crearCampo(panelCentro, "Provincia:");
        tfPais = crearCampo(panelCentro, "País:");

        panelCentro.add(Box.createVerticalStrut(10));

        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panelCentro.add(sep1);

        panelCentro.add(Box.createVerticalStrut(10));

        tfAlojamiento = crearCampo(panelCentro, "Importe dieta alojamiento:");
        tfManutencion = crearCampo(panelCentro, "Importe dieta manutención:");
        tfDias = crearCampo(panelCentro, "Número de días:");

        panelCentro.add(Box.createVerticalStrut(10));

        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panelCentro.add(sep2);

        panelCentro.add(Box.createVerticalStrut(10));

        tfTotal = crearCampoGrande(panelCentro, "TOTAL DIETAS:");

        frame.add(panelCentro, BorderLayout.CENTER);
    }

    private JLabel crearTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField crearCampo(JPanel panel, String etiqueta) {

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel(etiqueta);
        JTextField textField = new JTextField(20);
        textField.setEditable(false);

        fila.add(label);
        fila.add(textField);

        panel.add(fila);

        return textField;
    }

    private JTextField crearCampoGrande(JPanel panel, String etiqueta) {

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField textField = new JTextField(20);
        textField.setEditable(false);
        textField.setFont(new Font("Arial", Font.BOLD, 14));

        fila.add(label);
        fila.add(textField);

        panel.add(fila);

        return textField;
    }

    // =========================
    // MÉTODOS PARA CONTROLLER
    // =========================

    public void setEventos(List<EventoDTO> eventos) {
        tableModel.setRowCount(0);
        for (EventoDTO e : eventos) {
            tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getNombre(),
                    e.getFechaInicio(),
                    e.getFechaFin()
            });
        }
    }

    public void addEventoSelectionListener(javax.swing.event.ListSelectionListener l) {
        tablaEventos.getSelectionModel().addListSelectionListener(l);
    }

    public int getEventoSeleccionadoIndex() {
        return tablaEventos.getSelectedRow();
    }

    public void mostrarDieta(EventoDTO evento, DietaDTO dieta) {

        tfEvento.setText(evento.getNombre());
        tfProvincia.setText(evento.getNombreProvincia());
        tfPais.setText(evento.getNombrePais());

        tfAlojamiento.setText(dieta.getImporteAlojamiento() + " €");
        tfManutencion.setText(dieta.getImporteManutencion() + " €");
        tfDias.setText(String.valueOf(dieta.getNumeroDias()));
        tfTotal.setText(dieta.getTotal() + " €");
    }

    public void limpiarResultados() {
        tfEvento.setText("");
        tfProvincia.setText("");
        tfPais.setText("");
        tfAlojamiento.setText("");
        tfManutencion.setText("");
        tfDias.setText("");
        tfTotal.setText("");
    }

    public void mostrarVista() {
        frame.setVisible(true);
    }
}