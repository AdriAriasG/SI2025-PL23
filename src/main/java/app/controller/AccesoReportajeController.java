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
	    // 1. Cargar los datos iniciales en la tabla
	    cargarTabla();

	    // 2. Configurar el listener del botón de descarga JSON
	    configurarBotonDescarga();

	    // 3. Configurar el listener de selección de la tabla
	    // Este permite que al pinchar en una fila se muestre el contenido a la derecha
	    view.getTable().getSelectionModel().addListSelectionListener(e -> {
	        // getValueIsAdjusting() evita que el evento se dispare dos veces (al pulsar y al soltar)
	        if (!e.getValueIsAdjusting()) {
	            actualizarVisor();
	        }
	    });

	    // 4. Hacer visible la ventana (si no se hace en el SwingMain)
	    view.getFrame().setVisible(true);
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
            // Pasamos los 4 parámetros: titulo, subtitulo, cuerpo y la LISTA de archivos
            view.actualizarReportaje(
                    seleccionado.getTitulo(),
                    seleccionado.getSubtitulo(),
                    seleccionado.getCuerpo(),
                    seleccionado.getArchivosMultimedia());
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
	
	
	private void configurarBotonDescarga() {
	    view.getBtnDescargarJSON().addActionListener(e -> {
	        int fila = view.getTable().getSelectedRow();
	        if (fila == -1) {
	            JOptionPane.showMessageDialog(view.getFrame(), "Por favor, selecciona un reportaje de la lista.");
	            return;
	        }

	        ReportajeDTO sel = reportajesActuales.get(fila);
	        
	        // 1. Construcción manual del JSON
	        StringBuilder json = new StringBuilder();
	        json.append("{\n");
	        json.append("  \"evento\": \"").append(sel.getNombreEvento()).append("\",\n");
	        json.append("  \"fecha\": \"").append(sel.getFecha()).append("\",\n");
	        json.append("  \"titulo\": \"").append(sel.getTitulo()).append("\",\n");
	        json.append("  \"subtitulo\": \"").append(sel.getSubtitulo()).append("\",\n");
	        // Escapamos los saltos de línea del cuerpo para que el JSON sea válido
	        String cuerpoEscapado = sel.getCuerpo().replace("\"", "\\\"").replace("\n", "\\n");
	        json.append("  \"cuerpo\": \"").append(cuerpoEscapado).append("\",\n");
	        
	        json.append("  \"multimedia\": [\n");
	        for (int i = 0; i < sel.getArchivosMultimedia().size(); i++) {
	            json.append("    \"").append(sel.getArchivosMultimedia().get(i)).append("\"");
	            if (i < sel.getArchivosMultimedia().size() - 1) json.append(",");
	            json.append("\n");
	        }
	        json.append("  ]\n");
	        json.append("}");

	        // 2. Diálogo para guardar el archivo
	        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
	        fileChooser.setDialogTitle("Guardar Reportaje como JSON");
	        // Nombre por defecto sugerido
	        String nombreSugerido = "reportaje_" + sel.getNombreEvento().replaceAll("\\s+", "_") + ".json";
	        fileChooser.setSelectedFile(new java.io.File(nombreSugerido));

	        if (fileChooser.showSaveDialog(view.getFrame()) == javax.swing.JFileChooser.APPROVE_OPTION) {
	            java.io.File archivoDestino = fileChooser.getSelectedFile();
	            
	            // Asegurar que tenga extensión .json
	            if (!archivoDestino.getName().toLowerCase().endsWith(".json")) {
	                archivoDestino = new java.io.File(archivoDestino.getAbsolutePath() + ".json");
	            }

	            try (java.io.FileWriter writer = new java.io.FileWriter(archivoDestino)) {
	                writer.write(json.toString());
	                JOptionPane.showMessageDialog(view.getFrame(), "Reportaje exportado correctamente en:\n" + archivoDestino.getName());
	            } catch (java.io.IOException ex) {
	                JOptionPane.showMessageDialog(view.getFrame(), "Error al escribir el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    });
	}
	
	public void mostrarVista() {
		view.getFrame().setVisible(true);
	}
	
	
}
