package app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import app.dto.EmpresaComunicacionDTO;
import app.dto.InformeReportajeEmpresaDTO;
import app.model.InformeReportajeEmpresaModel;
import app.view.InformeReportajeEmpresaView;

public class InformeReportajeEmpresaController {
    private InformeReportajeEmpresaModel model;
    private InformeReportajeEmpresaView view;
    private EmpresaComunicacionDTO empresa;

    public InformeReportajeEmpresaController(InformeReportajeEmpresaModel model, InformeReportajeEmpresaView view, EmpresaComunicacionDTO empresa) {
        this.model = model;
        this.view = view;
        this.empresa = empresa;
        this.initController();
        this.view.getFrame().setVisible(true);
    }

    private void initController() {
        view.getBtnGenerar().addActionListener(e -> generarInforme());
        view.getBtnCerrar().addActionListener(e -> view.getFrame().dispose());
    }

    private void generarInforme() {
        String fechaInicio = view.getTxtFechaInicio().getText().trim();
        String fechaFin = view.getTxtFechaFin().getText().trim();

        if (!esFechaValida(fechaInicio) || !esFechaValida(fechaFin)) {
            view.showError("Las fechas deben tener el formato YYYY-MM-DD y ser fechas válidas.");
            return;
        }

        if (fechaInicio.compareTo(fechaFin) > 0) {
            view.showError("La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }

        List<InformeReportajeEmpresaDTO> reportajes = model.getReportajesAccesibles(empresa.getId(), fechaInicio, fechaFin);
        
        DefaultTableModel tablaModel = view.getModeloReportajes();
        tablaModel.setRowCount(0); // Limpiar tabla

        double precioTotal = 0.0;

        for (InformeReportajeEmpresaDTO rep : reportajes) {
            tablaModel.addRow(new Object[]{
                rep.getTituloReportaje(),
                rep.getNombreEvento(),
                rep.getFechaEvento(),
                String.format("%.2f", rep.getPrecio())
            });
            precioTotal += rep.getPrecio();
        }

        view.getLblPrecioTotal().setText(String.format("Precio Total: %.2f €", precioTotal));
    }

    private boolean esFechaValida(String fecha) {
        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
