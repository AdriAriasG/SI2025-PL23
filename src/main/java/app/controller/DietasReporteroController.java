package app.controller;

import java.util.List;

import app.dto.EventoDTO;
import app.dto.DietaDTO;
import app.model.DietasReporteroModel;
import app.view.DietasReporteroView;

public class DietasReporteroController {

    private DietasReporteroModel model;
    private DietasReporteroView view;
    private int idReportero;

    private List<EventoDTO> listaEventos;

    public DietasReporteroController(DietasReporteroModel model,
                                     DietasReporteroView view,
                                     int idReportero) {

        this.model = model;
        this.view = view;
        this.idReportero = idReportero;

        inicializar();
    }

    private void inicializar() {

        // Cargar eventos asignados
        listaEventos = model.getEventosAsignados(idReportero);

        view.setEventos(listaEventos);

        // Listener de selección
        view.addEventoSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                procesarSeleccionEvento();
            }
        });

        view.mostrarVista();
    }

    private void procesarSeleccionEvento() {

        int index = view.getEventoSeleccionadoIndex();

        if (index < 0) {
            view.limpiarResultados();
            return;
        }

        EventoDTO evento = listaEventos.get(index);

        try {

            DietaDTO dieta = model.calcularDietas(
                    evento.getId(),
                    idReportero
            );

            view.mostrarDieta(evento, dieta);

        } catch (Exception ex) {

            view.limpiarResultados();

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
}