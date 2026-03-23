package app.dto;

public class RevisionDTO {

    private Integer idReportaje;
    private Integer idReportero;
    private String comentario;
    private String estado;

    public RevisionDTO() {}

    public Integer getIdReportaje() {
        return idReportaje;
    }

    public void setIdReportaje(Integer idReportaje) {
        this.idReportaje = idReportaje;
    }

    public Integer getIdReportero() {
        return idReportero;
    }

    public void setIdReportero(Integer idReportero) {
        this.idReportero = idReportero;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isFinalizada() {
        return "FINALIZADA".equals(estado);
    }
}