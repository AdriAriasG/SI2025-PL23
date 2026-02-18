package app.dto;

public class ReporteroDTO {
    private int id;
    private String nombre;
    private int idAgencia;

    // Constructor vacío para DbUtils
    public ReporteroDTO() {}

    public ReporteroDTO(int id, String nombre, int idAgencia) {
        this.id = id;
        this.nombre = nombre;
        this.idAgencia = idAgencia;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getIdAgencia() { return idAgencia; }
    public void setIdAgencia(int idAgencia) { this.idAgencia = idAgencia; }

    @Override
    public String toString() {
        return nombre; // Para que se vea el nombre en JComboBox/JList
    }
}