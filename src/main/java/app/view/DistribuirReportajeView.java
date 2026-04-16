package app.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.dto.EmpresaComunicacionDTO;
import app.dto.EventoDTO;

public class DistribuirReportajeView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTable tableEventos;
    private JTable tableEmpresas;
    private JTable tableSeleccionadas;

    private JButton btnAceptar;
    private JButton btnCancelar;
    private JButton btnConceder;
    private JButton btnQuitar;
    private JButton btnAccesoEspecial;

    private JLabel lblNombreAgencia;
    private JLabel lblEmpresas;
    private JLabel lblSeleccionadas;

    private JComboBox<String> comboFiltro;
    private JComboBox<String> comboFiltroReportajes;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DistribuirReportajeView frame = new DistribuirReportajeView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DistribuirReportajeView() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 640, 470);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        lblNombreAgencia = new JLabel("Agencia de Prensa: ");
        lblNombreAgencia.setBounds(20, 10, 300, 20);
        panel.add(lblNombreAgencia);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 40, 600, 2);
        panel.add(separator);

        comboFiltro = new JComboBox<String>();
        comboFiltro.setModel(new DefaultComboBoxModel<String>(
                new String[] { "Empresas Sin Acceso", "Empresas Con Acceso" }));
        comboFiltro.setBounds(52, 64, 220, 20);
        panel.add(comboFiltro);

        comboFiltroReportajes = new JComboBox<String>();
        comboFiltroReportajes.setModel(new DefaultComboBoxModel<String>(
                new String[] { "Todos los reportajes", "Reportajes con embargo" }));
        comboFiltroReportajes.setBounds(340, 64, 220, 20);
        panel.add(comboFiltroReportajes);

        JLabel lblEventos = new JLabel("Eventos");
        lblEventos.setBounds(20, 110, 100, 15);
        panel.add(lblEventos);

        lblEmpresas = new JLabel("Empresas Sin Acceso");
        lblEmpresas.setBounds(220, 110, 150, 15);
        panel.add(lblEmpresas);

        lblSeleccionadas = new JLabel("Empresas Con Acceso");
        lblSeleccionadas.setBounds(420, 110, 160, 15);
        panel.add(lblSeleccionadas);

        JScrollPane scrollEventos = new JScrollPane();
        scrollEventos.setBounds(20, 135, 170, 140);
        panel.add(scrollEventos);

        tableEventos = new JTable();
        tableEventos.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Nombre", "Fecha" }
        ));
        scrollEventos.setViewportView(tableEventos);

        JScrollPane scrollEmpresas = new JScrollPane();
        scrollEmpresas.setBounds(220, 135, 170, 140);
        panel.add(scrollEmpresas);

        tableEmpresas = new JTable();
        tableEmpresas.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Nombre" }
        ));
        scrollEmpresas.setViewportView(tableEmpresas);

        JScrollPane scrollSeleccionadas = new JScrollPane();
        scrollSeleccionadas.setBounds(420, 135, 170, 140);
        panel.add(scrollSeleccionadas);

        tableSeleccionadas = new JTable();
        tableSeleccionadas.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Nombre" }
        ));
        scrollSeleccionadas.setViewportView(tableSeleccionadas);

        ocultarColumnaId(tableEmpresas);
        ocultarColumnaId(tableSeleccionadas);

        btnConceder = new JButton("<html>Conceder<br>Acceso</html>");
        btnConceder.setBounds(250, 295, 110, 40);
        panel.add(btnConceder);

        btnAccesoEspecial = new JButton("<html>Acceso<br>Especial</html>");
        btnAccesoEspecial.setBounds(250, 345, 110, 40);
        panel.add(btnAccesoEspecial);

        btnQuitar = new JButton("<html>Quitar<br>Acceso</html>");
        btnQuitar.setBounds(450, 295, 110, 40);
        panel.add(btnQuitar);

        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(350, 405, 100, 25);
        panel.add(btnAceptar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(470, 405, 100, 25);
        panel.add(btnCancelar);
    }

    private void ocultarColumnaId(JTable table) {
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
    }

    public JFrame getFrame() {
        return this;
    }

    public JTable getTablaEventos() {
        return tableEventos;
    }

    public JTable getTablaEmpresas() {
        return tableEmpresas;
    }

    public JTable getTablaSeleccionadas() {
        return tableSeleccionadas;
    }

    public JButton getBtnAceptar() {
        return btnAceptar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnConceder() {
        return btnConceder;
    }

    public JButton getBtnQuitar() {
        return btnQuitar;
    }

    public JButton getBtnAccesoEspecial() {
        return btnAccesoEspecial;
    }

    public JComboBox<String> getComboFiltro() {
        return comboFiltro;
    }

    public JComboBox<String> getComboFiltroReportajes() {
        return comboFiltroReportajes;
    }

    public String getFiltroSeleccionado() {
        return (String) comboFiltro.getSelectedItem();
    }

    public String getFiltroReportajesSeleccionado() {
        return (String) comboFiltroReportajes.getSelectedItem();
    }

    public void setNombreAgencia(String nombre) {
        lblNombreAgencia.setText("Agencia de Prensa: " + nombre);
    }

    public void setTextoEmpresas(String texto) {
        lblEmpresas.setText(texto);
    }

    public void setTextoSeleccionadas(String texto) {
        lblSeleccionadas.setText(texto);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setEventos(List<EventoDTO> eventos) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "ID", "Nombre", "Fecha" }, 0);

        for (EventoDTO e : eventos) {
            model.addRow(new Object[] {
                    e.getId(),
                    e.getNombre(),
                    e.getFecha()
            });
        }

        tableEventos.setModel(model);
    }

    public void setEmpresas(List<EmpresaComunicacionDTO> empresas) {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nombre"}, 0
        );

        for (EmpresaComunicacionDTO e : empresas) {
            model.addRow(new Object[]{
                e.getId(),
                e.getNombre()
            });
        }

        tableEmpresas.setModel(model);
        ocultarColumnaId(tableEmpresas);
    }

    public void setSeleccionadas(List<EmpresaComunicacionDTO> empresas) {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nombre"}, 0
        );

        for (EmpresaComunicacionDTO e : empresas) {
            model.addRow(new Object[]{
                e.getId(),
                e.getNombre()
            });
        }

        tableSeleccionadas.setModel(model);
        ocultarColumnaId(tableSeleccionadas);
    }

    public void limpiarEmpresas() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "ID", "Nombre" }, 0);
        tableEmpresas.setModel(model);
        ocultarColumnaId(tableEmpresas);
    }

    public void limpiarSeleccionadas() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "ID", "Nombre" }, 0);
        tableSeleccionadas.setModel(model);
        ocultarColumnaId(tableSeleccionadas);
    }

    public int getIdEventoSeleccionado() {
        int row = tableEventos.getSelectedRow();
        if (row == -1) return -1;
        return (int) tableEventos.getValueAt(row, 0);
    }

    public List<Integer> getEmpresasSeleccionadasIds() {
        int[] rows = tableSeleccionadas.getSelectedRows();
        List<Integer> ids = new ArrayList<>();

        for (int row : rows) {
            ids.add((Integer) tableSeleccionadas.getValueAt(row, 0));
        }

        return ids;
    }
}