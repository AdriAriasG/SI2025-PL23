package app.dto;

public class ReporteroDTO {
    private int id;
    private String nombre;
    private String tipo; // GRAFICO, CAMAROGRAFO, BASE
    private Integer idAgencia; // Integer (nullable) para soportar freelances (id_agencia NULL)
    private String email;

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

    public Integer getIdAgencia() { return idAgencia; }
    public void setIdAgencia(Integer idAgencia) { this.idAgencia = idAgencia; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return nombre; // Para que se vea el nombre en JComboBox/JList
    }
}