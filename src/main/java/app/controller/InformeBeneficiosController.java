package app.controller;

import java.util.List;
import app.dto.InformeBeneficiosDTO;
import app.dto.TematicaDTO;
import app.model.InformeBeneficiosModel;
import app.view.InformeBeneficiosView;
import app.dto.AgenciaDTO;

public class InformeBeneficiosController {
    private InformeBeneficiosModel model;
    private InformeBeneficiosView view;
    private List<InformeBeneficiosDTO> listaEventos;
    private int idAgencia;

    public InformeBeneficiosController(InformeBeneficiosModel model, InformeBeneficiosView view, AgenciaDTO agencia) {
        this.model = model;
        this.view = view;
        this.idAgencia = agencia.getId();
        init();
    }

    private void init() {
        // Cargar temáticas al inicio
        for (TematicaDTO t : model.getTodasTematicas()) {
            view.getCbTematicas().addItem(t.getNombre());
        }

        // ACCIÓN DEL BOTÓN GENERAR
        view.getBtnGenerar().addActionListener(e -> {
            String tema = (String) view.getCbTematicas().getSelectedItem();
            
            // --- LIMPIEZA PREVIA (Esto es lo que faltaba) ---
            view.getModelEventos().setRowCount(0);           // Borra eventos anteriores
            view.getModelTarifaPlana().setRowCount(0);       // Borra empresas anteriores
            view.getModelPagosIndividuales().setRowCount(0); // Borra pagos anteriores
            view.setBalance(0, 0);                           // Resetea el cuadro de TOTAL a cero
            // ------------------------------------------------
            
            listaEventos = model.getEventosPorAgenciaYTematica(idAgencia, tema);
            
            for (InformeBeneficiosDTO ev : listaEventos) {
                view.getModelEventos().addRow(new Object[]{ ev.getNombreEvento() + " (" + ev.getFecha() + ")" });
            }
        });

        // Listener de selección de la tabla de eventos
        view.getTableEventos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = view.getTableEventos().getSelectedRow();
                if (fila != -1) {
                    cargarDesglose(listaEventos.get(fila).getIdEvento());
                }
            }
        });
        
        view.getFrame().setVisible(true);
    }

    private void cargarDesglose(int idEvento) {
        List<InformeBeneficiosDTO> detalles = model.getDesgloseEmpresas(idEvento);
        
        // Aquí sí limpiamos solo las de abajo porque estamos cambiando de evento
        view.getModelTarifaPlana().setRowCount(0);
        view.getModelPagosIndividuales().setRowCount(0);
        
        double sumT = 0, sumP = 0;

        for (InformeBeneficiosDTO d : detalles) {
            if (d.isTarifaPlana()) {
                view.getModelTarifaPlana().addRow(new Object[]{ d.getNombreEmpresa(), d.getImporte() + "€" });
                sumT += d.getImporte();
            } else {
                view.getModelPagosIndividuales().addRow(new Object[]{ d.getNombreEmpresa(), d.getImporte() + "€" });
                sumP += d.getImporte();
            }
        }
        view.setBalance(sumT, sumP);
    }
}