package app.dto;

public class VersionDTO {
	private int id;
	private String subtitulo;
	private String cuerpo;
	private String fecha_hora;
	private String cambios_realizados;
	private int id_reportero_modificador;	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}
	public String getCuerpo() {
		return cuerpo;
	}
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	public String getFecha_hora() {
		return fecha_hora;
	}
	public void setFecha_hora(String fecha_hora) {
		this.fecha_hora = fecha_hora;
	}
	public String getCambios_realizados() {
		return cambios_realizados;
	}
	public void setCambios_realizados(String cambios_realizados) {
		this.cambios_realizados = cambios_realizados;
	}
	public int getId_reportero_modificador() {
		return id_reportero_modificador;
	}
	public void setId_reportero_modificador(int id_reportero_modificador) {
		this.id_reportero_modificador = id_reportero_modificador;
	}
}
