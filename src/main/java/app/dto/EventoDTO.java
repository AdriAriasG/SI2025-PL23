package app.dto;

public class EventoDTO {
    private int id;
    private String nombre;
    private String fecha;
    private int idAgencia;

    // Constructor vacío para DbUtils
    public EventoDTO() {}

    public EventoDTO(int id, String nombre, String fecha, int idAgencia) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.idAgencia = idAgencia;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getIdAgencia() { return idAgencia; }
    public void setIdAgencia(int idAgencia) { this.idAgencia = idAgencia; }

    @Override
    public String toString() {
        return nombre + " (" + fecha + ")";
    }
}