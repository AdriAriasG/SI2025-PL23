package app.dto;

import java.util.List;

/**
 * DTO para almacenar la información del informe de un evento (HU #33548)
 */
public class InformeEventoDTO {
    private String nombreEvento;
    private String fechaEvento;
    private List<String> reporterosAsignados;
    private boolean tieneReportaje;
    private String nombreAutor;
    private List<String> empresasConAcceso;

    public InformeEventoDTO() {}

    // Getters y Setters
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }

    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }

    public List<String> getReporterosAsignados() { return reporterosAsignados; }
    public void setReporterosAsignados(List<String> reporterosAsignados) { this.reporterosAsignados = reporterosAsignados; }

    public boolean isTieneReportaje() { return tieneReportaje; }
    public void setTieneReportaje(boolean tieneReportaje) { this.tieneReportaje = tieneReportaje; }

    public String getNombreAutor() { return nombreAutor; }
    public void setNombreAutor(String nombreAutor) { this.nombreAutor = nombreAutor; }

    public List<String> getEmpresasConAcceso() { return empresasConAcceso; }
    public void setEmpresasConAcceso(List<String> empresasConAcceso) { this.empresasConAcceso = empresasConAcceso; }
}