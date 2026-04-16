package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AccesoReportajeView {

    private JFrame frame;
    private JScrollPane scrollMultimedia;
    private JTable table;
    private DefaultTableModel model;
    
    private DefaultListModel<String> modelMultimedia;
    private JList<String> listMultimedia;
    private JButton btnDescargarJSON;
    
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JTextPane txtCuerpo;
    private JLabel lblMensajeVacio;
    private JScrollPane scrollTabla;
    
    private JPanel panelContenido;

    public AccesoReportajeView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Acceso a reportajes");
        frame.setBounds(100, 100, 950, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        frame.setContentPane(contentPane);
        
        // Tabla de eventos
        String[] columnas = {"Evento", "Fecha"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        scrollTabla = new JScrollPane(table);
        
        lblMensajeVacio = new JLabel("No hay reportajes disponibles para esta empresa");
        lblMensajeVacio.setHorizontalAlignment(JLabel.CENTER);
        lblMensajeVacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblMensajeVacio.setForeground(Color.GRAY);

        // Visor del reportaje
        JPanel panelVisor = new JPanel(new BorderLayout(10, 10));
        panelVisor.setBackground(Color.WHITE);
        panelVisor.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel panelCabecera = new JPanel(new GridLayout(0, 1, 5, 5));
        panelCabecera.setBackground(Color.WHITE);
        
        lblTitulo = new JLabel("Seleccione un evento de la lista");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        lblSubtitulo = new JLabel("");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblSubtitulo.setForeground(Color.DARK_GRAY);
        
        panelCabecera.add(lblTitulo);
        panelCabecera.add(lblSubtitulo);
        
        txtCuerpo = new JTextPane();
        txtCuerpo.setEditable(false);
        txtCuerpo.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        
        JScrollPane scrollCuerpo = new JScrollPane(txtCuerpo);
        scrollCuerpo.setBorder(null);
        
        panelContenido = new JPanel(new BorderLayout(0, 10));
        panelContenido.setBackground(Color.WHITE);
        panelContenido.add(scrollCuerpo, BorderLayout.CENTER);

        // Lista Multimedia
        modelMultimedia = new DefaultListModel<>();
        listMultimedia = new JList<>(modelMultimedia);
        scrollMultimedia = new JScrollPane(listMultimedia);
        scrollMultimedia.setBorder(javax.swing.BorderFactory.createTitledBorder("Elementos Multimedia"));
        scrollMultimedia.setPreferredSize(new Dimension(0, 150));
        scrollMultimedia.setVisible(false);
        
        panelContenido.add(scrollMultimedia, BorderLayout.SOUTH);

        panelVisor.add(panelCabecera, BorderLayout.NORTH);
        panelVisor.add(panelContenido, BorderLayout.CENTER);

        // Botón descargar
        JPanel panelSur = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);
        btnDescargarJSON = new JButton("Descargar");
        btnDescargarJSON.setEnabled(false); 
        panelSur.add(btnDescargarJSON);
        panelVisor.add(panelSur, BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabla, panelVisor);
        splitPane.setDividerLocation(300);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }
    
    /**
     * Lógica central de visualización con gestión de embargos.
     */
    public void actualizarReportaje(String titulo, String subtitulo, String cuerpo, 
                                   List<String> archivos, String fechaEmbargoStr, boolean accesoEspecial) {
        
        // 1. Limpieza inicial
        modelMultimedia.clear();
        btnDescargarJSON.setEnabled(false);
        scrollMultimedia.setVisible(false);

        // 2. Conversión de la fecha String a Date para la comparación
        Date fechaEmbargo = null;
        try {
            if (fechaEmbargoStr != null && !fechaEmbargoStr.isEmpty()) {
                // Ajustamos el formato al estándar de SQLite y tu script anterior
                fechaEmbargo = new SimpleDateFormat("yyyy-MM-dd").parse(fechaEmbargoStr);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar la fecha de embargo: " + e.getMessage());
        }

        // 3. Evaluar embargo
        boolean tieneEmbargoVigente = (fechaEmbargo != null && fechaEmbargo.after(new Date()));

        if (!tieneEmbargoVigente) {
            // ESCENARIO 1: Acceso total
            lblTitulo.setText(titulo);
            lblSubtitulo.setText(subtitulo);
            txtCuerpo.setText(cuerpo);
            
            if (archivos != null && !archivos.isEmpty()) {
                for (String archivo : archivos) modelMultimedia.addElement(archivo);
                scrollMultimedia.setVisible(true);
            }
            btnDescargarJSON.setEnabled(true);

        } else if (accesoEspecial) {
            // ESCENARIO 2: Embargo vigente CON acceso especial -> Solo Texto
            lblTitulo.setText(titulo);
            lblSubtitulo.setText(subtitulo);
            txtCuerpo.setText(cuerpo);
            // Multimedia y descarga invisibles por el paso 1
            
        } else {
            // ESCENARIO 3: Embargo vigente SIN acceso especial -> Bloqueo
            lblTitulo.setText("ACCESO DENEGADO");
            lblSubtitulo.setText("Reportaje embargado hasta: " + (fechaEmbargoStr != null ? fechaEmbargoStr : "desconocido"));
            txtCuerpo.setText("El contenido de este reportaje no está disponible debido a un embargo vigente.");
        }

        // 4. Refresco de UI
        panelContenido.revalidate();
        panelContenido.repaint();
        frame.repaint();
    }

    public void mostrarMensajeVacio(boolean mostrar) {
        if(mostrar) {
            scrollTabla.setViewportView(lblMensajeVacio);
        } else {
            scrollTabla.setViewportView(table);
        }
        scrollTabla.revalidate();
        scrollTabla.repaint();
    }

    // Getters
    public JFrame getFrame() { return frame; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JTextPane getTxtCuerpo(){ return txtCuerpo; }
    public JButton getBtnDescargarJSON() { return btnDescargarJSON; }
}