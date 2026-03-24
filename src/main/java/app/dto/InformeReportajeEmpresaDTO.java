package app.dto;

public class InformeReportajeEmpresaDTO {
    private String tituloReportaje;
    private String nombreEvento;
    private String fechaEvento;
    private double precio;

    public String getTituloReportaje() {
        return tituloReportaje;
    }

    public void setTituloReportaje(String tituloReportaje) {
        this.tituloReportaje = tituloReportaje;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(String fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
