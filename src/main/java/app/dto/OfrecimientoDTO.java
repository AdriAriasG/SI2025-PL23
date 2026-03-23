package app.dto;

public class OfrecimientoDTO {
	private int id;
	private String nombre;
	private String fecha;
	private String estado;
	private int accesoConcedido; // Cambiado para que coincida con el alias SQL
	private double precio;
	
	public OfrecimientoDTO() {}
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public String getFecha() { return fecha; }
	public void setFecha(String fecha) { this.fecha = fecha; }
	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }
	public int getAccesoConcedido() { return accesoConcedido; }
	public void setAccesoConcedido(int accesoConcedido) { this.accesoConcedido = accesoConcedido; }
	public double getPrecio() { return precio; }
	public void setPrecio(double precio) { this.precio = precio; } 
}