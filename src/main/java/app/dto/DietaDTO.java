package app.dto;

public class DietaDTO {

    private double importeAlojamiento;
    private double importeManutencion;
    private int numeroDias;
    private double total;

    public DietaDTO(double alojamiento,
                    double manutencion,
                    int dias,
                    double total) {

        this.importeAlojamiento = alojamiento;
        this.importeManutencion = manutencion;
        this.numeroDias = dias;
        this.total = total;
    }

    public double getImporteAlojamiento() {
        return importeAlojamiento;
    }

    public double getImporteManutencion() {
        return importeManutencion;
    }

    public int getNumeroDias() {
        return numeroDias;
    }

    public double getTotal() {
        return total;
    }

}
