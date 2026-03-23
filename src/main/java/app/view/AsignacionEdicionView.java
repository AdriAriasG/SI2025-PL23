package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import app.dto.EventoDTO;
import app.dto.ReporteroDTO;

/**
 * Vista para la modificación de asignación de reporteros a eventos.
 * Cubre la HU #33543 (modo edición).
 * 
 * En esta vista, los reporteros seleccionados se añaden directamente a la tabla
 * de reporteros del evento (diferenciados visualmente), y solo se guardan en BD
 * cuando el usuario pulsa "Asignar".
 */
public class AsignacionEdicionView {
    private JFrame frame;
    
    // Componentes para eventos
    private JComboBox<String> cbFiltro;
    private JTable tablaEventos;
    private DefaultTableModel modeloEventos;
    
    // Componentes para reporteros del evento (asignados + pendientes de asignar)
    private JTable tablaReporterosEvento;
    private DefaultTableModel modeloReporterosEvento;
    private JButton btnEliminar;
    private JScrollPane scrollReporterosEvento;
    
    // IDs de reporteros ya asignados (cargados de BD)
    private Set<Integer> idsAsignados = new HashSet<>();
    
    // IDs de reporteros pendientes de asignar (seleccionados pero no guardados)
    private Set<Integer> idsPendientes = new HashSet<>();
    
    // Componentes para reporteros disponibles
    private JTable tablaDisponibles;
    private DefaultTableModel modeloDisponibles;
    private JCheckBox chkFiltroTematica;
    private JComboBox<String> cbFiltroTipo;
    private JLabel lblTematicasEvento;
    
    // Botones
    private JButton btnAsignar;
    private JButton btnCancelar;
    
