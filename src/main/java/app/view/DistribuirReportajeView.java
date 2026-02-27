package app.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
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

    private JLabel lblNombreAgencia;

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
        setBounds(100, 100, 600, 400);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        // Label Agencia
        lblNombreAgencia = new JLabel("Agencia de Prensa: ");
        lblNombreAgencia.setBounds(20, 10, 300, 20);
        panel.add(lblNombreAgencia);

        // Separador
        JSeparator separator = new JSeparator();
        separator.setBounds(10, 40, 560, 2);
        panel.add(separator);

        // Títulos
        JLabel lblEventos = new JLabel("Eventos");
        lblEventos.setBounds(20, 50, 100, 15);
        panel.add(lblEventos);

        JLabel lblEmpresas = new JLabel("Empresas");
        lblEmpresas.setBounds(220, 50, 100, 15);
        panel.add(lblEmpresas);

        JLabel lblSeleccionadas = new JLabel("Empresas seleccionadas");
        lblSeleccionadas.setBounds(400, 50, 160, 15);
        panel.add(lblSeleccionadas);

        // Tabla Eventos
        JScrollPane scrollEventos = new JScrollPane();
        scrollEventos.setBounds(20, 70, 150, 150);
        panel.add(scrollEventos);

        tableEventos = new JTable();
        tableEventos.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Nombre", "Fecha" }
        ));
       
        scrollEventos.setViewportView(tableEventos);

        // Tabla Empresas
        JScrollPane scrollEmpresas = new JScrollPane();
        scrollEmpresas.setBounds(220, 70, 150, 150);
        panel.add(scrollEmpresas);

        tableEmpresas = new JTable();
        tableEmpresas.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Nombre" }
        ));
        scrollEmpresas.setViewportView(tableEmpresas);

        // Tabla Seleccionadas
        JScrollPane scrollSeleccionadas = new JScrollPane();
        scrollSeleccionadas.setBounds(400, 70, 150, 150);
        panel.add(scrollSeleccionadas);

        tableSeleccionadas = new JTable();
        tableSeleccionadas.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Nombre" }
        ));
        scrollSeleccionadas.setViewportView(tableSeleccionadas);

        tableSeleccionadas.getColumnModel().getColumn(0).setMinWidth(0);
        tableSeleccionadas.getColumnModel().getColumn(0).setMaxWidth(0);
        tableSeleccionadas.getColumnModel().getColumn(0).setWidth(0);
        
        // Botón Conceder
        btnConceder = new JButton("<html>Conceder<br>Acceso</html>");
        btnConceder.setBounds(250, 230, 100, 40);
        panel.add(btnConceder);

        // Botón Aceptar
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(320, 300, 100, 25);
        panel.add(btnAceptar);

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(440, 300, 100, 25);
        panel.add(btnCancelar);
    }

    // ===== MÉTODOS PARA EL CONTROLLER =====

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

    public void setNombreAgencia(String nombre) {
        lblNombreAgencia.setText("Agencia de Prensa: " + nombre);
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

        // Ocultar columna ID
        tableEmpresas.getColumnModel().getColumn(0).setMinWidth(0);
        tableEmpresas.getColumnModel().getColumn(0).setMaxWidth(0);
        tableEmpresas.getColumnModel().getColumn(0).setWidth(0);
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