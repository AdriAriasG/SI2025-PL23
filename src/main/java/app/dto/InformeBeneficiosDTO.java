package app.dto;

public class InformeBeneficiosDTO {
    private int idEvento;
    private String nombreEvento;
    private String fecha;
    private String nombreEmpresa;
    private double importe;
    private int tieneTarifaPlana; // 1 si es tarifa plana, 0 si no

    public InformeBeneficiosDTO() {}

    // Getters y Setters
    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }
    public int getTieneTarifaPlana() { return tieneTarifaPlana; }
    public void setTieneTarifaPlana(int tieneTarifaPlana) { this.tieneTarifaPlana = tieneTarifaPlana; }

    public boolean isTarifaPlana() { return tieneTarifaPlana == 1; }
}