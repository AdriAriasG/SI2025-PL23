package app.dto;

public class ReporteroDTO {
    private int id;
    private String nombre;
    private String tipo; // GRAFICO, CAMAROGRAFO, BASE
    private int idAgencia;

    // Constructor vacío para DbUtils
    public ReporteroDTO() {}

    public ReporteroDTO(int id, String nombre, String tipo, int idAgencia) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.idAgencia = idAgencia;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getIdAgencia() { return idAgencia; }
    public void setIdAgencia(int idAgencia) { this.idAgencia = idAgencia; }

    @Override
    public String toString() {
        return nombre; // Para que se vea el nombre en JComboBox/JList
    }
}