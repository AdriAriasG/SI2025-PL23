package app.controller;

import app.dto.ReportajeDTO;
import app.model.AccesoReportajeModel;
import app.view.AccesoReportajeView;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JOptionPane;

public class AccesoReportajeController {
	
	private AccesoReportajeView view;
	private AccesoReportajeModel model;
	private List<ReportajeDTO> reportajesActuales;
	private int idEmpresa;
	
	public AccesoReportajeController(AccesoReportajeView view, AccesoReportajeModel model, int idEmpresa){
		this.view = view;
		this.model = model;
		this.idEmpresa = idEmpresa;
		this.initController();
	}
	
	public void initController() {
		cargarTabla();
		
		view.getTable().getSelectionModel().addListSelectionListener(e->{
			if(!e.getValueIsAdjusting()) {
				actualizarVisor();
			}
		});
		
		view.getTxtCuerpo().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					abrirZoom();
				}
			}
		});	
	}
	
	
	public void cargarTabla(){
		reportajesActuales = model.getReportajesConAcceso(this.idEmpresa);
		view.getModel().setRowCount(0);
		
		if(reportajesActuales.isEmpty()) {
			view.mostrarMensajeVacio(true);
		}
		else {
			
			view.mostrarMensajeVacio(false);
			
			for(ReportajeDTO r: reportajesActuales) {
				view.getModel().addRow(new Object[] {
						r.getNombreEvento(),
						r.getFecha()
				});
			}
		}
	}
	
	private void actualizarVisor() {
		int fila = view.getTable().getSelectedRow();
		if (fila != -1) {
			ReportajeDTO seleccionado = reportajesActuales.get(fila);
			view.actualizarReportaje(
					seleccionado.getTitulo(),
					seleccionado.getSubtitulo(),
					seleccionado.getCuerpo());
		}
	}
	
	private void abrirZoom() {
		int fila = view.getTable().getSelectedRow();
	    if (fila == -1) return;

	    ReportajeDTO sel = reportajesActuales.get(fila);

	    // 1. Creamos la ventana de Zoom (JDialog)
	    JDialog dialogZoom = new JDialog(view.getFrame(), "Vista Ampliada", true);
	    dialogZoom.setSize(800, 600);
	    dialogZoom.setLocationRelativeTo(view.getFrame());
	    dialogZoom.setLayout(new BorderLayout(15, 15));

	    // 2. Área de texto configurada para lectura fácil
	    JTextPane txtZoom = new JTextPane();
	    txtZoom.setEditable(false);
	    txtZoom.setMargin(new java.awt.Insets(20, 20, 20, 20));

	    // 3. Dar formato al texto con HTML para que sea GIGANTE
	    String htmlText = "<html><body style='font-family: Arial; padding: 10px;'>" +
	                      "<h1 style='color: #2c3e50; font-size: 26px;'>" + sel.getTitulo() + "</h1>" +
	                      "<h3 style='color: #7f8c8d; font-size: 18px; font-style: italic;'>" + sel.getSubtitulo() + "</h3>" +
	                      "<hr><br>" +
	                      "<p style='font-size: 20px; line-height: 1.5;'>" + sel.getCuerpo() + "</p>" +
	                      "</body></html>";
	    
	    txtZoom.setContentType("text/html");
	    txtZoom.setText(htmlText);

	    // 4. Scroll y botón de cerrar
	    JScrollPane scroll = new JScrollPane(txtZoom);
	    JButton btnCerrar = new JButton("Cerrar vista ampliada");
	    btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
	    btnCerrar.addActionListener(e -> dialogZoom.dispose());

	    dialogZoom.add(scroll, BorderLayout.CENTER);
	    dialogZoom.add(btnCerrar, BorderLayout.SOUTH);

	    // 5. Mostrar
	    dialogZoom.setVisible(true);
	}
	
	public void mostrarVista() {
		view.getFrame().setVisible(true);
	}
	
	
}
