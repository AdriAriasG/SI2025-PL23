package app.dto;

import java.util.List;
import java.util.Date;

public class ReportajeDTO {
    private int version;      // Es el ID de la versión del reportaje
    private int idEvento;     // Lo necesitamos para el UPDATE del ofrecimiento
    private String nombreEvento;
    private String fecha;
    private String titulo;
    private String subtitulo;
    private String cuerpo;
    private List<String> archivosMultimedia;
    
    // Nuevos campos para la HU #34062
    private String fechaEmbargo;
    private boolean accesoEspecial;

    public ReportajeDTO() {}

    // Getters y Setters existentes
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }

    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }

    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }

    public List<String> getArchivosMultimedia() { return archivosMultimedia; }
    public void setArchivosMultimedia(List<String> archivosMultimedia) { this.archivosMultimedia = archivosMultimedia; }

    // Nuevos Getters y Setters
    public String getFechaEmbargo() { return fechaEmbargo; }
    public void setFechaEmbargo(String fechaEmbargo) { this.fechaEmbargo = fechaEmbargo; }

    public boolean isAccesoEspecial() { return accesoEspecial; }
    public void setAccesoEspecial(boolean accesoEspecial) { this.accesoEspecial = accesoEspecial; }
}