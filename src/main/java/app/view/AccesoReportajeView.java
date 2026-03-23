package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class AccesoReportajeView {

    private JFrame frame;
    private JScrollPane scrollMultimedia;
    private JTable table;
    private DefaultTableModel model;
    
    private DefaultListModel<String> modelMultimedia;
    private JList<String> listMultimedia;
    private JButton btnDescargarJSON;
    
    //Componentes de la vista previa
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JTextPane txtCuerpo;
    private JLabel lblMensajeVacio;
    private JScrollPane scrollTabla;
    
    
    
	
	public 	AccesoReportajeView() {
        initialize();
    }

    private void initialize() {
	frame = new JFrame();
	frame.setTitle("Acceso a reportajes");
	frame.setBounds(100,100,950,650);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setLocationRelativeTo(null); //Centrar en pantalla
	
	JPanel contentPane = new JPanel(new BorderLayout(10,10));
	contentPane.setBorder(new EmptyBorder(15,15,15,15));
	frame.setContentPane(contentPane);
	
	//Lista de eventos
	String[] columnas = {"Evento", "Fecha"};
	model = new DefaultTableModel(columnas, 0) {
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	table = new JTable(model);
	table.setRowHeight(25);
	scrollTabla = new JScrollPane(table);
	
	lblMensajeVacio = new JLabel("No hay reportajes disponibles para esta empresa");
	lblMensajeVacio.setHorizontalAlignment(JLabel.CENTER);
	lblMensajeVacio.setVerticalAlignment(JLabel.CENTER);
	lblMensajeVacio.setForeground(Color.GRAY);
	lblMensajeVacio.setFont(new Font("Segoe UI", Font.ITALIC, 14));
	lblMensajeVacio.setPreferredSize(new Dimension(0,0));
	//Visor del reportaje
	JPanel panelVisor = new JPanel(new BorderLayout(10,10));
	panelVisor.setBackground(Color.WHITE);
	panelVisor.setBorder(new EmptyBorder(10,10,10,10));
	
	JPanel panelCabecera = new JPanel(new GridLayout(0,1,5,5));
	panelCabecera.setBackground(Color.WHITE);
	
	lblTitulo = new JLabel("Seleccione un evento de la lista");
	lblTitulo.setFont(new Font("Segoe UI", Font.BOLD,22));
	
	lblSubtitulo = new JLabel("");
	lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 15));
	lblSubtitulo.setForeground(Color.DARK_GRAY);
	
	panelCabecera.add(lblTitulo);
	panelCabecera.add(lblSubtitulo);
	
	//Cuerpo del reportaje
	txtCuerpo = new JTextPane();
	txtCuerpo.setEditable(false);
	txtCuerpo.setContentType("text/plain");
	txtCuerpo.setCursor(new Cursor(Cursor.HAND_CURSOR)); //Para poder hacer después el zoom
	
	JScrollPane scrollCuerpo = new JScrollPane(txtCuerpo);
	scrollCuerpo.setBorder(null);
	
	panelVisor.add(panelCabecera, BorderLayout.NORTH);
	JPanel panelContenido = new JPanel(new GridLayout(2, 1, 0, 10));
    panelContenido.setBackground(Color.WHITE);

    panelContenido.add(scrollCuerpo); // Añadimos el scroll que ya tenías

    // Nueva Lista Multimedia
    modelMultimedia = new javax.swing.DefaultListModel<>();
    listMultimedia = new javax.swing.JList<>(modelMultimedia);
    scrollMultimedia = new JScrollPane(listMultimedia);
    scrollMultimedia = new JScrollPane(listMultimedia);
    scrollMultimedia.setBorder(javax.swing.BorderFactory.createTitledBorder("Elementos Multimedia"));
    scrollMultimedia.setVisible(false);
    panelContenido.add(scrollMultimedia);

    panelVisor.add(panelContenido, BorderLayout.CENTER);

    // Botón descargar abajo a la derecha
    JPanel panelSur = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
    panelSur.setBackground(Color.WHITE);
    btnDescargarJSON = new javax.swing.JButton("Descargar");
    btnDescargarJSON.setEnabled(false); 
    panelSur.add(btnDescargarJSON);
    panelVisor.add(panelSur, BorderLayout.SOUTH);
	
	
	
	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabla, panelVisor);
	splitPane.setDividerLocation(300);
	splitPane.setOneTouchExpandable(true);
	splitPane.setDividerSize(8);
	
	contentPane.add(splitPane, BorderLayout.CENTER);
	
    }
    
    public void mostrarMensajeVacio(boolean mostrar) {
    	if(mostrar) {
    		scrollTabla.setViewportView(lblMensajeVacio);
    		scrollTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    		scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    	}else {
    		scrollTabla.setViewportView(table);
    		scrollTabla.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    		scrollTabla.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    	}
    	scrollTabla.revalidate();
    	scrollTabla.repaint();
    }
    
    public JFrame getFrame() {
    	return frame;
    }
    
    public JTable getTable() {
    	return table;
    }
    
    public DefaultTableModel getModel() {
    	return model;
    }
    
    public JTextPane getTxtCuerpo(){
    	return txtCuerpo;
    }
    
    public javax.swing.JButton getBtnDescargarJSON() {
        return btnDescargarJSON;
    }
    
    public void actualizarReportaje(String titulo, String subtitulo, String cuerpo, java.util.List<String> archivos) {
        // Si por lo que sea scrollMultimedia aún es null, salimos para no romper el programa
        if (scrollMultimedia == null) return;

        lblTitulo.setText(titulo);
        lblSubtitulo.setText(subtitulo);
        txtCuerpo.setText(cuerpo);
        
        modelMultimedia.clear();
        boolean tieneArchivos = (archivos != null && !archivos.isEmpty());
        
        if (tieneArchivos) {
            for (String archivo : archivos) {
                modelMultimedia.addElement(archivo);
            }
        }
        
        scrollMultimedia.setVisible(tieneArchivos);
        
        // Esto hace que el panel se ajuste al nuevo tamaño
        if (scrollMultimedia.getParent() != null) {
            scrollMultimedia.getParent().revalidate();
            scrollMultimedia.getParent().repaint();
        }
        
        btnDescargarJSON.setEnabled(true);
    }
    
}
