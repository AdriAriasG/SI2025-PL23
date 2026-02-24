package app.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import app.dto.AgenciaDTO;
import app.dto.EventoDTO;
import app.dto.InformeEventoDTO;
import app.model.InformeModel;
import app.view.InformeView;

/**
 * Controlador para el informe de eventos.
 * Cubre la HU #33548.
 */
public class InformeController {
    private InformeModel model;
    private InformeView view;
    private AgenciaDTO agencia;

    public InformeController(InformeModel model, InformeView view, AgenciaDTO agencia) {
        this.model = model;
        this.view = view;
        this.agencia = agencia;
        this.initView();
        this.initController();
    }

    /**
     * Inicializa la vista con los datos
     */
    public void initView() {
        cargarEventos();
        view.limpiarInforme();
        view.getFrame().setVisible(true);
    }

    /**
     * Inicializa los controladores de eventos
     */
    public void initController() {
        // Evento: botón generar informe
        view.getBtnGenerar().addActionListener(e -> onGenerarInforme());

        // Evento: botón exportar CSV (solo visual, sin funcionalidad)
        view.getBtnExportarCSV().addActionListener(e -> onExportarCSV());

        // Evento: botón cerrar
        view.getBtnCerrar().addActionListener(e -> view.getFrame().dispose());

        // Evento: doble clic en tabla para generar informe directamente
        view.getTablaEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onGenerarInforme();
                }
            }
        });
    }

    /**
     * Carga los eventos de la agencia
     */
    private void cargarEventos() {
        List<EventoDTO> eventos = model.getEventosAgencia(agencia.getId());
        view.setEventos(eventos);
    }

    /**
     * Genera el informe del evento seleccionado
     */
    private void onGenerarInforme() {
        int idEvento = view.getIdEventoSeleccionado();
        if (idEvento == -1) {
            view.showError("Debe seleccionar un evento de la lista.");
            return;
        }

        String nombreEvento = view.getNombreEventoSeleccionado();
        String fechaEvento = view.getFechaEventoSeleccionado();

        InformeEventoDTO informe = model.generarInforme(idEvento, nombreEvento, fechaEvento);
        view.mostrarInforme(informe);
    }

    /**
     * Acción del botón exportar CSV
     */
    private void onExportarCSV() {
        InformeEventoDTO informe = view.getInformeActual();
        if (informe == null) {
            view.showError("Debe generar un informe antes de exportarlo.");
            return;
        }

        // Crear selector de archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar informe como CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        fileChooser.setSelectedFile(new java.io.File("informe_" + sanitizeFileName(informe.getNombreEvento()) + ".csv"));

        int userSelection = fileChooser.showSaveDialog(view.getFrame());

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();

            // Asegurar extensión .csv
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
            }

            try {
                exportarInformeCSV(informe, fileToSave);
                view.showInfo("Informe exportado correctamente a:\n" + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                view.showError("Error al exportar el informe:\n" + e.getMessage());
            }
        }
    }

    /**
     * Exporta el informe a un archivo CSV
     */
    private void exportarInformeCSV(InformeEventoDTO informe, java.io.File archivo) throws IOException {
        try (FileWriter writer = new FileWriter(archivo)) {
            // Cabecera del informe
            writer.append("INFORME DEL EVENTO\n");
            writer.append("Evento,").append(escapeCSV(informe.getNombreEvento())).append("\n");
            writer.append("Fecha,").append(escapeCSV(informe.getFechaEvento())).append("\n");
            writer.append("\n");

            // Reporteros asignados
            writer.append("REPORTEROS ASIGNADOS\n");
            List<String> reporteros = informe.getReporterosAsignados();
            if (reporteros == null || reporteros.isEmpty()) {
                writer.append("(No hay reporteros asignados)\n");
            } else {
                for (String nombre : reporteros) {
                    writer.append(escapeCSV(nombre)).append("\n");
                }
            }
            writer.append("\n");

            // Reportaje
            writer.append("REPORTAJE\n");
            writer.append("Estado,").append(informe.isTieneReportaje() ? "ENTREGADO" : "NO ENTREGADO").append("\n");
            if (informe.isTieneReportaje()) {
                writer.append("Autor,").append(escapeCSV(informe.getNombreAutor() != null ? informe.getNombreAutor() : "Desconocido")).append("\n");
            }
            writer.append("\n");

            // Empresas con acceso
            writer.append("EMPRESAS CON ACCESO AL REPORTAJE\n");
            List<String> empresas = informe.getEmpresasConAcceso();
            if (empresas == null || empresas.isEmpty()) {
                writer.append("(No hay empresas con acceso)\n");
            } else {
                for (String nombre : empresas) {
                    writer.append(escapeCSV(nombre)).append("\n");
                }
            }
        }
    }

    /**
     * Escapa caracteres especiales para CSV
     */
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        // Si contiene comas, comillas o saltos de línea, rodear con comillas
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Sanitiza un nombre de archivo eliminando caracteres no válidos
     */
    private String sanitizeFileName(String name) {
        if (name == null) {
            return "informe";
        }
        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}