package app.dto;

public class ReportajeRevisionResumenDTO {

    private Integer idReportaje;
    private String titulo;
    private String nombreEvento;
    private String fecha;

    public ReportajeRevisionResumenDTO() {
    }

    public Integer getIdReportaje() {
        return idReportaje;
    }

    public void setIdReportaje(Integer idReportaje) {
        this.idReportaje = idReportaje;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}