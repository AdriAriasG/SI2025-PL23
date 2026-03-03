package app.controller;

import java.util.List;

import javax.swing.JOptionPane;

import app.dto.EventoDTO;
import app.dto.VersionDTO;
import app.model.ReportajeModel;
import app.view.RestaurarVersionView;
import giis.demo.util.SwingUtil;

public class RestaurarVersionController {

    private ReportajeModel model;
    private RestaurarVersionView view;
    private int idReportero;

    private List<EventoDTO> listaEventos;
    private List<VersionDTO> listaVersiones;

    public RestaurarVersionController(ReportajeModel model,
                                      RestaurarVersionView view,
                                      int idReportero) {
        this.model = model;
        this.view = view;
        this.idReportero = idReportero;

        initView();
        initController();
    }

    private void initView() {
        cargarEventos();
        view.getFrame().setVisible(true);
    }

    private void initController() {

        // Selección de evento
        view.getTablaEventos().getSelectionModel().addListSelectionListener(e ->
            SwingUtil.exceptionWrapper(() -> cargarVersiones())
        );

        // Selección de versión
        view.getTablaVersiones().getSelectionModel().addListSelectionListener(e ->
            SwingUtil.exceptionWrapper(() -> mostrarComparacion())
        );

        // Botón restaurar
        view.getBtnRestaurar().addActionListener(e ->
            SwingUtil.exceptionWrapper(() -> restaurar())
        );
    }

    // --------------------------------------------------
    // CARGAR EVENTOS
    // --------------------------------------------------

    private void cargarEventos() {

        listaEventos = model.getEventosAsignados(idReportero, true);

        String[] columnas = {"id", "nombre", "fecha"};

        view.getTablaEventos().setModel(
                SwingUtil.getTableModelFromPojos(listaEventos, columnas));

        SwingUtil.autoAdjustColumns(view.getTablaEventos());
    }

    // --------------------------------------------------
    // CARGAR VERSIONES
    // --------------------------------------------------

    private void cargarVersiones() {

        int fila = view.getTablaEventos().getSelectedRow();
        if (fila < 0) return;

        EventoDTO evento = listaEventos.get(fila);

        listaVersiones = model.getVersiones(evento.getId());

        String[] columnas = {"id", "fecha_hora", "cambios_realizados"};

        view.getTablaVersiones().setModel(
                SwingUtil.getTableModelFromPojos(listaVersiones, columnas));

        SwingUtil.autoAdjustColumns(view.getTablaVersiones());

        mostrarVersionActual(evento.getId());
    }

    // --------------------------------------------------
    // MOSTRAR VERSION ACTUAL
    // --------------------------------------------------

    private void mostrarVersionActual(int idEvento) {

        VersionDTO actual = model.getVersionActual(idEvento);

        if (actual != null) {
            view.getTxtSubtituloActual().setText(actual.getSubtitulo());
            view.getTxtCuerpoActual().setText(actual.getCuerpo());
        }
    }

    // --------------------------------------------------
    // MOSTRAR VERSION SELECCIONADA
    // --------------------------------------------------

    private void mostrarComparacion() {

        int fila = view.getTablaVersiones().getSelectedRow();
        if (fila < 0) return;

        VersionDTO version = listaVersiones.get(fila);

        view.getTxtSubtituloSeleccionado().setText(version.getSubtitulo());
        view.getTxtCuerpoSeleccionado().setText(version.getCuerpo());
    }

    // --------------------------------------------------
    // RESTAURAR
    // --------------------------------------------------

    private void restaurar() {

        int filaEvento = view.getTablaEventos().getSelectedRow();
        int filaVersion = view.getTablaVersiones().getSelectedRow();

        if (filaEvento < 0)
            throw new RuntimeException("Debe seleccionar un evento");

        if (filaVersion < 0)
            throw new RuntimeException("Debe seleccionar una versión");

        boolean restaurarSubtitulo = view.getChkSubtitulo().isSelected();
        boolean restaurarCuerpo = view.getChkCuerpo().isSelected();

        if (!restaurarSubtitulo && !restaurarCuerpo)
            throw new RuntimeException("Debe seleccionar qué desea restaurar");

        EventoDTO evento = listaEventos.get(filaEvento);
        VersionDTO version = listaVersiones.get(filaVersion);

        model.restaurarVersion(
                evento.getId(),
                idReportero,
                version.getId(),
                restaurarSubtitulo,
                restaurarCuerpo
        );

        JOptionPane.showMessageDialog(view.getFrame(),
                "Restauración realizada correctamente");

        // Refrescar versiones y vista
        cargarVersiones();
    }
}