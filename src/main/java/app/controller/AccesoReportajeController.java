package app.controller;

import app.dto.ReportajeDTO;
import app.model.AccesoReportajeModel;
import app.view.AccesoReportajeView;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.*;

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
        configurarBotonDescarga();

        view.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarVisor();
            }
        });

        view.getFrame().setVisible(true);
    }
    
    public void cargarTabla(){
        reportajesActuales = model.getReportajesConAcceso(this.idEmpresa);
        view.getModel().setRowCount(0);
        
        if(reportajesActuales.isEmpty()) {
            view.mostrarMensajeVacio(true);
        } else {
            view.mostrarMensajeVacio(false);
            for(ReportajeDTO r: reportajesActuales) {
                view.getModel().addRow(new Object[] { r.getNombreEvento(), r.getFecha() });
            }
        }
    }
    
    private void actualizarVisor() {
        int fila = view.getTable().getSelectedRow();
        if (fila != -1) {
            ReportajeDTO seleccionado = reportajesActuales.get(fila);
            
            // Pasamos los parámetros. 
            // seleccionado.getFechaEmbargo() ahora devuelve un String, 
            // que es lo que la nueva View espera recibir.
            view.actualizarReportaje(
                    seleccionado.getTitulo(),
                    seleccionado.getSubtitulo(),
                    seleccionado.getCuerpo(),
                    seleccionado.getArchivosMultimedia(),
                    seleccionado.getFechaEmbargo(), // Es un String
                    seleccionado.isAccesoEspecial() // Es un boolean
            );
        }
    }
    
    private void configurarBotonDescarga() {
        view.getBtnDescargarJSON().addActionListener(e -> {
            int fila = view.getTable().getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(view.getFrame(), "Por favor, selecciona un reportaje.");
                return;
            }

            ReportajeDTO sel = reportajesActuales.get(fila);
            
            // Generar JSON
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"evento\": \"").append(sel.getNombreEvento()).append("\",\n");
            json.append("  \"fecha\": \"").append(sel.getFecha()).append("\",\n");
            json.append("  \"titulo\": \"").append(sel.getTitulo()).append("\",\n");
            json.append("  \"subtitulo\": \"").append(sel.getSubtitulo()).append("\",\n");
            
            String cuerpoOriginal = sel.getCuerpo() != null ? sel.getCuerpo() : "";
            String cuerpoEscapado = cuerpoOriginal.replace("\"", "\\\"").replace("\n", "\\n");
            
            json.append("  \"cuerpo\": \"").append(cuerpoEscapado).append("\",\n");
            json.append("  \"multimedia\": [\n");
            
            List<String> media = sel.getArchivosMultimedia();
            if (media != null) {
                for (int i = 0; i < media.size(); i++) {
                    json.append("    \"").append(media.get(i)).append("\"");
                    if (i < media.size() - 1) json.append(",");
                    json.append("\n");
                }
            }
            json.append("  ]\n}");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reportaje JSON");
            String nombreSugerido = "reportaje_" + sel.getNombreEvento().replaceAll("\\s+", "_") + ".json";
            fileChooser.setSelectedFile(new java.io.File(nombreSugerido));

            if (fileChooser.showSaveDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
                java.io.File archivoDestino = fileChooser.getSelectedFile();
                if (!archivoDestino.getName().toLowerCase().endsWith(".json")) {
                    archivoDestino = new java.io.File(archivoDestino.getAbsolutePath() + ".json");
                }

                try (java.io.FileWriter writer = new java.io.FileWriter(archivoDestino)) {
                    writer.write(json.toString());
                    
                    // ACTUALIZACIÓN DE BASE DE DATOS
                    model.marcarComoDescargado(sel.getIdEvento(), this.idEmpresa);
                    
                    JOptionPane.showMessageDialog(view.getFrame(), "Descarga completada y registro actualizado.");
                } catch (java.io.IOException ex) {
                    JOptionPane.showMessageDialog(view.getFrame(), "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void mostrarVista() { view.getFrame().setVisible(true); }
}