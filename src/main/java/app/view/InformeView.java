package app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import app.dto.EventoDTO;
import app.dto.InformeEventoDTO;

/**
 * Vista para el informe de eventos.
 * Cubre la HU #33548.
 */
public class InformeView {
    private JFrame frame;

    // Tabla de eventos
    private JTable tablaEventos;
    private DefaultTableModel modeloEventos;

    // Panel de informe
    private JTextArea txtInforme;
    private JButton btnGenerar;
    private JButton btnExportarCSV;
    private JButton btnCerrar;

    // Informe actual (para exportación)
    private InformeEventoDTO informeActual;
    
    public InformeView() {
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Informe de Evento");
        frame.setBounds(100, 100, 700, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // === PANEL EVENTOS ===
        JPanel panelEventos = new JPanel();
        panelEventos.setLayout(new BoxLayout(panelEventos, BoxLayout.Y_AXIS));
        panelEventos.setBorder(BorderFactory.createTitledBorder("Seleccione un Evento"));
        
        modeloEventos = new DefaultTableModel(new String[]{"ID", "Nombre", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEventos = new JTable(modeloEventos);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEventos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaEventos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaEventos.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane scrollEventos = new JScrollPane(tablaEventos);
        scrollEventos.setPreferredSize(new Dimension(650, 150));
        panelEventos.add(scrollEventos);
        mainPanel.add(panelEventos);
        
        // === PANEL INFORME ===
        JPanel panelInforme = new JPanel();
        panelInforme.setLayout(new BoxLayout(panelInforme, BoxLayout.Y_AXIS));
        panelInforme.setBorder(BorderFactory.createTitledBorder("Informe del Evento"));
        
        txtInforme = new JTextArea();
        txtInforme.setEditable(false);
        txtInforme.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtInforme.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollInforme = new JScrollPane(txtInforme);
        scrollInforme.setPreferredSize(new Dimension(650, 300));
        panelInforme.add(scrollInforme);
        mainPanel.add(panelInforme);
        
        // === PANEL BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGenerar = new JButton("Generar Informe");
        btnExportarCSV = new JButton("Exportar CSV");
        btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnGenerar);
        panelBotones.add(btnExportarCSV);
        panelBotones.add(btnCerrar);
        mainPanel.add(panelBotones);
    }
    
    // === GETTERS ===
    public JFrame getFrame() { return frame; }
    public JTable getTablaEventos() { return tablaEventos; }
    public JTextArea getTxtInforme() { return txtInforme; }
    public JButton getBtnGenerar() { return btnGenerar; }
    public JButton getBtnExportarCSV() { return btnExportarCSV; }
    public JButton getBtnCerrar() { return btnCerrar; }
    
    // === MÉTODOS PARA POBLAR DATOS ===
    
    /**
     * Limpia y pobla la tabla de eventos
     */
    public void setEventos(List<EventoDTO> eventos) {
        modeloEventos.setRowCount(0);
        for (EventoDTO e : eventos) {
            modeloEventos.addRow(new Object[]{e.getId(), e.getNombre(), e.getFecha()});
        }
    }
    
    /**
     * Obtiene el ID del evento seleccionado, o -1 si no hay selección
     */
    public int getIdEventoSeleccionado() {
        int row = tablaEventos.getSelectedRow();
        if (row >= 0) {
            return (int) modeloEventos.getValueAt(row, 0);
        }
        return -1;
    }
    
    /**
     * Obtiene el nombre del evento seleccionado
     */
    public String getNombreEventoSeleccionado() {
        int row = tablaEventos.getSelectedRow();
        if (row >= 0) {
            return (String) modeloEventos.getValueAt(row, 1);
        }
        return null;
    }
    
    /**
     * Obtiene la fecha del evento seleccionado
     */
    public String getFechaEventoSeleccionado() {
        int row = tablaEventos.getSelectedRow();
        if (row >= 0) {
            return (String) modeloEventos.getValueAt(row, 2);
        }
        return null;
    }
    
    /**
     * Muestra el informe en el área de texto
     */
    public void mostrarInforme(InformeEventoDTO informe) {
        this.informeActual = informe; // Guardar referencia para exportación

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                    INFORME DEL EVENTO\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");
        
        sb.append("Evento: ").append(informe.getNombreEvento()).append("\n");
        sb.append("Fecha:  ").append(informe.getFechaEvento()).append("\n\n");
        
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append("REPORTEROS ASIGNADOS:\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        List<String> reporteros = informe.getReporterosAsignados();
        if (reporteros == null || reporteros.isEmpty()) {
            sb.append("  (No hay reporteros asignados)\n");
        } else {
            for (String nombre : reporteros) {
                sb.append("  • ").append(nombre).append("\n");
            }
        }
        sb.append("\n");
        
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append("REPORTAJE:\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        if (informe.isTieneReportaje()) {
            sb.append("  Estado: ENTREGADO\n");
            sb.append("  Autor:  ").append(informe.getNombreAutor() != null ? informe.getNombreAutor() : "Desconocido").append("\n");
        } else {
            sb.append("  Estado: NO ENTREGADO\n");
        }
        sb.append("\n");
        
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append("EMPRESAS CON ACCESO AL REPORTAJE:\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        List<String> empresas = informe.getEmpresasConAcceso();
        if (empresas == null || empresas.isEmpty()) {
            sb.append("  (No hay empresas con acceso)\n");
        } else {
            for (String nombre : empresas) {
                sb.append("  • ").append(nombre).append("\n");
            }
        }
        sb.append("\n");
        
        sb.append("═══════════════════════════════════════════════════════════════\n");
        
        txtInforme.setText(sb.toString());
        txtInforme.setCaretPosition(0); // Scroll al inicio
    }
    
    /**
     * Limpia el área de informe
     */
    public void limpiarInforme() {
        this.informeActual = null; // Limpiar referencia
        txtInforme.setText("Seleccione un evento y pulse \"Generar Informe\" para ver el informe.");
    }

    /**
     * Obtiene el informe actual (para exportación)
     */
    public InformeEventoDTO getInformeActual() {
        return informeActual;
    }

    /**
     * Muestra un mensaje de error
     */
    public void showError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje informativo
     */
    public void showInfo(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
