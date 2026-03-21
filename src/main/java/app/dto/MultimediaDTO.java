package app.dto;

public class MultimediaDTO {

    private Integer id;
    private String ruta;
    private String tipo;
    private String estado;
    private Integer idAutor;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getIdAutor() { return idAutor; }
    public void setIdAutor(Integer idAutor) { this.idAutor = idAutor; }
}