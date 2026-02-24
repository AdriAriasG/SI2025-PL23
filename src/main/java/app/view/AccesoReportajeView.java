package app.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class AccesoReportajeView {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    
    //Componentes de la vista previa
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JTextPane txtCuerpo;
    
	
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
	JScrollPane scrollTabla = new JScrollPane(table);
	
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
	panelVisor.add(scrollCuerpo, BorderLayout.CENTER);
	
	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabla, panelVisor);
	splitPane.setDividerLocation(300);
	splitPane.setOneTouchExpandable(true);
	splitPane.setDividerSize(8);
	
	contentPane.add(splitPane, BorderLayout.CENTER);
	
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
    
    public void actualizarReportaje(String titulo, String subtitulo, String cuerpo) {
    	lblTitulo.setText(titulo);
    	lblSubtitulo.setText(subtitulo);
    	txtCuerpo.setText(cuerpo);
    	txtCuerpo.setCaretPosition(0);
    }
    
}
