package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
 * Vista para la asignación/modificación de reporteros a eventos.
 * Cubre HU #33537, #33543 y #34426.
 */
public class AsignacionEdicionView {
    private JFrame frame;

    // Componentes para eventos
    private JComboBox<String> cbFiltro;
    private JTable tablaEventos;
    private DefaultTableModel modeloEventos;

    // Componentes para reporteros del evento (asignados + pendientes)
    private JTable tablaReporterosEvento;
    private DefaultTableModel modeloReporterosEvento;
    private JButton btnEliminar;
    private JButton btnDesignarRR;
    private JButton btnFinalizar;
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
        frame.setTitle("Asignar / Modificar Reporteros");
        frame.setBounds(100, 100, 780, 680);
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

        modeloEventos = new DefaultTableModel(new String[]{"ID", "Nombre", "Fecha", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEventos = new JTable(modeloEventos);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEventos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaEventos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaEventos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaEventos.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Renderer para colorear eventos finalizados
        tablaEventos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String estado = (String) table.getValueAt(row, 3);
                if (!isSelected) {
                    if ("FINALIZADA".equals(estado)) {
                        c.setBackground(new Color(220, 220, 240));
                        c.setForeground(new Color(100, 100, 140));
                        if (c instanceof JLabel) {
                            ((JLabel) c).setFont(c.getFont().deriveFont(Font.ITALIC));
                        }
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                        if (c instanceof JLabel) {
                            ((JLabel) c).setFont(c.getFont().deriveFont(Font.PLAIN));
                        }
                    }
                }
                return c;
            }
        });

        JScrollPane scrollEventos = new JScrollPane(tablaEventos);
        scrollEventos.setPreferredSize(new Dimension(740, 120));
        panelEventos.add(scrollEventos);
        mainPanel.add(panelEventos);

        // === PANEL REPORTEROS DEL EVENTO ===
        JPanel panelReporterosEvento = new JPanel();
        panelReporterosEvento.setLayout(new BoxLayout(panelReporterosEvento, BoxLayout.Y_AXIS));
        panelReporterosEvento.setBorder(BorderFactory.createTitledBorder("Reporteros del Evento"));

        // Leyenda
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblAsignado = new JLabel("■ Asignado");
        lblAsignado.setForeground(new Color(0, 100, 0));
        JLabel lblPendiente = new JLabel("    ■ Pendiente");
        lblPendiente.setForeground(new Color(200, 100, 0));
        JLabel lblRR = new JLabel("    ★ Responsable (RR)");
        lblRR.setForeground(new Color(0, 0, 180));
        panelLeyenda.add(lblAsignado);
        panelLeyenda.add(lblPendiente);
        panelLeyenda.add(lblRR);
        panelReporterosEvento.add(panelLeyenda);

        modeloReporterosEvento = new DefaultTableModel(new String[]{"Estado", "ID", "Nombre", "Tipo", "RR"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaReporterosEvento = new JTable(modeloReporterosEvento);
        tablaReporterosEvento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaReporterosEvento.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaReporterosEvento.getColumnModel().getColumn(1).setPreferredWidth(40);
        tablaReporterosEvento.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaReporterosEvento.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaReporterosEvento.getColumnModel().getColumn(4).setPreferredWidth(50);

        // Renderer para colorear filas según estado y RR
        tablaReporterosEvento.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String estado = (String) table.getValueAt(row, 0);
                    String rr = (String) table.getValueAt(row, 4);
                    if ("★ RR".equals(rr)) {
                        c.setBackground(new Color(210, 220, 255));
                        c.setForeground(new Color(0, 0, 180));
                    } else if ("Asignado".equals(estado)) {
                        c.setBackground(new Color(220, 255, 220));
                        c.setForeground(new Color(0, 100, 0));
                    } else if ("Pendiente".equals(estado)) {
                        c.setBackground(new Color(255, 250, 220));
                        c.setForeground(new Color(200, 100, 0));
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                return c;
            }
        });

        scrollReporterosEvento = new JScrollPane(tablaReporterosEvento);
        scrollReporterosEvento.setPreferredSize(new Dimension(740, 120));
        panelReporterosEvento.add(scrollReporterosEvento);

        // Botones de reporteros del evento
        JPanel panelBtnReporteros = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDesignarRR = new JButton("Designar como RR");
        btnEliminar = new JButton("Eliminar Seleccionado");
        btnFinalizar = new JButton("Finalizar Asignación");
        panelBtnReporteros.add(btnDesignarRR);
        panelBtnReporteros.add(btnEliminar);
        panelBtnReporteros.add(btnFinalizar);
        panelReporterosEvento.add(panelBtnReporteros);

        mainPanel.add(panelReporterosEvento);

        // === PANEL REPORTEROS DISPONIBLES ===
        JPanel panelDisponibles = new JPanel();
        panelDisponibles.setLayout(new BoxLayout(panelDisponibles, BoxLayout.Y_AXIS));
        panelDisponibles.setBorder(BorderFactory.createTitledBorder("Reporteros Disponibles (marque para añadir al evento)"));

        // Filtros
        JPanel panelFiltrosDisp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkFiltroTematica = new JCheckBox("Filtrar por temática del evento");
        panelFiltrosDisp.add(chkFiltroTematica);
        panelFiltrosDisp.add(new JLabel("  Tipo:"));
        cbFiltroTipo = new JComboBox<>(new String[]{"Todos", "GRAFICO", "CAMAROGRAFO", "BASE"});
        cbFiltroTipo.setSelectedIndex(0);
        panelFiltrosDisp.add(cbFiltroTipo);
        panelDisponibles.add(panelFiltrosDisp);

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
                return column == 0;
            }
        };
        tablaDisponibles = new JTable(modeloDisponibles);
        tablaDisponibles.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaDisponibles.getColumnModel().getColumn(1).setPreferredWidth(40);
        tablaDisponibles.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaDisponibles.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollDisponibles = new JScrollPane(tablaDisponibles);
        scrollDisponibles.setPreferredSize(new Dimension(740, 130));
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
    public JButton getBtnDesignarRR() { return btnDesignarRR; }
    public JButton getBtnFinalizar() { return btnFinalizar; }
    public JButton getBtnAsignar() { return btnAsignar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JCheckBox getChkFiltroTematica() { return chkFiltroTematica; }
    public JComboBox<String> getCbFiltroTipo() { return cbFiltroTipo; }
    public JLabel getLblTematicasEvento() { return lblTematicasEvento; }

    // === MÉTODOS PARA POBLAR DATOS ===

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
     * Limpia y pobla la tabla de eventos. Muestra el estado de finalización.
     */
    public void setEventos(List<EventoDTO> eventos) {
        modeloEventos.setRowCount(0);
        for (EventoDTO e : eventos) {
            String estado = e.isAsignacionFinalizada() ? "FINALIZADA" : "Abierta";
            modeloEventos.addRow(new Object[]{e.getId(), e.getNombre(), e.getFecha(), estado});
        }
    }

    /**
     * Establece los reporteros asignados al evento, con info de RR.
     * @param datos lista de Object[] con: id, nombre, tipo, id_agencia, es_responsable
     */
    public void setAsignadosConRR(List<Object[]> datos) {
        idsAsignados.clear();
        idsPendientes.clear();
        modeloReporterosEvento.setRowCount(0);

        for (Object[] row : datos) {
            int id = ((Number) row[0]).intValue();
            String nombre = (String) row[1];
            String tipo = (String) row[2];
            boolean esRR = row[4] != null && (row[4] instanceof Boolean ? (Boolean) row[4] : ((Number) row[4]).intValue() != 0);

            idsAsignados.add(id);
            modeloReporterosEvento.addRow(new Object[]{
                "Asignado", id, nombre, tipo, esRR ? "★ RR" : ""
            });
        }
    }

    public boolean addPendiente(int id, String nombre, String tipo) {
        if (idsAsignados.contains(id) || idsPendientes.contains(id)) {
            return false;
        }
        idsPendientes.add(id);
        modeloReporterosEvento.addRow(new Object[]{"Pendiente", id, nombre, tipo, ""});
        return true;
    }

    public boolean removePendiente(int id) {
        if (!idsPendientes.contains(id)) {
            return false;
        }
        idsPendientes.remove(id);
        for (int i = 0; i < modeloReporterosEvento.getRowCount(); i++) {
            if (((Number) modeloReporterosEvento.getValueAt(i, 1)).intValue() == id) {
                modeloReporterosEvento.removeRow(i);
                break;
            }
        }
        return true;
    }

    public boolean isAsignado(int id) { return idsAsignados.contains(id); }
    public boolean isPendiente(int id) { return idsPendientes.contains(id); }

    public void setDisponibles(List<ReporteroDTO> reporteros) {
        modeloDisponibles.setRowCount(0);
        for (ReporteroDTO r : reporteros) {
            boolean marcado = idsPendientes.contains(r.getId());
            modeloDisponibles.addRow(new Object[]{marcado, r.getId(), r.getNombre(), r.getTipo()});
        }
    }

    public int getIdEventoSeleccionado() {
        int row = tablaEventos.getSelectedRow();
        if (row >= 0) {
            return ((Number) modeloEventos.getValueAt(row, 0)).intValue();
        }
        return -1;
    }

    public String getEstadoEventoSeleccionado() {
        int row = tablaEventos.getSelectedRow();
        if (row >= 0) {
            return (String) modeloEventos.getValueAt(row, 3);
        }
        return null;
    }

    public int getIdReporteroEventoSeleccionado() {
        int row = tablaReporterosEvento.getSelectedRow();
        if (row >= 0) {
            return ((Number) modeloReporterosEvento.getValueAt(row, 1)).intValue();
        }
        return -1;
    }

    public String getEstadoReporteroEventoSeleccionado() {
        int row = tablaReporterosEvento.getSelectedRow();
        if (row >= 0) {
            return (String) modeloReporterosEvento.getValueAt(row, 0);
        }
        return null;
    }

    public List<Integer> getIdsSeleccionadosDisponibles() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < modeloDisponibles.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) modeloDisponibles.getValueAt(i, 0);
            if (seleccionado != null && seleccionado) {
                ids.add(((Number) modeloDisponibles.getValueAt(i, 1)).intValue());
            }
        }
        return ids;
    }

    public String getNombreDisponible(int id) {
        for (int i = 0; i < modeloDisponibles.getRowCount(); i++) {
            if (((Number) modeloDisponibles.getValueAt(i, 1)).intValue() == id) {
                return (String) modeloDisponibles.getValueAt(i, 2);
            }
        }
        return null;
    }

    public String getTipoDisponible(int id) {
        for (int i = 0; i < modeloDisponibles.getRowCount(); i++) {
            if (((Number) modeloDisponibles.getValueAt(i, 1)).intValue() == id) {
                return (String) modeloDisponibles.getValueAt(i, 3);
            }
        }
        return null;
    }

    public String getSelectedTipo() {
        String sel = (String) cbFiltroTipo.getSelectedItem();
        return "Todos".equals(sel) ? null : sel;
    }

    public List<Integer> getIdsPendientes() {
        return new ArrayList<>(idsPendientes);
    }

    public List<Integer> getIdsAsignados() {
        return new ArrayList<>(idsAsignados);
    }

    public void clearPendientes() {
        idsPendientes.clear();
    }

    public void setCheckboxDisponible(int id, boolean marcado) {
        for (int i = 0; i < modeloDisponibles.getRowCount(); i++) {
            if (((Number) modeloDisponibles.getValueAt(i, 1)).intValue() == id) {
                modeloDisponibles.setValueAt(marcado, i, 0);
                break;
            }
        }
    }

    /**
     * Habilita/deshabilita los controles de edición.
     * Cuando un evento está finalizado, se deshabilitan.
     */
    public void setEdicionHabilitada(boolean habilitada) {
        btnAsignar.setEnabled(habilitada);
        btnEliminar.setEnabled(habilitada);
        btnDesignarRR.setEnabled(habilitada);
        btnFinalizar.setEnabled(habilitada);
        chkFiltroTematica.setEnabled(habilitada);
        cbFiltroTipo.setEnabled(habilitada);

        // Hacer que los checkboxes de disponibles no sean editables
        modeloDisponibles = new DefaultTableModel(
            new String[]{"Añadir", "ID", "Nombre", "Tipo"}, 0) {
            final boolean editable = habilitada;
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return editable && column == 0;
            }
        };
        tablaDisponibles.setModel(modeloDisponibles);
        tablaDisponibles.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaDisponibles.getColumnModel().getColumn(1).setPreferredWidth(40);
        tablaDisponibles.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaDisponibles.getColumnModel().getColumn(3).setPreferredWidth(100);
    }

    public void showError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean showConfirm(String mensaje) {
        int result = JOptionPane.showConfirmDialog(frame, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}
