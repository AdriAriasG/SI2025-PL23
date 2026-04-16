package app.dto;

public class OfrecimientoDTO {
    private int id;
    private String nombre;
    private String fecha;
    private String estado;
    private int accesoConcedido; 
    private double precio;
    
    // NUEVOS CAMPOS PARA LA HU #34072 (Embargos)
    private int accesoEspecial;      // 1 si es un reportaje embargado, 0 si no
    private String fechaFinEmbargo;  // La fecha que mostraremos en la nueva columna
    
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

    // GETTERS Y SETTERS PARA LOS NUEVOS CAMPOS
    public int getAccesoEspecial() { return accesoEspecial; }
    public void setAccesoEspecial(int accesoEspecial) { this.accesoEspecial = accesoEspecial; }

    public String getFechaFinEmbargo() { return fechaFinEmbargo; }
    public void setFechaFinEmbargo(String fechaFinEmbargo) { this.fechaFinEmbargo = fechaFinEmbargo; }
}