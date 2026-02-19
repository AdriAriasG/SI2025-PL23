package app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.dto.EventoDTO;
import app.dto.ReporteroDTO;

/**
 * Vista para la asignación de reporteros a eventos.
 * Cubre las HU #33537 y #33543 (modo edición).
 */
public class AsignacionView {
    private JFrame frame;
    private boolean modoEdicion;
    
    // Componentes para eventos
    private JComboBox<String> cbFiltro;
    private JTable tablaEventos;
    private DefaultTableModel modeloEventos;
    
    // Componentes para reporteros asignados (solo modo edición)
    private JTable tablaAsignados;
    private DefaultTableModel modeloAsignados;
    private JButton btnEliminar;
    private JLabel lblAsignados;
    private JScrollPane scrollAsignados;
    
    // Componentes para reporteros disponibles
    private JTable tablaDisponibles;
    private DefaultTableModel modeloDisponibles;
    
    // Lista de reporteros a asignar
    private DefaultListModel<String> listaParaAsignar;
    private JList<String> listParaAsignar;
    
    // Botones
    private JButton btnAsignar;
    private JButton btnCancelar;
    
    public AsignacionView(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame();
        frame.setTitle(modoEdicion ? "Modificar Asignación de Reporteros" : "Asignación de Reporteros");
        frame.setBounds(100, 100, 750, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // === PANEL FILTRO (solo modo edición) ===
        if (modoEdicion) {
            JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelFiltro.add(new JLabel("Filtro:"));
            cbFiltro = new JComboBox<>(new String[]{"Sin asignar", "Con asignados"});
            cbFiltro.setSelectedIndex(0);
            panelFiltro.add(cbFiltro);
            mainPanel.add(panelFiltro);
        }
        
        // === PANEL EVENTOS ===
        JPanel panelEventos = new JPanel();
        panelEventos.setLayout(new BoxLayout(panelEventos, BoxLayout.Y_AXIS));
        panelEventos.setBorder(BorderFactory.createTitledBorder("Eventos"));
        
        modeloEventos = new DefaultTableModel(new String[]{"ID", "Nombre", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEventos = new JTable(modeloEventos);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEventos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaEventos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaEventos.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane scrollEventos = new JScrollPane(tablaEventos);
        scrollEventos.setPreferredSize(new Dimension(700, 120));
        panelEventos.add(scrollEventos);
        mainPanel.add(panelEventos);
        
        // === PANEL REPORTEROS ASIGNADOS (solo modo edición) ===
        if (modoEdicion) {
            JPanel panelAsignados = new JPanel();
            panelAsignados.setLayout(new BoxLayout(panelAsignados, BoxLayout.Y_AXIS));
            panelAsignados.setBorder(BorderFactory.createTitledBorder("Reporteros Asignados al Evento"));
            
            modeloAsignados = new DefaultTableModel(new String[]{"ID", "Nombre"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tablaAsignados = new JTable(modeloAsignados);
            tablaAsignados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaAsignados.getColumnModel().getColumn(0).setPreferredWidth(40);
            tablaAsignados.getColumnModel().getColumn(1).setPreferredWidth(200);
            
            scrollAsignados = new JScrollPane(tablaAsignados);
            scrollAsignados.setPreferredSize(new Dimension(700, 100));
            panelAsignados.add(scrollAsignados);
            
            // Botón eliminar
            JPanel panelBtnEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnEliminar = new JButton("Eliminar Asignación Seleccionada");
            panelBtnEliminar.add(btnEliminar);
            panelAsignados.add(panelBtnEliminar);
            
            mainPanel.add(panelAsignados);
        }
        
        // === PANEL REPORTEROS DISPONIBLES ===
        JPanel panelDisponibles = new JPanel();
        panelDisponibles.setLayout(new BoxLayout(panelDisponibles, BoxLayout.Y_AXIS));
        panelDisponibles.setBorder(BorderFactory.createTitledBorder("Reporteros Disponibles"));
        
        modeloDisponibles = new DefaultTableModel(new String[]{"Seleccionar", "ID", "Nombre"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Solo la columna de selección es editable
            }
        };
        tablaDisponibles = new JTable(modeloDisponibles);
        tablaDisponibles.getColumnModel().getColumn(0).setPreferredWidth(70);
        tablaDisponibles.getColumnModel().getColumn(1).setPreferredWidth(40);
        tablaDisponibles.getColumnModel().getColumn(2).setPreferredWidth(200);
        
        JScrollPane scrollDisponibles = new JScrollPane(tablaDisponibles);
        scrollDisponibles.setPreferredSize(new Dimension(700, 150));
        panelDisponibles.add(scrollDisponibles);
        mainPanel.add(panelDisponibles);
        
        // === PANEL REPORTEROS A ASIGNAR ===
        JPanel panelParaAsignar = new JPanel();
        panelParaAsignar.setLayout(new BoxLayout(panelParaAsignar, BoxLayout.Y_AXIS));
        panelParaAsignar.setBorder(BorderFactory.createTitledBorder("Reporteros a Asignar"));
        
        listaParaAsignar = new DefaultListModel<>();
        listParaAsignar = new JList<>(listaParaAsignar);
        JScrollPane scrollParaAsignar = new JScrollPane(listParaAsignar);
        scrollParaAsignar.setPreferredSize(new Dimension(700, 80));
        panelParaAsignar.add(scrollParaAsignar);
        mainPanel.add(panelParaAsignar);
        
        // === PANEL BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAsignar = new JButton(modoEdicion ? "Asignar Seleccionados" : "Asignar Reporteros");
        btnCancelar = new JButton("Cerrar");
        panelBotones.add(btnAsignar);
        panelBotones.add(btnCancelar);
        mainPanel.add(panelBotones);
    }
    
    // === GETTERS ===
    public JFrame getFrame() { return frame; }
    public JComboBox<String> getCbFiltro() { return cbFiltro; }
    public JTable getTablaEventos() { return tablaEventos; }
    public JTable getTablaAsignados() { return tablaAsignados; }
    public JTable getTablaDisponibles() { return tablaDisponibles; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnAsignar() { return btnAsignar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public boolean isModoEdicion() { return modoEdicion; }
    
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
     * Limpia y pobla la tabla de reporteros asignados
     */
    public void setAsignados(List<ReporteroDTO> reporteros) {
        if (!modoEdicion) return;
        modeloAsignados.setRowCount(0);
        for (ReporteroDTO r : reporteros) {
            modeloAsignados.addRow(new Object[]{r.getId(), r.getNombre()});
        }
    }
    
    /**
     * Limpia y pobla la tabla de reporteros disponibles
     */
    public void setDisponibles(List<ReporteroDTO> reporteros) {
        modeloDisponibles.setRowCount(0);
        for (ReporteroDTO r : reporteros) {
            modeloDisponibles.addRow(new Object[]{false, r.getId(), r.getNombre()});
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
     * Obtiene el ID del reportero asignado seleccionado, o -1 si no hay selección
     */
    public int getIdAsignadoSeleccionado() {
        if (!modoEdicion) return -1;
        int row = tablaAsignados.getSelectedRow();
        if (row >= 0) {
            return (int) modeloAsignados.getValueAt(row, 0);
        }
        return -1;
    }
    
    /**
     * Obtiene los IDs de los reporteros marcados como seleccionados
     */
    public java.util.List<Integer> getIdsSeleccionados() {
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        for (int i = 0; i < modeloDisponibles.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) modeloDisponibles.getValueAt(i, 0);
            if (seleccionado != null && seleccionado) {
                ids.add((int) modeloDisponibles.getValueAt(i, 1));
            }
        }
        return ids;
    }
    
    /**
     * Obtiene el nombre de un reportero por su ID en la tabla de disponibles
     */
    public String getNombreDisponible(int id) {
        for (int i = 0; i < modeloDisponibles.getRowCount(); i++) {
            if ((int) modeloDisponibles.getValueAt(i, 1) == id) {
                return (String) modeloDisponibles.getValueAt(i, 2);
            }
        }
        return null;
    }
    
    /**
     * Añade un reportero a la lista de "a asignar"
     */
    public void addParaAsignar(String nombre) {
        if (!listaParaAsignar.contains(nombre)) {
            listaParaAsignar.addElement(nombre);
        }
    }
    
    /**
     * Elimina un reportero de la lista de "a asignar"
     */
    public void removeParaAsignar(String nombre) {
        listaParaAsignar.removeElement(nombre);
    }
    
    /**
     * Limpia la lista de "a asignar"
     */
    public void clearParaAsignar() {
        listaParaAsignar.clear();
    }
    
    /**
     * Obtiene los nombres en la lista de "a asignar"
     */
    public java.util.List<String> getNombresParaAsignar() {
        java.util.List<String> nombres = new java.util.ArrayList<>();
        for (int i = 0; i < listaParaAsignar.getSize(); i++) {
            nombres.add(listaParaAsignar.get(i));
        }
        return nombres;
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
    
    /**
     * Muestra un diálogo de confirmación
     */
    public boolean showConfirm(String mensaje) {
        int result = JOptionPane.showConfirmDialog(frame, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}