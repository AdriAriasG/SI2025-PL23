package app.dto;

import java.util.List;

public class ReportajeDTO {
	
	private String titulo;
	private String subtitulo;
	private String cuerpo;
	private int version;
	private String nombreEvento;
	private String fecha;
	private List<String> archivosMultimedia;

	
	public ReportajeDTO() {}
	
	public String getTitulo() {return titulo;}
	public void setTitulo(String titulo) {this.titulo = titulo;}
	
	public String getSubtitulo() {return subtitulo;}
	public void setSubtitulo(String subtitulo) {this.subtitulo = subtitulo;}
	
	public String getCuerpo() {return cuerpo;}
	public void setCuerpo(String cuerpo) {this.cuerpo = cuerpo;}
	
	public int getVersion() {return version;}
	public void setVersion(int version) {this.version = version;}
	
	public String getNombreEvento() {return nombreEvento;}
	public void setNombreEvento(String nombreEvento) {this.nombreEvento = nombreEvento;}
	
	public String getFecha() {return fecha;}
	public void setFecha(String fecha) {this.fecha = fecha;}

    public java.util.List<String> getArchivosMultimedia() { return archivosMultimedia; }
    public void setArchivosMultimedia(java.util.List<String> archivos) { this.archivosMultimedia = archivos; }
	
}