    public AsignacionEdicionView() {
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Modificar Asignación de Reporteros");
        frame.setBounds(100, 100, 750, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // === PANEL FILTRO ===
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.add(new JLabel("Filtro:"));
        cbFiltro = new JComboBox<>(new String[]{"Sin asignar", "Con asignados"});
        cbFiltro.setSelectedIndex(0);
        panelFiltro.add(cbFiltro);
        mainPanel.add(panelFiltro);
        
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
        
        // === PANEL REPORTEROS DEL EVENTO (ASIGNADOS + PENDIENTES) ===
        JPanel panelReporterosEvento = new JPanel();
        panelReporterosEvento.setLayout(new BoxLayout(panelReporterosEvento, BoxLayout.Y_AXIS));
        panelReporterosEvento.setBorder(BorderFactory.createTitledBorder("Reporteros del Evento"));
        
        // Añadir leyenda
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblLeyenda = new JLabel("Leyenda: ");
        JLabel lblAsignado = new JLabel("■ Asignado");
        lblAsignado.setForeground(new Color(0, 100, 0));
        JLabel lblPendiente = new JLabel("    ■ Pendiente de asignar");
        lblPendiente.setForeground(new Color(200, 100, 0));
        panelLeyenda.add(lblLeyenda);
        panelLeyenda.add(lblAsignado);
        panelLeyenda.add(lblPendiente);
        panelReporterosEvento.add(panelLeyenda);
        
        modeloReporterosEvento = new DefaultTableModel(new String[]{"Estado", "ID", "Nombre", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaReporterosEvento = new JTable(modeloReporterosEvento);
        tablaReporterosEvento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaReporterosEvento.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaReporterosEvento.getColumnModel().getColumn(1).setPreferredWidth(40);
        tablaReporterosEvento.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaReporterosEvento.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        // Renderer para colorear las filas según estado
        tablaReporterosEvento.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String estado = (String) table.getValueAt(row, 0);
                if (!isSelected) {
                    if ("Asignado".equals(estado)) {
                        c.setBackground(new Color(220, 255, 220)); // Verde claro
                        c.setForeground(new Color(0, 100, 0));
                    } else if ("Pendiente".equals(estado)) {
                        c.setBackground(new Color(255, 250, 220)); // Amarillo claro
                        c.setForeground(new Color(200, 100, 0));
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                return c;
            }
        });
        
        scrollReporterosEvento = new JScrollPane(tablaReporterosEvento);
        scrollReporterosEvento.setPreferredSize(new Dimension(700, 120));
        panelReporterosEvento.add(scrollReporterosEvento);
        
        // Botón eliminar
        JPanel panelBtnEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEliminar = new JButton("Eliminar Seleccionado");
        panelBtnEliminar.add(btnEliminar);
        panelReporterosEvento.add(panelBtnEliminar);
        
        mainPanel.add(panelReporterosEvento);
        
        // === PANEL REPORTEROS DISPONIBLES ===
        JPanel panelDisponibles = new JPanel();
        panelDisponibles.setLayout(new BoxLayout(panelDisponibles, BoxLayout.Y_AXIS));
        panelDisponibles.setBorder(BorderFactory.createTitledBorder("Reporteros Disponibles (marque para añadir al evento)"));
        
        // Subpanel para filtros de disponibilidad
        JPanel panelFiltrosDisp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkFiltroTematica = new JCheckBox("Filtrar por temática del evento");
        panelFiltrosDisp.add(chkFiltroTematica);
        
        panelFiltrosDisp.add(new JLabel("  Tipo:"));
        cbFiltroTipo = new JComboBox<>(new String[]{"Todos", "GRAFICO", "CAMAROGRAFO", "BASE"});
        cbFiltroTipo.setSelectedIndex(0); // "Todos" por defecto
        panelFiltrosDisp.add(cbFiltroTipo);
        
        panelDisponibles.add(panelFiltrosDisp);
        
        // Label para temáticas del evento
        lblTematicasEvento = new JLabel("Temáticas del evento: -");
        JPanel panelTematicas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTematicas.add(lblTematicasEvento);
        panelDisponibles.add(panelTematicas);
        
        modeloDisponibles = new DefaultTableModel(new String[]{"Añadir", "ID", "Nombre", "Tipo"}, 0) {
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
        tablaDisponibles.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaDisponibles.getColumnModel().getColumn(1).setPreferredWidth(40);
        tablaDisponibles.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaDisponibles.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        JScrollPane scrollDisponibles = new JScrollPane(tablaDisponibles);
        scrollDisponibles.setPreferredSize(new Dimension(700, 150));
        panelDisponibles.add(scrollDisponibles);
        mainPanel.add(panelDisponibles);
        
        // === PANEL BOTONES ===
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAsignar = new JButton("Asignar");
        btnCancelar = new JButton("Cerrar");
        panelBotones.add(btnAsignar);
        panelBotones.add(btnCancelar);
        mainPanel.add(panelBotones);
    }
    
    // === GETTERS ===
    public JFrame getFrame() { return frame; }
    public JComboBox<String> getCbFiltro() { return cbFiltro; }
    public JTable getTablaEventos() { return tablaEventos; }
    public JTable getTablaReporterosEvento() { return tablaReporterosEvento; }
    public JTable getTablaDisponibles() { return tablaDisponibles; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnAsignar() { return btnAsignar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JCheckBox getChkFiltroTematica() { return chkFiltroTematica; }
    public JComboBox<String> getCbFiltroTipo() { return cbFiltroTipo; }
    public JLabel getLblTematicasEvento() { return lblTematicasEvento; }
    
    // === MÉTODOS PARA POBLAR DATOS ===
    
    /**
     * Establece las temáticas del evento en el label
     */
    public void setTematicas(List<app.dto.TematicaDTO> tematicas) {
        if (tematicas == null || tematicas.isEmpty()) {
            lblTematicasEvento.setText("Temáticas del evento: Ninguna");
        } else {
            StringBuilder sb = new StringBuilder("Temáticas del evento: ");
            for (int i = 0; i < tematicas.size(); i++) {
                sb.append(tematicas.get(i).getNombre());
                if (i < tematicas.size() - 1) sb.append(", ");
            }
            lblTematicasEvento.setText(sb.toString());
        }
    }
    
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
     * Establece los reporteros ya asignados al evento.
     * Esto limpia tanto los asignados como los pendientes.
     */
    public void setAsignados(List<ReporteroDTO> reporteros) {
        idsAsignados.clear();
        idsPendientes.clear();
        modeloReporterosEvento.setRowCount(0);
        
        for (ReporteroDTO r : reporteros) {
            idsAsignados.add(r.getId());
            modeloReporterosEvento.addRow(new Object[]{"Asignado", r.getId(), r.getNombre(), r.getTipo()});
        }
    }
    
    /**
     * Añade un reportero como pendiente de asignar a la tabla del evento.
     * @return true si se añadió, false si ya estaba
     */
    public boolean addPendiente(int id, String nombre, String tipo) {
        // Verificar si ya está en asignados o pendientes
        if (idsAsignados.contains(id) || idsPendientes.contains(id)) {
            return false;
        }
        
        idsPendientes.add(id);
        modeloReporterosEvento.addRow(new Object[]{"Pendiente", id, nombre, tipo});
        return true;
    }
    
    /**
     * Elimina un reportero pendiente de la tabla del evento.
     * @return true si se eliminó, false si no era pendiente
     */
    public boolean removePendiente(int id) {
        if (!idsPendientes.contains(id)) {
            return false;
        }
        
        idsPendientes.remove(id);
        
        // Buscar y eliminar la fila
        for (int i = 0; i < modeloReporterosEvento.getRowCount(); i++) {
            if ((int) modeloReporterosEvento.getValueAt(i, 1) == id) {
                modeloReporterosEvento.removeRow(i);
                break;
            }
        }
        return true;
    }
    
    /**
     * Verifica si un ID está en los asignados
     */
    public boolean isAsignado(int id) {
        return idsAsignados.contains(id);
    }
    
    /**
     * Verifica si un ID está en los pendientes
     */
    public boolean isPendiente(int id) {
        return idsPendientes.contains(id);
    }
    
    /**
     * Limpia y pobla la tabla de reporteros disponibles.
     * Marca con check los que están pendientes.
     */
    public void setDisponibles(List<ReporteroDTO> reporteros) {
        modeloDisponibles.setRowCount(0);
        for (ReporteroDTO r : reporteros) {
            // Marcar si está en pendientes
            boolean marcado = idsPendientes.contains(r.getId());
            modeloDisponibles.addRow(new Object[]{marcado, r.getId(), r.getNombre(), r.getTipo()});
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
     * Obtiene el ID del reportero seleccionado en la tabla del evento, o -1 si no hay selección
     */
    public int getIdReporteroEventoSeleccionado() {
        int row = tablaReporterosEvento.getSelectedRow();
        if (row >= 0) {
            return (int) modeloReporterosEvento.getValueAt(row, 1);
        }
        return -1;
    }
    
    /**
     * Obtiene el estado del reportero seleccionado en la tabla del evento
     */
    public String getEstadoReporteroEventoSeleccionado() {
        int row = tablaReporterosEvento.getSelectedRow();
        if (row >= 0) {
            return (String) modeloReporterosEvento.getValueAt(row, 0);
        }
        return null;
    }
    
    /**
     * Obtiene los IDs de los reporteros marcados como seleccionados en disponibles
     */
    public List<Integer> getIdsSeleccionadosDisponibles() {
        List<Integer> ids = new ArrayList<>();
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
     * Obtiene el tipo de un reportero por su ID en la tabla de disponibles
     */
    public String getTipoDisponible(int id) {
        for (int i = 0; i < modeloDisponibles.getRowCount(); i++) {
            if ((int) modeloDisponibles.getValueAt(i, 1) == id) {
                return (String) modeloDisponibles.getValueAt(i, 3);
            }
        }
        return null;
    }

    /**
     * Obtiene el tipo seleccionado en el combo de filtro
     */
    public String getSelectedTipo() {
        String sel = (String) cbFiltroTipo.getSelectedItem();
        return "Todos".equals(sel) ? null : sel;
    }
    
    /**
     * Obtiene los IDs de los reporteros pendientes de asignar
     */
    public List<Integer> getIdsPendientes() {
        return new ArrayList<>(idsPendientes);
    }
    
    /**
     * Obtiene los IDs de los reporteros ya asignados
     */
    public List<Integer> getIdsAsignados() {
        return new ArrayList<>(idsAsignados);
    }
    
    /**
     * Limpia los pendientes (usado al cambiar de evento o guardar)
     */
    public void clearPendientes() {
        idsPendientes.clear();
    }
    
    /**
     * Actualiza el checkbox de un reportero en disponibles
     */
    public void setCheckboxDisponible(int id, boolean marcado) {
        for (int i = 0; i < modeloDisponibles.getRowCount(); i++) {
            if ((int) modeloDisponibles.getValueAt(i, 1) == id) {
                modeloDisponibles.setValueAt(marcado, i, 0);
                break;
            }
        }
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