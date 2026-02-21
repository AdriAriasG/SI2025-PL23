package app.dto;

public class ReportajeDTO {
	
	private String titulo;
	private String subtitulo;
	private String cuerpo;
	private int version;
	private String nombreEvento;
	
	public ReportajeDTO() {}
	
	public String getTitulo() {return titulo;}
	public void setTitulo() {this.titulo = titulo;}
	
	public String getSubtitulo() {return subtitulo;}
	public void setSubtitulo() {this.subtitulo = subtitulo;}
	
	public String getCuerpo() {return cuerpo;}
	public void setCuerpo() {this.cuerpo = cuerpo;}
	
	public int getVersion() {return version;}
	public void setVersion() {this.version = version;}
	
	public String getNombreEvento() {return nombreEvento;}
	public void setNNombreEvento() {this.nombreEvento = nombreEvento;}
	
}
