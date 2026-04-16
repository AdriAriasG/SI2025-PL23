package app.dto;

public class EventoDTO {

	private int id;
	private String nombre;

	private String fecha;

	private String fechaInicio;
	private String fechaFin;

	private int idAgencia;
	private boolean asignacionFinalizada;

	private int idProvincia;
	private String nombreProvincia;
	private String nombrePais;

	public EventoDTO() {}

	public EventoDTO(int id, String nombre, String fecha, int idAgencia) {
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.idAgencia = idAgencia;
	}

	public EventoDTO(int id, String nombre, String fecha, String fechaInicio, String fechaFin, int idAgencia, boolean asignacionFinalizada,
			int idProvincia, String nombreProvincia, String nombrePais) {
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.idAgencia = idAgencia;
		this.asignacionFinalizada = asignacionFinalizada;
		this.idProvincia = idProvincia;
		this.nombreProvincia = nombreProvincia;
		this.nombrePais = nombrePais;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getFecha() { return fecha; }
	public void setFecha(String fecha) { this.fecha = fecha; }

	public String getFechaInicio() { return fechaInicio; }
	public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

	public String getFechaFin() { return fechaFin; }
	public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

	public int getIdAgencia() { return idAgencia; }
	public void setIdAgencia(int idAgencia) { this.idAgencia = idAgencia; }

	public boolean isAsignacionFinalizada() { return asignacionFinalizada; }
	public void setAsignacionFinalizada(boolean asignacionFinalizada) { this.asignacionFinalizada = asignacionFinalizada; }

	public int getIdProvincia() { return idProvincia; }
	public void setIdProvincia(int idProvincia) { this.idProvincia = idProvincia; }

	public String getNombreProvincia() { return nombreProvincia; }
	public void setNombreProvincia(String nombreProvincia) { this.nombreProvincia = nombreProvincia; }

	public String getNombrePais() { return nombrePais; }
	public void setNombrePais(String nombrePais) { this.nombrePais = nombrePais; }

	@Override
	public String toString() {
		return nombre + " (" + fecha + ")";
	}
}